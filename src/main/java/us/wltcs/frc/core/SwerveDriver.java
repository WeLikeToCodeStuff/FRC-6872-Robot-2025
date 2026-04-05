package us.wltcs.frc.core;

import com.pathplanner.lib.config.ModuleConfig;
import com.pathplanner.lib.controllers.PathFollowingController;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.path.Waypoint;
import com.pathplanner.lib.trajectory.PathPlannerTrajectory;
import com.pathplanner.lib.trajectory.PathPlannerTrajectoryState;
import com.pathplanner.lib.util.PPLibTelemetry;
import com.pathplanner.lib.util.PathPlannerLogging;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import swervelib.SwerveDrive;
import swervelib.parser.SwerveParser;
import swervelib.parser.json.SwerveDriveJson;
import swervelib.telemetry.SwerveDriveTelemetry;
import us.wltcs.frc.core.devices.output.SwerveModule;
import us.wltcs.frc.core.logging.Context;
import us.wltcs.frc.core.math.vector2.Vector2d;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;

import static edu.wpi.first.wpilibj.RobotBase.isSimulation;

public class SwerveDriver {
  private final float driveSpeed;
  private final float turnSpeed;

  private final PPHolonomicDriveController autoDriveController;

  //  private Vector2d position;
  private double rotationRadians;

  private final SwerveDrive swerveDriver;
  private final Robot robot;

  // TODO: group these into their own autonomous class
  private PathPlannerPath path;
  private PathPlannerTrajectory currentTrajectory;
  private final Timer timer = new Timer();

//  private final PathFollowingController controller;

  public SwerveDriver(float driveSpeed, float turnSpeed, Robot robot) {
    this.driveSpeed = driveSpeed;
    this.turnSpeed = turnSpeed;
    this.robot = robot;

    if (isSimulation())
      SwerveDriveTelemetry.verbosity = SwerveDriveTelemetry.TelemetryVerbosity.HIGH;

    File swerveJsonDirectory = new File(Filesystem.getDeployDirectory(), "swerve/sparkmax");
    try {
      this.swerveDriver = new SwerveParser(swerveJsonDirectory).createSwerveDrive(
              this.driveSpeed,
              new Pose2d(new Translation2d(), new Rotation2d())
      );
      System.out.println(swerveDriver.getPose());
    } catch (IOException e) {
      Context.movement.logError("Failed to initialize swerve modules");
      throw new RuntimeException(e);
    }
    swerveDriver.setCosineCompensator(false);
    swerveDriver.setHeadingCorrection(false);
    swerveDriver.zeroGyro();

    autoDriveController = new PPHolonomicDriveController(
            new PIDConstants(5.0, 0.0, 0.0),  // Translation PID constants
            new PIDConstants(5.0, 0.0, 0.0)   // Rotation PID constants
    );

    try {
      path = PathPlannerPath.fromPathFile("go");
      if (path.getStartingHolonomicPose().isPresent())
        swerveDriver.resetOdometry(path.getStartingHolonomicPose().get());
    } catch (Exception e) {
      Context.movement.logError("Failed to load path: " + e.getMessage());
    }
//    try {
//      final RobotConfig config = RobotConfig.fromGUISettings();
//      AutoBuilder.configure(
//              swerveDriver::getPose, // Robot pose supplier
//              this::resetPose, // Method to reset odometry (will be called if your auto has a starting pose)
//              this::getRobotRelativeSpeeds, // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE
//              (speeds, feedforwards) -> swerveDriver.drive(speeds),
//              new PPHolonomicDriveController( // PPHolonomicController is the built in path following controller for holonomic drive trains
//                      new PIDConstants(5.0, 0.0, 0.0), // Translation PID constants
//                      new PIDConstants(5.0, 0.0, 0.0) // Rotation PID constants
//              ),
//              config,
//              () -> {
//                var alliance = DriverStation.getAlliance();
//                if (alliance.isPresent()) {
//                  return alliance.get() == DriverStation.Alliance.Red;
//                }
//                return false;
//              }
//      );
//    } catch (Exception e) {
//      Context.movement.logError("Failed to load path planner config from GUI settings");
//    }

  }

  private double timeOffset = 0;
  public void drive() {
    Pose2d currentPose = swerveDriver.getPose();
    ChassisSpeeds currentSpeeds = swerveDriver.getRobotVelocity();
    currentTrajectory = new PathPlannerTrajectory(
            path, swerveDriver.getRobotVelocity(), swerveDriver.getPose().getRotation(),
            new RobotConfig(
                    swerveDriver.getModules()[0].configuration.physicalCharacteristics.robotMassKg,
                    6.8,
                    new ModuleConfig(
                            0.048, 5.0, 1.2, DCMotor.getKrakenX60(1).withReduction(6.14), 60.0, 1),
                    0.55)
    );

    // Find the two closest states in front of and behind robot
    int closestState1Idx = 0;
    int closestState2Idx = 1;
    while (closestState2Idx < currentTrajectory.getStates().size() - 1) {
      double closest2Dist =
              currentTrajectory
                      .getState(closestState2Idx)
                      .pose
                      .getTranslation()
                      .getDistance(currentPose.getTranslation());
      double nextDist =
              currentTrajectory
                      .getState(closestState2Idx + 1)
                      .pose
                      .getTranslation()
                      .getDistance(currentPose.getTranslation());
      if (nextDist < closest2Dist) {
        closestState1Idx++;
        closestState2Idx++;
      } else {
        break;
      }
    }


    // Use the closest 2 states to interpolate what the time offset should be
    // This will account for the delay in pathfinding
    var closestState1 = currentTrajectory.getState(closestState1Idx);
    var closestState2 = currentTrajectory.getState(closestState2Idx);

    double d =
            closestState1.pose.getTranslation().getDistance(closestState2.pose.getTranslation());
    double t =
            (currentPose.getTranslation().getDistance(closestState1.pose.getTranslation())) / d;
    t = MathUtil.clamp(t, 0.0, 1.0);

    timeOffset = MathUtil.interpolate(closestState1.timeSeconds, closestState2.timeSeconds, t);

    // If the robot is stationary and at the start of the path, set the time offset to the next
    // loop
    // This can prevent an issue where the robot will remain stationary if new paths come in
    // every loop
    if (timeOffset <= 0.02
            && Math.hypot(currentSpeeds.vxMetersPerSecond, currentSpeeds.vyMetersPerSecond) < 0.1) {
      timeOffset = 0.02;
    }

    if (currentTrajectory != null) {
      var targetState = currentTrajectory.sample(timer.get() + timeOffset);

      ChassisSpeeds targetSpeeds = autoDriveController.calculateRobotRelativeSpeeds(currentPose, targetState);

      double currentVel =
              Math.hypot(currentSpeeds.vxMetersPerSecond, currentSpeeds.vyMetersPerSecond);

      PPLibTelemetry.setCurrentPose(currentPose);
      PathPlannerLogging.logCurrentPose(currentPose);

      PPLibTelemetry.setTargetPose(targetState.pose);
      PathPlannerLogging.logTargetPose(targetState.pose);

      PPLibTelemetry.setVelocities(
              currentVel,
              targetState.linearVelocity,
              currentSpeeds.omegaRadiansPerSecond,
              targetSpeeds.omegaRadiansPerSecond);

      swerveDriver.drive(targetSpeeds);
    }
  }

  // fieldRelative defines whether the forward direction to move at is either the robot's forward direction or the field's
  public void drive(Vector2d movementInput, Vector2d rotationInput, boolean fieldRelative) {
    double moveMagnitude = movementInput.length();  // Get the magnitude (speed) of the movement input
    double rotateMagnitude = rotationInput.length();  // Get the magnitude (speed) of the rotation input

    Translation2d direction = new Translation2d(-movementInput.y, -movementInput.x).times(driveSpeed * moveMagnitude);
    double rotation = -rotationInput.x * turnSpeed * rotateMagnitude;

    SwerveModuleState[] states;
    if (fieldRelative)
      states = swerveDriver.toServeModuleStates(ChassisSpeeds.fromFieldRelativeSpeeds(
              direction.getX(), direction.getY(), rotation, Rotation2d.fromRadians(rotationRadians)
      ), false);
    else
      states = swerveDriver.toServeModuleStates(new ChassisSpeeds(direction.getX(), direction.getY(), rotation), false);

    swerveDriver.setModuleStates(states, false);

//    position = new Vector2d(swerveDriver.getPose().getX(), swerveDriver.getPose().getY());
    rotationRadians = swerveDriver.getPose().getRotation().getRadians();
  }

  // Makes the wheels point towards the center of robot to prevent any movement
  public void lock() {
    swerveDriver.lockPose();
  }
}

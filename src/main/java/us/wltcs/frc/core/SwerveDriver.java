package us.wltcs.frc.core;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import swervelib.SwerveDrive;
import swervelib.parser.SwerveParser;
import swervelib.telemetry.SwerveDriveTelemetry;
import us.wltcs.frc.core.devices.output.SwerveModule;
import us.wltcs.frc.core.logging.Context;
import us.wltcs.frc.core.math.vector2.Vector2d;

import java.io.File;
import java.io.IOException;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;

import static edu.wpi.first.wpilibj.RobotBase.isSimulation;

public class SwerveDriver {
  private final float driveSpeed;
  private final float turnSpeed;

  private Vector2d position;
  private double rotationRadians;

  private final SwerveDrive swerveDriver;
  private final Robot robot;

  public SwerveDriver(float driveSpeed, float turnSpeed, Vector2d position, Robot robot) {
    this.driveSpeed = driveSpeed;
    this.turnSpeed = turnSpeed;
    this.robot = robot;

    this.position = position;

    if (isSimulation())
      SwerveDriveTelemetry.verbosity = SwerveDriveTelemetry.TelemetryVerbosity.HIGH;

    File swerveJsonDirectory = new File(Filesystem.getDeployDirectory(), "swerve/sparkmax");
    try {
      this.swerveDriver = new SwerveParser(swerveJsonDirectory).createSwerveDrive(
        this.driveSpeed,
        new Pose2d(new Translation2d(position.x, position.y), new Rotation2d())
      );
      System.out.println(swerveDriver.getPose());
    } catch (IOException e) {
      Context.movement.logError("Failed to initialize swerve modules");
      throw new RuntimeException(e);
    }
    swerveDriver.setCosineCompensator(false);
    swerveDriver.setHeadingCorrection(false);
    swerveDriver.zeroGyro();

    this.resetPose();

    try {
      final RobotConfig config = RobotConfig.fromGUISettings();
      AutoBuilder.configure(
        swerveDriver::getPose, // Robot pose supplier
        this::resetPose, // Method to reset odometry (will be called if your auto has a starting pose)
        this::getRobotRelativeSpeeds, // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE
        (speeds, feedforwards) -> swerveDriver.drive(speeds),
        new PPHolonomicDriveController( // PPHolonomicController is the built in path following controller for holonomic drive trains
                new PIDConstants(5.0, 0.0, 0.0), // Translation PID constants
                new PIDConstants(5.0, 0.0, 0.0) // Rotation PID constants
        ),
        config,
        () -> {
          var alliance = DriverStation.getAlliance();
          if (alliance.isPresent()) {
            return alliance.get() == DriverStation.Alliance.Red;
          }
          return false;
        }
      );
    } catch (Exception e) {
      Context.movement.logError("Failed to load path planner config from GUI settings");
    }
  }

  // fieldRelative defines whether the forward direction to move at is either the robot's forward direction or the field's
  public void drive(Vector2d movementInput, Vector2d rotationInput, boolean fieldRelative) {
    movementInput = movementInput.normalized();
    rotationInput = rotationInput.normalized();

    Translation2d direction = new Translation2d(-movementInput.y, -movementInput.x).times(driveSpeed);
    double rotation = -rotationInput.x * turnSpeed;

    SwerveModuleState[] states;
    if (fieldRelative)
      states = swerveDriver.toServeModuleStates(ChassisSpeeds.fromFieldRelativeSpeeds(
        direction.getX(), direction.getY(), rotation, Rotation2d.fromRadians(rotationRadians)
      ), false);
    else
      states = swerveDriver.toServeModuleStates(new ChassisSpeeds(direction.getX(), direction.getY(), rotation), false);

    swerveDriver.setModuleStates(states, false);

    position = new Vector2d(swerveDriver.getPose().getX(), swerveDriver.getPose().getY());
    rotationRadians = swerveDriver.getPose().getRotation().getRadians();
  }

  private void resetPose() {
      swerveDriver.resetOdometry(new Pose2d(new Translation2d(position.x, position.y), new Rotation2d()));
  }

  private void resetPose(Pose2d pose) {
      swerveDriver.resetOdometry(pose);
  }

  private ChassisSpeeds getRobotRelativeSpeeds() {
    return swerveDriver.getRobotVelocity();
  }
}

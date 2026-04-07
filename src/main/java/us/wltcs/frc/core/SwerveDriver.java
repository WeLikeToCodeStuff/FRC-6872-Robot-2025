package us.wltcs.frc.core;

import com.pathplanner.lib.config.ModuleConfig;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import swervelib.SwerveController;
import swervelib.SwerveDrive;
import swervelib.SwerveModule;
import swervelib.parser.SwerveModulePhysicalCharacteristics;
import swervelib.parser.SwerveParser;
import swervelib.telemetry.SwerveDriveTelemetry;
import us.wltcs.frc.core.logging.Context;
import us.wltcs.frc.core.math.MathF;
import us.wltcs.frc.core.math.vector2.Vector2d;
import java.io.File;
import java.io.IOException;
import com.pathplanner.lib.config.RobotConfig;

import static edu.wpi.first.wpilibj.RobotBase.isSimulation;

public class SwerveDriver {
  private final float maxMotorSpeed;

  private final SwerveDrive swerveDriver;
  private final Robot robot;

  public SwerveDriver(float driveSpeed, Robot robot) {
    this.maxMotorSpeed = driveSpeed;
    this.robot = robot;

    if (isSimulation())
      SwerveDriveTelemetry.verbosity = SwerveDriveTelemetry.TelemetryVerbosity.HIGH;

    File swerveJsonDirectory = new File(Filesystem.getDeployDirectory(), "swerve/sparkmax");
    try {
      this.swerveDriver = new SwerveParser(swerveJsonDirectory).createSwerveDrive(
        this.maxMotorSpeed,
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
  }

  public void driveAutonomous(ChassisSpeeds speed) {
    SwerveModuleState[] states = swerveDriver.toServeModuleStates(
      speed,
      true
    );
    swerveDriver.setModuleStates(states, false);
  }

  // fieldRelative defines whether the forward direction to move at is either the robot's forward direction or the field's
  public void drive(Vector2d movementInput, double rotationDelta, boolean fieldRelative) {
    Translation2d direction = new Translation2d(-movementInput.y, -movementInput.x).times(maxMotorSpeed * movementInput.length());
    double rotation = -rotationDelta * maxMotorSpeed;

    if (Robot.getAlliance() == Alliance.Red)
      direction = new Translation2d(-direction.getX(), -direction.getY());

    SwerveModuleState[] states;
    if (fieldRelative)
      states = swerveDriver.toServeModuleStates(ChassisSpeeds.fromFieldRelativeSpeeds(
        direction.getX(), direction.getY(), rotation, Rotation2d.fromRadians(getRotationRadians())
      ), true);
    else
      states = swerveDriver.toServeModuleStates(new ChassisSpeeds(direction.getX(), direction.getY(), rotation), true);

    swerveDriver.setModuleStates(states, false);
  }

  public void drive(Vector2d movementInput, Vector2d rotationInput, boolean fieldRelative) {
    double turnRadians = getRotationRadians();
    if (rotationInput.length() != 0)
      turnRadians = Math.atan2(-rotationInput.y, rotationInput.x) - Math.PI / 2;

    Vector2d robotForward = new Vector2d(Math.cos(getRotationRadians()), Math.sin(getRotationRadians()));
    Vector2d robotLeft = new Vector2d(robotForward.y, -robotForward.x);
    Vector2d moveDirection = robotLeft.times(movementInput.x).plus(robotForward.times(-movementInput.y)).normalized();
    if (fieldRelative)
      moveDirection = new Vector2d(-movementInput.y, -movementInput.x);

    ChassisSpeeds desiredSpeeds = swerveDriver.swerveController.getTargetSpeeds(
      moveDirection.x, moveDirection.y,
      turnRadians,
      getRotationRadians(),
      maxMotorSpeed
    );

    Translation2d translation = SwerveController.getTranslation2d(desiredSpeeds);
    swerveDriver.drive(
      translation,
      desiredSpeeds.omegaRadiansPerSecond,
      true,
      false
    );
  }

  public void resetOdometry(Vector2d position, double rotationRadians) {
    swerveDriver.resetOdometry(new Pose2d(new Translation2d(position.x, position.y), new Rotation2d(rotationRadians)));
  }

  // Makes the wheels point towards the center of robot to prevent any movement
  public void lock() {
    swerveDriver.lockPose();
  }

  public Vector2d getPositionMeters() {
    return new Vector2d(swerveDriver.getPose().getTranslation().getX(), swerveDriver.getPose().getTranslation().getY());
  }

  public Vector2d getPositionInches() {
    return new Vector2d(
      MathF.metersToInches(swerveDriver.getPose().getTranslation().getX()),
      MathF.metersToInches(swerveDriver.getPose().getTranslation().getY())
    );
  }

  public double getRotationRadians() {
    return swerveDriver.getPose().getRotation().getRadians();
  }

  public ChassisSpeeds getRobotRelativeVelocity() {
    return swerveDriver.getRobotVelocity();
  }

  public RobotConfig getRobotConfig() {
    SwerveModule module = swerveDriver.getModules()[0];
    SwerveModulePhysicalCharacteristics physicalCharacteristics = module.configuration.physicalCharacteristics;

    double wheelGripCoefficient = physicalCharacteristics.wheelGripCoefficientOfFriction;
    double robotMassKg = physicalCharacteristics.robotMassKg;
    double wheelDiameterInches = physicalCharacteristics.conversionFactor.drive.diameter;
    double wheelRadiusInches = wheelDiameterInches / 2.0;
    double gearboxReduction = physicalCharacteristics.conversionFactor.drive.gearRatio;
    double driveCurrentLimit = physicalCharacteristics.driveMotorCurrentLimit;

    System.out.println("wheelgripcoef: " + wheelGripCoefficient);
    System.out.println("mass: " + robotMassKg);
    System.out.println("wheelDiameterIN: " + wheelDiameterInches);
    System.out.println("wheelRadiusIN: " + wheelRadiusInches);
    System.out.println("gearboxreudction: " + gearboxReduction);
    System.out.println("driveCurrentlimit: " + driveCurrentLimit);

    // Estimating MOI
    // https://pathplanner.dev/robot-config.html#calculating-a-rough-moi-estimate
    double length = MathF.inchesToMeters(28);
    double width = MathF.inchesToMeters(28);
    double robotMOI = (1.0 / 12.0) * robotMassKg * (length * length + width * width);
    System.out.println("moi: " + robotMOI);

    return
      new RobotConfig(
        robotMassKg,
        robotMOI,
        new ModuleConfig(
          MathF.inchesToMeters(wheelRadiusInches),
          maxMotorSpeed, // TODO: GET THE REAL VALUE
          wheelGripCoefficient,
          DCMotor.getNEO(1).withReduction(gearboxReduction), driveCurrentLimit,
          1
        ),
        new Translation2d[] {
          new Translation2d(0.273, 0.273),
          new Translation2d(0.273, -0.273),
          new Translation2d(-0.273, 0.273),
          new Translation2d(-0.273, -0.273)
        }
      );
  }
}

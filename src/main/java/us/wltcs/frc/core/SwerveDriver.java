package us.wltcs.frc.core;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Filesystem;
import swervelib.SwerveController;
import swervelib.SwerveDrive;
import swervelib.math.SwerveMath;
import swervelib.parser.SwerveParser;
import swervelib.telemetry.SwerveDriveTelemetry;
import us.wltcs.frc.core.logging.Context;
import us.wltcs.frc.core.math.vector2.Vector2d;

import java.io.File;
import java.io.IOException;

import static edu.wpi.first.wpilibj.RobotBase.isSimulation;

public class SwerveDriver {
  private final float maxSpeed;
  private final float turnSpeed;

  private Vector2d position;
  private double rotationRadians;

  private final SwerveDrive swerveDriver;
  private final Robot robot;

  public SwerveDriver(float maxSpeed, float turnSpeed, Vector2d position, Robot robot) {
    this.maxSpeed = maxSpeed;
    this.turnSpeed = turnSpeed;
    this.robot = robot;

    this.position = position;

    if (isSimulation())
      SwerveDriveTelemetry.verbosity = SwerveDriveTelemetry.TelemetryVerbosity.HIGH;

    File swerveJsonDirectory = new File(Filesystem.getDeployDirectory(), "swerve/sparkmax");
    try {
      this.swerveDriver = new SwerveParser(swerveJsonDirectory).createSwerveDrive(
        this.maxSpeed,
        new Pose2d(new Translation2d(position.x, position.y), new Rotation2d())
      );
      System.out.println(swerveDriver.getPose());
    } catch (IOException e) {
      Context.movement.logError("Failed to initialize swerve modules");
      throw new RuntimeException(e);
    }

//    swerveDriver.setCosineCompensator(true);
//    swerveDriver.setHeadingCorrection(false);
    swerveDriver.zeroGyro();
  }

  // fieldRelative defines whether the forward direction to move at is either the robot's forward direction or the field's
  public void drive(Vector2d movementInput, Vector2d rotationInput, boolean fieldRelative) {
    movementInput = movementInput.normalized().times(swerveDriver.getMaximumChassisVelocity());
    rotationInput = rotationInput.normalized();

    Translation2d direction = new Translation2d(-movementInput.y, -movementInput.x);
    double rotation = -rotationInput.x * swerveDriver.getMaximumChassisAngularVelocity();
    System.out.println(movementInput.toString());

    swerveDriver.drive(
      direction, rotation,
      fieldRelative,
      false
    );

    position = new Vector2d(swerveDriver.getPose().getX(), swerveDriver.getPose().getY());
    rotationRadians = swerveDriver.getPose().getRotation().getRadians();
  }
}

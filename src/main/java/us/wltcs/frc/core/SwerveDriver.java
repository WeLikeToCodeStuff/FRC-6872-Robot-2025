package us.wltcs.frc.core;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Filesystem;
import swervelib.SwerveDrive;
import swervelib.parser.SwerveParser;
import swervelib.telemetry.SwerveDriveTelemetry;
import us.wltcs.frc.core.logging.Context;
import us.wltcs.frc.core.math.vector2.Vector2d;

import java.io.File;
import java.io.IOException;

public class SwerveDriver {
  private final float driveSpeed;
  private final float turnSpeed;

  private Vector2d position;
  private double rotationRadians;

  private final SwerveDrive swerveDriver;

  public SwerveDriver(float driveSpeed, float turnSpeed, Vector2d position) {
    this.driveSpeed = driveSpeed;
    this.turnSpeed = turnSpeed;

    this.position = position;

    SwerveDriveTelemetry.verbosity = SwerveDriveTelemetry.TelemetryVerbosity.HIGH;
    File swerveJsonDirectory = new File(Filesystem.getDeployDirectory(), "swerve/sparkmax");
    try {
      this.swerveDriver = new SwerveParser(swerveJsonDirectory).createSwerveDrive(this.driveSpeed);
    } catch (IOException e) {
      Context.movement.logError("Failed to initialize swerve modules");
      throw new RuntimeException(e);
    }

    swerveDriver.setCosineCompensator(false);
    swerveDriver.setHeadingCorrection(false);
    swerveDriver.zeroGyro();

    swerveDriver.resetOdometry(new Pose2d(new Translation2d(position.x, position.y), new Rotation2d()));
  }

  // fieldRelative defines whether the forward direction to move at is either the robot's forward direction or the field's
  public void drive(Vector2d movementInput, Vector2d rotationInput, boolean fieldRelative) {
    swerveDriver.drive(
      new Translation2d(-movementInput.y * driveSpeed, -movementInput.x * driveSpeed),
      -rotationInput.x * turnSpeed,
      fieldRelative,
      false
    );

    position = new Vector2d(swerveDriver.getPose().getX(), swerveDriver.getPose().getY());
    rotationRadians = swerveDriver.getPose().getRotation().getRadians();
    System.out.println(rotationRadians);
  }
}

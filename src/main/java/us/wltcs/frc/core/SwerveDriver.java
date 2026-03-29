package us.wltcs.frc.core;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.networktables.GenericPublisher;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Filesystem;
import swervelib.SwerveController;
import swervelib.SwerveDrive;
import swervelib.math.SwerveMath;
import swervelib.parser.SwerveParser;
import swervelib.telemetry.SwerveDriveTelemetry;
import us.wltcs.frc.core.devices.input.Gyroscope;
import us.wltcs.frc.core.devices.output.SwerveModule;
import us.wltcs.frc.core.math.vector2.Vector2d;
import us.wltcs.frc.core.ui.Dashboard;
import us.wltcs.frc.robot.SwerveModules;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SwerveDriver {
//  private final Gyroscope gyroscope = new Gyroscope();

  // In meters
  private final float maxDriveSpeed;

  private final File swerveJsonDirectory;
  private final SwerveDrive swerveDriver;

  private final Dashboard dashboard;

  public SwerveDriver(float driveSpeed, Dashboard dashboard) {
    this.dashboard = dashboard;
    this.maxDriveSpeed = driveSpeed;

    SwerveDriveTelemetry.verbosity = SwerveDriveTelemetry.TelemetryVerbosity.HIGH;
    this.swerveJsonDirectory = new File(Filesystem.getDeployDirectory(), "swerve/sparkmax");
    try {
      this.swerveDriver = new SwerveParser(swerveJsonDirectory).createSwerveDrive(maxDriveSpeed);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    swerveDriver.setCosineCompensator(false);
    swerveDriver.setHeadingCorrection(false);
    swerveDriver.zeroGyro();
  }

  public void drive(Vector2d movementInput, Vector2d rotationInput, boolean fieldRelative) {
    double robotRadians = swerveDriver.getOdometryHeading().getRadians();

    double turnRadians = robotRadians;
    if (movementInput.length() != 0)
      turnRadians = Math.atan2(-rotationInput.y, rotationInput.x) - Math.PI / 2;


    ChassisSpeeds desiredSpeeds = swerveDriver.swerveController.getTargetSpeeds(
      movementInput.x, movementInput.y,
      turnRadians, robotRadians,
      maxDriveSpeed
    );


    swerveDriver.drive(new Translation2d(movementInput.x, movementInput.y), robotRadians, true, false);
  }
}

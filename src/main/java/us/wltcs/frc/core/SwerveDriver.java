package us.wltcs.frc.core;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Filesystem;
import swervelib.SwerveController;
import swervelib.SwerveDrive;
import swervelib.parser.SwerveParser;
import swervelib.telemetry.SwerveDriveTelemetry;
import us.wltcs.frc.core.math.vector2.Vector2d;
import us.wltcs.frc.core.ui.Dashboard;

import java.io.File;
import java.io.IOException;

public class SwerveDriver {
  private final float driveSpeed;

  private final float turnSpeed;

  private final File swerveJsonDirectory;
  private final SwerveDrive swerveDriver;

  private final Dashboard dashboard;

  public SwerveDriver(float driveSpeed, float turnSpeed, Dashboard dashboard) {
    this.dashboard = dashboard;
    this.driveSpeed = driveSpeed;
    this.turnSpeed = turnSpeed;

    SwerveDriveTelemetry.verbosity = SwerveDriveTelemetry.TelemetryVerbosity.HIGH;
    this.swerveJsonDirectory = new File(Filesystem.getDeployDirectory(), "swerve/sparkmax");
    try {
      this.swerveDriver = new SwerveParser(swerveJsonDirectory).createSwerveDrive(this.driveSpeed);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    swerveDriver.setCosineCompensator(false);
    swerveDriver.setHeadingCorrection(false);
    swerveDriver.zeroGyro();
  }

  public void drive(Vector2d movementInput, Vector2d rotationInput, boolean fieldRelative) {
    ChassisSpeeds desiredSpeeds = new ChassisSpeeds(
      movementInput.x * driveSpeed,
      movementInput.y * turnSpeed,
      rotationInput.x * driveSpeed
    );

    swerveDriver.drive(
      SwerveController.getTranslation2d(desiredSpeeds),
      desiredSpeeds.omegaRadiansPerSecond,
      fieldRelative,
      false
    );
  }
}

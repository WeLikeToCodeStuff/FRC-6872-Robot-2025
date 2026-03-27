package us.wltcs.frc.core;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
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
  // private final Gyroscope gyroscope = new Gyroscope();

  private SwerveDriveKinematics kinematics;

  // private final SwerveModule frontLeftModule;
  // private final SwerveModule frontRightModule;
  // private final SwerveModule rearLeftModule;
  // private final SwerveModule rearRightModule;

  // In meters
  private final float maxDriveSpeed;

  private final File swerveJsonDirectory;
  private final SwerveDrive swerveDriver;

  private final Dashboard dashboard;

  public double getControllerOutput() {
    return 0;
    // return frontLeftModule.getOutput();
  }

  public SwerveDriver(
          // SwerveModule frontLeft,
          // SwerveModule frontRight,
          // SwerveModule rearLeft,
          // SwerveModule rearRight,
          float driveSpeed,
          Dashboard dashboard
  ) {
    this.dashboard = dashboard;
    this.maxDriveSpeed = driveSpeed;

    SwerveDriveTelemetry.verbosity = SwerveDriveTelemetry.TelemetryVerbosity.HIGH;

    this.swerveJsonDirectory = new File(Filesystem.getDeployDirectory(), "swerve/sparkmax");
    try {
      this.swerveDriver = new SwerveParser(swerveJsonDirectory).createSwerveDrive(maxDriveSpeed);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // if (Robot.isSimulation() && (frontLeft == null || frontRight == null || rearLeft == null || rearRight == null)) {
    //   // In simulation, we can create dummy modules if any of them are null to avoid null pointer exceptions.
    //   this.frontLeftModule = new SwerveModule(0, 0, new Vector2d(-SwerveModules.chassisWidth / 2, SwerveModules.chassisLength / 2), 0, false);
    //   this.frontRightModule = new SwerveModule(0, 0, new Vector2d(SwerveModules.chassisWidth / 2, SwerveModules.chassisLength / 2), 0, false);
    //   this.rearLeftModule = new SwerveModule(0, 0, new Vector2d(-SwerveModules.chassisWidth / 2, -SwerveModules.chassisLength / 2), 0, false);
    //   this.rearRightModule = new SwerveModule(0, 0, new Vector2d(SwerveModules.chassisWidth / 2, -SwerveModules.chassisLength / 2), 0, false);
    // } else {
    //   this.frontLeftModule = frontLeft;
    //   this.frontRightModule = frontRight;
    //   this.rearLeftModule = rearLeft;
    //   this.rearRightModule = rearRight;
    // }
    // kinematics = new SwerveDriveKinematics(
    //   new Translation2d(frontLeft.getPosition().x, frontLeft.getPosition().y),
    //   new Translation2d(frontRight.getPosition().x, frontRight.getPosition().y),
    //   new Translation2d(rearLeft.getPosition().x, rearLeft.getPosition().y),
    //   new Translation2d(rearRight.getPosition().x, rearRight.getPosition().y)
    // );
  }

  public void drive(Vector2d moveDirection, Vector2d turnDirection, boolean fieldRelative) {
    if (moveDirection.length() == 0 && turnDirection.length() == 0) {
      stop();
      return;
    }

    double heading = Math.atan2(moveDirection.getX(), -moveDirection.getY());
    ChassisSpeeds desiredSpeeds = swerveDriver.swerveController.getTargetSpeeds(
      -moveDirection.getY(),
      moveDirection.getX(),
      heading * Math.PI,
      swerveDriver.getOdometryHeading().getRadians(),
      maxDriveSpeed
    );

    Translation2d translation = SwerveController.getTranslation2d(desiredSpeeds);
    swerveDriver.drive(
      translation,
      desiredSpeeds.omegaRadiansPerSecond,
      true,
      false
    );
  }

  public void stop() {
    swerveDriver.drive(new Translation2d(0, 0), 0, true, false);
    // frontLeftModule.stop();
    // frontRightModule.stop();
    // rearLeftModule.stop();
    // rearRightModule.stop();
  }
}

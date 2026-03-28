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
//  private final Gyroscope gyroscope = new Gyroscope();

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
          // SwerveModule rearRight,x
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
  }

  public void drive(Vector2d inputMovementDirection, Vector2d inputTurnDirection, boolean fieldRelative) {
    if (inputMovementDirection.length() == 0 && inputTurnDirection.length() == 0) {
      stop();
      return;
    }

    double robotRadians = swerveDriver.getOdometryHeading().getRadians();

    double turnRadians = robotRadians;
    if (inputTurnDirection.length() != 0)
      turnRadians = Math.atan2(-inputTurnDirection.y, inputTurnDirection.x) - Math.PI / 2;

    Vector2d robotForward = new Vector2d(Math.cos(robotRadians), Math.sin(robotRadians));
    Vector2d robotLeft = new Vector2d(robotForward.y, -robotForward.x);
    Vector2d moveDirection = robotLeft.times(inputMovementDirection.x).plus(robotForward.times(-inputMovementDirection.y)).normalized();

    ChassisSpeeds desiredSpeeds = swerveDriver.swerveController.getTargetSpeeds(
      moveDirection.x, moveDirection.y,
      turnRadians,
      robotRadians,
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
  }
}

package us.wltcs.frc.core;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import us.wltcs.frc.core.devices.input.Gyroscope;
import us.wltcs.frc.core.devices.output.SwerveModule;

public class SwerveDriver {
  private final Gyroscope gyroscope = new Gyroscope();

  private final SwerveDriveKinematics kinematics;

  private final SwerveModule frontLeftModule;
  private final SwerveModule frontRightModule;
  private final SwerveModule rearLeftModule;
  private final SwerveModule rearRightModule;

  public SwerveDriver(SwerveModule frontLeft, SwerveModule frontRight, SwerveModule rearLeft, SwerveModule rearRight) {
    frontLeftModule = frontLeft;
    frontRightModule = frontRight;
    rearLeftModule = rearLeft;
    rearRightModule = rearRight;

    kinematics = new SwerveDriveKinematics(
      new Translation2d(frontLeft.getPosition().x, frontLeft.getPosition().y),
      new Translation2d(frontRight.getPosition().x, frontRight.getPosition().y),
      new Translation2d(rearLeft.getPosition().x, rearLeft.getPosition().y),
      new Translation2d(rearRight.getPosition().x, rearRight.getPosition().y)
    );
  }

  private static final double kMaxSpeedMetersPerSecond = 4.8;
  private static final double kMaxAngularSpeed = 2 * Math.PI; // radians per second
  public void drive(double xSpeed, double ySpeed, double rot, boolean fieldRelative) {
    // Convert the commanded speeds into the correct units for the drivetrain
    double xSpeedDelivered = xSpeed * kMaxSpeedMetersPerSecond;
    double ySpeedDelivered = ySpeed * kMaxSpeedMetersPerSecond;
    double rotDelivered = rot * kMaxAngularSpeed;

    var swerveModuleStates = kinematics.toSwerveModuleStates(
            fieldRelative
                    ? ChassisSpeeds.fromFieldRelativeSpeeds(xSpeedDelivered, ySpeedDelivered, rotDelivered,
                    Rotation2d.fromDegrees(gyroscope.getDegrees()))
                    : new ChassisSpeeds(xSpeedDelivered, ySpeedDelivered, rotDelivered));
    SwerveDriveKinematics.desaturateWheelSpeeds(
            swerveModuleStates, kMaxSpeedMetersPerSecond);
    frontLeftModule.setState(swerveModuleStates[0]);
    frontRightModule.setState(swerveModuleStates[1]);
    rearLeftModule.setState(swerveModuleStates[2]);
    rearRightModule.setState(swerveModuleStates[3]);
  }
}

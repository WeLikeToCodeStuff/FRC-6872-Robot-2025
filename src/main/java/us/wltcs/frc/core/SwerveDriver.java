package us.wltcs.frc.core;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import us.wltcs.frc.core.devices.output.SwerveModule;

public class SwerveDriver {
  private final SwerveDriveKinematics kinematics;

  public SwerveDriver(SwerveModule frontLeft, SwerveModule frontRight, SwerveModule rearLeft, SwerveModule rearRight) {
    kinematics = new SwerveDriveKinematics(
//      new Translation2d(frontLeft.getLocation().x, frontLeft.getLocation().y),
//      new Translation2d(frontRight.getLocation().x, frontRight.getLocation().y),
//      new Translation2d(rearLeft.getLocation().x, rearLeft.getLocation().y),
//      new Translation2d(rearRight.getLocation().x, rearRight.getLocation().y)
    );
  }
}

package us.wltcs.frc.core.swerves;

import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import us.wltcs.frc.core.devices.input.gyroscope.Gyroscope;
import us.wltcs.frc.robot.Config;
import us.wltcs.frc.core.math.vector2.Vector2d;

public class SwerveDriver {
  public final SwerveDriveKinematics kinematics;
  public final Config config;
//  private final SwerveModule[] swerveModules;

  public final Gyroscope gyroscope;

  private Vector2d position;

  public SwerveDriver(Config config, Vector2d position) {
    this.config = config;
    this.position = position;
    this.gyroscope = new Gyroscope(config.gyroscope);
    kinematics = new SwerveDriveKinematics();


  }

  public void drive(Vector2d direction) {

  }
}

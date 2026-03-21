package us.wltcs.frc.robot;
import us.wltcs.frc.core.devices.output.SwerveModule;
import us.wltcs.frc.core.math.MathF;
import us.wltcs.frc.core.math.vector2.Vector2d;

public class SwerveModules {
  // Naming convention: forward/rear left/right motor
  // Positions are relative to the center of the robot.

  // Units are in meters
  public static final double chassisWidth = MathF.inchesToMeters(27);
  public static final double chassisLength = MathF.inchesToMeters(27);

  public static final SwerveModule frontLeftMotorController = new SwerveModule(
    1,
    2,
    0,
    new Vector2d(-chassisWidth / 2, chassisLength / 2)
  );

  public static final SwerveModule frontRightMotorController = new SwerveModule(
    7,
    8,
    3,
    new Vector2d(chassisWidth / 2, chassisLength / 2)
  );

  public static final SwerveModule rearLeftMotorController = new SwerveModule(
    3,
          4,
    1,
    new Vector2d(-chassisWidth / 2, -chassisLength / 2)
  );

  public static final SwerveModule rearRightMotorController = new SwerveModule(
    5,
          6,
    2,
    new Vector2d(chassisWidth / 2, -chassisLength / 2)
  );
}

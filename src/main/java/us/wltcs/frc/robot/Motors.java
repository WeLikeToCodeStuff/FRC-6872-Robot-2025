package us.wltcs.frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import us.wltcs.frc.core.devices.output.Motor;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;

public class Motors {
  public static final Translation2d m_frontLeftLocation = new Translation2d(0.381, 0.381);
  public static final Translation2d m_frontRightLocation = new Translation2d(0.381, -0.381);
  public static final Translation2d m_backLeftLocation = new Translation2d(-0.381, 0.381);
  public static final Translation2d m_backRightLocation = new Translation2d(-0.381, -0.381);
  // Naming convention: forward/rear left/right motor
  public static final Motor frontLeftMotor = new Motor(new PWMSparkMax(1));
  public static final Motor frontRightMotorController = new Motor(new PWMSparkMax(2));
  public static final Motor rearLeftMotorController = new Motor(new PWMSparkMax(3));
  public static final Motor rearRightMotorController = new Motor(new PWMSparkMax(4));
}

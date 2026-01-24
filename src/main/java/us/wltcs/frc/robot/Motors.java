package us.wltcs.frc.robot;

import us.wltcs.frc.core.devices.output.Motor;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;

public class Motors {
  // Naming convention: forward/rear left/right motor
  public static final Motor frontLeftMotor = new Motor(new PWMSparkMax(1));
  public static final Motor frontRightMotorController = new Motor(new PWMSparkMax(2));
  public static final Motor rearLeftMotorController = new Motor(new PWMSparkMax(3));
  public static final Motor rearRightMotorController = new Motor(new PWMSparkMax(4));
}

package us.wltcs.frc.robot;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MotorConstants {
  // naming convention: forward/rear left/right motor
  public static final MotorController frontLeftMotorController = new PWMSparkMax(0);
  public static final MotorController frontRightMotorController = new PWMSparkMax(1);
  public static final MotorController rearLeftMotorController = new PWMSparkMax(2);
  public static final MotorController rearRightMotorController = new PWMSparkMax(3);
}

package us.wltcs.frc.robot;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.motorcontrol.PWMTalonSRX;
import edu.wpi.first.wpilibj.motorcontrol.PWMVenom;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj.motorcontrol.VictorSP;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MotorConstants {
  // naming convention: forward/rear left/right motor
  public static final PWMTalonSRX frontLeftMotorController = new PWMTalonSRX(1);
  public static final PWMTalonSRX frontRightMotorController = new PWMTalonSRX(2);
  public static final PWMTalonSRX rearLeftMotorController = new PWMTalonSRX(3);
  public static final VictorSP rearRightMotorController = new VictorSP(4);
  public static final VictorSP leftArmMotorController = new VictorSP(5);
  public static final PWMTalonSRX bottomIntakeMotorController = new PWMTalonSRX(6);
  public static final VictorSP rightArmMotorController = new VictorSP(7);
  public static final VictorSP topIntakeMotorController = new VictorSP(8);
}

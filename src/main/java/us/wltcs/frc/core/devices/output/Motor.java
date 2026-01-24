package us.wltcs.frc.core.devices.output;

import lombok.Getter;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public class Motor{
  @Getter
  private final MotorController motorController;

  public Motor(MotorController controller) {
    motorController = controller;
  }
}

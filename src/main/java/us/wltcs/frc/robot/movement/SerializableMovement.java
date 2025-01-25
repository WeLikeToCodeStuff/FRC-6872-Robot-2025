package us.wltcs.frc.robot.movement;

import edu.wpi.first.wpilibj.Joystick;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

import us.wltcs.frc.robot.utils.input.JoystickUtils;

@AllArgsConstructor
@Data
public class SerializableMovement {
  private final double x, y, z;
  private final Map<Integer, Boolean> buttons;
  private final double time;

  public SerializableMovement(Joystick joystick, double time) {
    this.x = joystick.getX();
    this.y = joystick.getZ();
    this.z = joystick.getZ();
    this.time = time;
    this.buttons = JoystickUtils.getJoystickButtons(joystick);
  }
}

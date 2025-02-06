package us.wltcs.frc.robot.movement;

import edu.wpi.first.wpilibj.Joystick;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

import us.wltcs.frc.robot.util.input.JoystickUtils;

@AllArgsConstructor
@Data
public class SerializableMovement {
  private final double x, y, z;
  private final Map<Integer, Boolean> buttons;
  private final long time;

  public SerializableMovement(final Joystick joystick, final long time) {
    this.x = joystick.getX();
    this.y = joystick.getZ();
    this.z = joystick.getZ();
    this.time = time;
    this.buttons = JoystickUtils.getJoystickButtons(joystick);
  }
}

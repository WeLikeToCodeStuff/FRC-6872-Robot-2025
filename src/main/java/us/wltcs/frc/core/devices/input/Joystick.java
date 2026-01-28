package us.wltcs.frc.core.devices.input;
import java.util.Map;

import lombok.Getter;
import us.wltcs.frc.core.logging.Context;
import us.wltcs.frc.core.math.MathF;
import us.wltcs.frc.core.math.vector2.Vector2d;

// Class for the Logitech extreme 3d pro joystick
public class Joystick {
  private Map<Integer, Boolean> buttons;
  @Getter
  private final edu.wpi.first.wpilibj.Joystick joystick;

  public Joystick(int port) {
    this.joystick = new edu.wpi.first.wpilibj.Joystick(port);
    for (int i = 1; i <= joystick.getButtonCount(); i++) {
      try {
        buttons.put(i, joystick.getRawButton(i));
      } catch (Exception e) {
        Context.movement.logError("Invalid button ID %s", i);
      }
    }
  }

  public Vector2d getDirection() {
    Vector2d vector = new Vector2d(MathF.round((float) joystick.getRawAxis(1),5), MathF.round((float) joystick.getRawAxis(2), 5));
    if (vector.length() >= 0.1)
      return vector.normalized();

    return new Vector2d(0, 0);
  }

  public double getSlider() {
    return joystick.getThrottle();
  }

  public boolean buttonPressed(int button) {
    if (button > buttons.size()) {
      Context.movement.logError("Attempted to get an out of range button id of  %s", button);
      return false;
    }

    return buttons.get(button);
  }

}

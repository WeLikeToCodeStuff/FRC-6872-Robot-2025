package us.wltcs.frc.core.devices.input;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import us.wltcs.frc.core.logging.Context;
import us.wltcs.frc.core.math.MathF;
import us.wltcs.frc.core.math.vector2.Vector2d;

// Class for the Logitech extreme 3d pro joystick
public class Joystick {
  @Getter
  private final edu.wpi.first.wpilibj.Joystick joystick;

  public Joystick(int port) {
    this.joystick = new edu.wpi.first.wpilibj.Joystick(port);
  }

  public Vector2d getDirection() {
    Vector2d vector = new Vector2d(MathF.round((float) joystick.getRawAxis(1),5), MathF.round((float) joystick.getRawAxis(2), 5));
    if (vector.length() <= 0.1)
      return new Vector2d(0, 0);

    return vector.normalized();
  }

  public double getSlider() {
    return joystick.getThrottle();
  }

  public boolean buttonPressed(int button) {
    if (button > this.joystick.getButtonCount()) {
      Context.movement.logError("Attempted to get an out of range button id of %s", button);
      return false;
    }

    return joystick.getRawButton(button);
  }
}

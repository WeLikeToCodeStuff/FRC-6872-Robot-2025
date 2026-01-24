package us.wltcs.frc.core.devices;
import java.util.Map;
import us.wltcs.frc.core.logging.Context;
import us.wltcs.frc.core.math.vector3.Vector3d;

// Class for the Logitech extreme 3d pro joystick
public class Joystick {
  private Map<Integer, Boolean> buttons;
  private final edu.wpi.first.wpilibj.Joystick joystick;

  public Joystick(int port) {
    joystick = new edu.wpi.first.wpilibj.Joystick(port);
    for (int i = 1; i <= joystick.getButtonCount(); i++) {
      try {
        buttons.put(i, joystick.getRawButton(i));
      } catch (Exception e) {
        Context.movement.logError("Invalid button ID %s", i);
      }
    }
  }

  public Vector3d getDirection() {
    return new Vector3d(joystick.getRawAxis(0), joystick.getRawAxis(1), joystick.getRawAxis(2));
  }

  public boolean buttonPressed(int button) {
    if (button > buttons.size()) {
      Context.movement.logError("Attempted to get an out of range button id of  %s", button);
      return false;
    }

    return buttons.get(button);
  }
}

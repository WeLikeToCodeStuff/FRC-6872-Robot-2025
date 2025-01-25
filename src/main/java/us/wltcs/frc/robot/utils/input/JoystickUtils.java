package us.wltcs.frc.robot.utils.input;

import edu.wpi.first.wpilibj.Joystick;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class JoystickUtils {
  public boolean isJoystickMoving(Joystick joystick, double threshold) {
    return joystick.getMagnitude() > threshold;
  }

  public Map<Integer, Boolean> getJoystickButtons(Joystick joystick) {
    final Map<Integer, Boolean> buttons = new HashMap<>();
    for (int i = 1; i <= joystick.getButtonCount(); i++) {
      try {
        buttons.put(i, joystick.getRawButton(i));
      } catch (Exception e) {
        e.fillInStackTrace();
      }
    }

    return buttons;
  }

  public boolean isJoystickUsed(Joystick joystick, double threshold) {
    return !getJoystickButtons(joystick).isEmpty() && isJoystickMoving(joystick, threshold);
  }
}

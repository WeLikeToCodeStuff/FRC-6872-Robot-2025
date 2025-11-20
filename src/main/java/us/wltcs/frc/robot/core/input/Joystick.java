package us.wltcs.frc.robot.core.input;

import java.util.Map;

import us.wltcs.frc.robot.core.logging.Logs;
import us.wltcs.frc.robot.core.logging.LogType;

public class Joystick {
  private Map<Integer, Boolean> buttons;
  private final edu.wpi.first.wpilibj.Joystick joystick;

  public Joystick(int port) {
    joystick = new edu.wpi.first.wpilibj.Joystick(port);
    for (int i = 1; i <= joystick.getButtonCount(); i++) {
      try {
        buttons.put(i, joystick.getRawButton(i));
      } catch (Exception e) {
        Logs.movement.log(LogType.ERROR, String.format("Invalid button ID %s", i));
      }
    }
  }

  public boolean buttonPressed(int button) {
    if (button > buttons.size()) {
      Logs.program.log(LogType.WARNING, "Attempted to get an out of range button id of " + button);
      return false;
    }

    return buttons.get(button);
  }
}

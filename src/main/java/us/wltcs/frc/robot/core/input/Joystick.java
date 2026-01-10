package us.wltcs.frc.robot.core.input;

import java.util.Map;

import us.wltcs.frc.robot.core.logging.Context;
import us.wltcs.frc.robot.core.logging.Levels;

public class Joystick {
  private Map<Integer, Boolean> buttons;
  private final edu.wpi.first.wpilibj.Joystick joystick;

  public Joystick(int port) {
    joystick = new edu.wpi.first.wpilibj.Joystick(port);
    for (int i = 1; i <= joystick.getButtonCount(); i++) {
      try {
        buttons.put(i, joystick.getRawButton(i));
      } catch (Exception e) {
        Context.movement.log(Levels.ERROR, String.format("Invalid button ID %s", i));
      }
    }
  }

  public boolean buttonPressed(int button) {
    if (button > buttons.size()) {
      Context.program.log(Levels.WARNING, "Attempted to get an out of range button id of " + button);
      return false;
    }

    return buttons.get(button);
  }
}

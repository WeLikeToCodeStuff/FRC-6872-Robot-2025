package us.wltcs.frc.core.devices.input;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import lombok.Getter;
import us.wltcs.frc.core.logging.Context;
import us.wltcs.frc.core.math.MathF;
import us.wltcs.frc.core.math.vector2.Vector2d;

// Class for the Xbox 360, Xbox One, or Logitech F310 controllers
public class Controller {
  @Getter
  private final XboxController controller;

  public Controller(int port) {
    this.controller = new XboxController(port);
  }

  public boolean buttonPressed(int button) {
    if (button > this.controller.getButtonCount()) {
      Context.movement.logError("Attempted to get an out of range button id of %s", button);
      return false;
    }

    return controller.getRawButton(button);
  }

  // Vector direction of left joystick
  public Vector2d getLeftDirection() {
    Vector2d vector = new Vector2d(
      MathF.round((float) controller.getRawAxis(0),5),
      MathF.round((float) controller.getRawAxis(1), 5)
    );

    if (vector.length() <= 0.1)
      return new Vector2d(0, 0);

    return vector;
  }

  // Vector direction of right joystick
  public Vector2d getRightDirection() {
    Vector2d vector;
    try {
      vector = new Vector2d(
        MathF.round((float) controller.getRawAxis(4), 5),
        MathF.round((float) controller.getRawAxis(5), 5)
      );
    } catch (Exception e) {
      Context.movement.logError("Attempted to get right joystick direction, but controller does not have right joystick axes");
      return new Vector2d(0, 0);
    }

    if (vector.length() <= 0.1)
      return new Vector2d(0, 0);

    return vector;
  }
}

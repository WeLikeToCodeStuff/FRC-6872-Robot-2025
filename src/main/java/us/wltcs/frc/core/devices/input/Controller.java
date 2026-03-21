package us.wltcs.frc.core.devices.input;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.wpilibj.XboxController;
import lombok.Getter;
import us.wltcs.frc.core.logging.Context;
import us.wltcs.frc.core.math.MathF;
import us.wltcs.frc.core.math.vector2.Vector2d;

// Class for the Logitech F310 controller
public class Controller {
  private final Map<Integer, Boolean> buttons = new HashMap<>();

  @Getter
  private final XboxController controller;

  public Controller(int port) {
    this.controller = new XboxController(port);
  }

  public void init() {
    System.out.print(getLeftAxisDirection());
    for (int i = 1; i <= this.controller.getButtonCount(); i++) {
      try {
        buttons.put(i, controller.getRawButton(i));
      } catch (Exception e) {
        Context.movement.logError("Invalid button ID %s", i);
      }
    }
  }

  public Vector2d getLeftAxisDirection() {
    Vector2d vector = new Vector2d(MathF.round((float) controller.getRawAxis(0),5), MathF.round((float) controller.getRawAxis(1), 5));
    if (vector.length() >= 0.1)
      return vector.normalized();

    return new Vector2d(0, 0); 
  }
}

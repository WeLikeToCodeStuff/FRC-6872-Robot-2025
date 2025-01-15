package us.wltcs.frc.robot.movement;

import edu.wpi.first.math.Pair;
import edu.wpi.first.wpilibj.Joystick;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Locked;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Data
public class SerializableMovement {
    private final double x, y, z;
    private final Map<Integer, Boolean> buttons = new HashMap<>();  

    public SerializableMovement(Joystick joystick) {
        this.x = joystick.getX();
        this.y = joystick.getZ();
        this.z = joystick.getZ();
        for (int i = 1; i <= joystick.getButtonCount(); i++) {
            try {
                this.buttons.put(i, joystick.getRawButton(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

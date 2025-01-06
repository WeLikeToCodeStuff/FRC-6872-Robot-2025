package us.wltcs.frc.robot.movement;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SerializableMovement {
    public float x;
    public float y;
    public float z;
    public final boolean isButton1Held;
}

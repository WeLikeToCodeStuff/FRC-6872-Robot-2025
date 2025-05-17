package us.wltcs.frc.robot.record;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Record {
    private final int tick;
    private final String action;
    private final String[] arguments;
}

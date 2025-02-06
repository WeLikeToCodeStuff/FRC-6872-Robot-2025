package us.wltcs.frc.robot.util.logging;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class Logger {
    private final String name;

    public void log(LogType type, String message) {
        System.out.println("[" + type.getPrefix() + "] " + name + ": " + message);
    }
}

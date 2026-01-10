package us.wltcs.frc.robot.core.logging;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class Logger {
  private final String name;

  public void log(Levels type, String message) {
    System.out.println("[" + type.getPrefix() + "] " + name + ": " + message);
  }
}
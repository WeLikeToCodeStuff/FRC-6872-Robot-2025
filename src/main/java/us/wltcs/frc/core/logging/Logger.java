package us.wltcs.frc.core.logging;

import java.util.Calendar;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class Logger {
  private final String name;
  private final String escapeSequence = "\u001B[";

  public void log(Levels type, String format, Object... args) {
    Calendar calendar = Calendar.getInstance();
    String formatted = String.format(format, args);
    String time = String.format("%d:%d:%d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
    String log = String.format("[%s] [%s] [%s] ", time, type.getPrefix(), name);
    System.out.println(log + formatted);
  }

  public void logInfo(String format, Object... args) {
    log(Levels.INFO, format, args);
  }

  public void logWarning(String format, Object... args) {
    log(Levels.WARNING, format, args);
  }

  public void logError(String format, Object... args) {
    log(Levels.ERROR, format, args);
  }

  public void logDebug(String format, Object... args) {
    log(Levels.DEBUG, format, args);
  }
}
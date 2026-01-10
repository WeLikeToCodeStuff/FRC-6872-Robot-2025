package us.wltcs.frc.core.logging;

import lombok.Getter;

@Getter()
public enum AnsiColor {
  BLACK,
  RED,
  GREEN,
  YELLOW,
  BLUE,
  MAGENTA,
  CYAN,
  WHITE;

  private final int code = ordinal();
}
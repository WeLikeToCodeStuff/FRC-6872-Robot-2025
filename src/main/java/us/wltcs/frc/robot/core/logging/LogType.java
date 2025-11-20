package us.wltcs.frc.robot.core.logging;

import lombok.Getter;

@Getter()
public enum LogType {
  INFO("INFO"),
  WARNING("WARNING"),
  ERROR("ERROR"),
  DEBUG("DEBUG");

  private final String prefix;
  private final int level = ordinal();

  LogType(final String prefix) {
    this.prefix = prefix;
  }
}
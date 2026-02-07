package us.wltcs.frc.core.logging;

import lombok.Getter;

@Getter()
public enum Levels {
  INFO("INFO"),
  WARNING("WARNING"),
  ERROR("ERROR"),
  DEBUG("DEBUG");

  private final String prefix;
  private final int level = ordinal();

  Levels(final String prefix) {
    this.prefix = prefix;
  }
}
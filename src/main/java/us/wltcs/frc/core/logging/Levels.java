package us.wltcs.frc.core.logging;

import lombok.Getter;

@Getter()
public enum Levels {
  INFO("INFO", new Style(AnsiColor.GREEN, AnsiBrightness.STANDARD, AnsiColor.GREEN, AnsiBrightness.BRIGHT)),
  WARNING("WARNING", new Style(AnsiColor.YELLOW, AnsiBrightness.STANDARD, AnsiColor.YELLOW, AnsiBrightness.BRIGHT)),
  ERROR("ERROR", new Style(AnsiColor.RED, AnsiBrightness.STANDARD, AnsiColor.RED, AnsiBrightness.BRIGHT)),
  DEBUG("DEBUG", new Style(AnsiColor.RED, AnsiBrightness.STANDARD, AnsiColor.RED, AnsiBrightness.BRIGHT));

  private final Style style;
  private final String prefix;
  private final int level = ordinal();

  Levels(final String prefix, final Style style) {
    this.prefix = prefix;
    this.style = style;
  }
}
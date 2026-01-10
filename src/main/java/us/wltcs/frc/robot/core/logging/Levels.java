package us.wltcs.frc.robot.core.logging;

import us.wltcs.frc.robot.core.logging.Style;
import lombok.Getter;

@Getter()
public enum Levels {
  INFO("INFO", new Style(AnsiColor.GREEN, AnsiBrightness.STANDARD, AnsiColor.GREEN, AnsiBrightness.BRIGHT)),
  WARNING("WARNING", new Style(AnsiColor.GREEN, AnsiBrightness.STANDARD, AnsiColor.GREEN, AnsiBrightness.BRIGHT)),
  ERROR("ERROR", new Style(AnsiColor.GREEN, AnsiBrightness.STANDARD, AnsiColor.GREEN, AnsiBrightness.BRIGHT)),
  DEBUG("DEBUG", new Style(AnsiColor.GREEN, AnsiBrightness.STANDARD, AnsiColor.GREEN, AnsiBrightness.BRIGHT));

  private final String prefix;
  private final Style style;
  private final int level = ordinal();

  Levels(final String prefix, Style style) {
    this.prefix = prefix;
    this.style = style;
  }
}
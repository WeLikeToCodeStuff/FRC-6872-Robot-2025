package us.wltcs.frc.core.logging;

import lombok.Getter;

@Getter
public class Style {
  private final AnsiColor messageColor;
  private final AnsiBrightness messageBrightness;
  private final AnsiColor logColor;
  private final AnsiBrightness logBrightness;

  Style(AnsiColor messageColor, AnsiBrightness messageBrightness, AnsiColor logColor, AnsiBrightness logBrightness) {
    this.messageColor = messageColor;
    this.messageBrightness = messageBrightness;
    this.logColor = logColor;
    this.logBrightness = logBrightness;
  }
}

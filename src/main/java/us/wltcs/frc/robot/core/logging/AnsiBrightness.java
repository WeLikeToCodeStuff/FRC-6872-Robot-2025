package us.wltcs.frc.robot.core.logging;

import lombok.Getter;

@Getter()
public enum AnsiBrightness {
  STANDARD(9),
  BRIGHT(3);

  private final int code;

  AnsiBrightness(final int code) {
    this.code = code;
  }
}

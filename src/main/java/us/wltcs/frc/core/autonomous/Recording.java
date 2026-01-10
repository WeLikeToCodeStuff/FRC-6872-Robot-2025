package us.wltcs.frc.core.autonomous;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Recording {
  private final int tick;
  private final String action;
  private final String[] arguments;
}
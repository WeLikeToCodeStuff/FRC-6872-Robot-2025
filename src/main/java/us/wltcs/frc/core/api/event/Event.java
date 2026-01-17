package us.wltcs.frc.core.api.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class Event {
  private final EventType eventType;
}

package us.wltcs.frc.core.impl.events;

import us.wltcs.frc.core.api.event.Event;
import us.wltcs.frc.core.api.event.EventType;

public class RobotStartEvent extends Event {
  public RobotStartEvent(EventType eventType) {
    super(eventType);
  }
}

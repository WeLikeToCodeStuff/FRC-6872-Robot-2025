package us.wltcs.frc.robot.events;

import us.wltcs.frc.core.api.event.Event;
import us.wltcs.frc.core.api.event.EventType;

public class RobotStart extends Event {
  public RobotStart(EventType type) {
    super(type);
  }
}

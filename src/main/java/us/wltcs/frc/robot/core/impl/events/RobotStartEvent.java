package us.wltcs.frc.robot.core.impl.events;

import us.wltcs.frc.robot.core.api.event.Event;
import us.wltcs.frc.robot.core.api.event.EventType;

public class RobotStartEvent extends Event {

    public RobotStartEvent(EventType eventType) {
        super(eventType);
    }
}

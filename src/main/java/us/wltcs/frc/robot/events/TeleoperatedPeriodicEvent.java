package us.wltcs.frc.robot.events;

import lombok.Getter;
import us.wltcs.frc.core.Robot;
import us.wltcs.frc.core.api.event.Event;
import us.wltcs.frc.core.api.event.EventType;

public class TeleoperatedPeriodicEvent extends Event {
    @Getter private final Robot robot;
    public TeleoperatedPeriodicEvent(EventType eventType, Robot robot) {
        super(eventType);
        this.robot = robot;
    }
}

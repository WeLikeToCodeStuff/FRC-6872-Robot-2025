package us.wltcs.frc.core.api.event;

@FunctionalInterface
public interface EventListener<Event>
{
    void call(final Event event);
}

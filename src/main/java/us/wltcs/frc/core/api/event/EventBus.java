package us.wltcs.frc.core.api.event;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class EventBus {
  private final Map<Type, List<CallSite<Event>>> callSiteMap;
  private final Map<Type, List<EventListener<Event>>> listenerCache;

  public EventBus() {
    this.callSiteMap = new HashMap<>();
    this.listenerCache = new HashMap<>();
  }

  public final void subscribe(final Object subscriber) {
    Field[] declaredFields;
    for (int length = (declaredFields = subscriber.getClass().getDeclaredFields()).length, i = 0; i < length; ++i) {
      final Field field = declaredFields[i];
      if (field.isAnnotationPresent(EventTarget.class)) {
        final Type eventType = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        if (!field.isAccessible()) {
          field.setAccessible(true);
        }
        try {
          final EventListener<Event> listener = (EventListener<Event>) field.get(subscriber);
          if (this.callSiteMap.containsKey(eventType)) {
            final List<CallSite<Event>> callSites = this.callSiteMap.get(eventType);
            callSites.add(new CallSite<Event>(subscriber, listener));
          } else {
            this.callSiteMap.put(eventType, List.of(new CallSite(subscriber, listener)));
          }
        } catch (IllegalAccessException ignored) {
        }
      }
    }
    this.populateListenerCache();
  }

  private void populateListenerCache() {
    final Map<Type, List<CallSite<Event>>> callSiteMap = this.callSiteMap;
    final Map<Type, List<EventListener<Event>>> listenerCache = this.listenerCache;
    for (final Type type : callSiteMap.keySet()) {
      final List<CallSite<Event>> callSites = callSiteMap.get(type);
      final int size = callSites.size();
      final List<EventListener<Event>> listeners = new ArrayList<>(size);
      for (CallSite<Event> callSite : callSites) {
        listeners.add(callSite.listener);
      }
      listenerCache.put(type, listeners);
    }
  }

  public final void unsubscribe(final Object subscriber) {
    for (final List<CallSite<Event>> callSites : this.callSiteMap.values()) {
      callSites.removeIf(eventCallSite -> eventCallSite.owner == subscriber);
    }
    this.populateListenerCache();
  }

  public final void post(final Event event) {
    final List<EventListener<Event>> listeners = this.listenerCache.get(event.getClass());
    if (listeners != null) {
      for (final EventListener<Event> listener : listeners) {
        listener.call(event);
      }
    }
  }

  private record CallSite<Event>(Object owner, EventListener<Event> listener) {
  }
}
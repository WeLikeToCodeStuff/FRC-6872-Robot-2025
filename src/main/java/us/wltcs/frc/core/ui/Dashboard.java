package us.wltcs.frc.core.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Supplier;
import edu.wpi.first.networktables.*;

public class Dashboard {
  private final NetworkTableInstance inst = NetworkTableInstance.getDefault();
  private final NetworkTable table = inst.getTable("Robot");
  private final Map<String, NetworkTableEntry> entries;
  private final Map<String, Callable<?>> callbackMap;

  public Dashboard() {
    this.entries = new HashMap<>();
    this.callbackMap = new HashMap<>();
  }

  public void update() {
    callbackMap.forEach((key, callback) -> {
      try {
        Object value = callback.call();
        setValue(entries.get(key), value);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }

  public <T> void addEntry(String key, T value) {
    NetworkTableEntry entry = table.getEntry(key);
    entries.put(key, entry);
    setValue(entry, value);
  }

  // This function adds a callback function to call periodically
  public <T> void addEntry(String key, Supplier<T> supplier) {
    NetworkTableEntry entry = table.getEntry(key);
    entries.put(key, entry);
    callbackMap.put(key, supplier::get);
  }

  public Object getValue(String key) {
    NetworkTableEntry entry = table.getEntry(key);
    return getValue(entry);
  }

  private <T> void setValue(NetworkTableEntry entry, T value) {
    // TODO: implement more types
    if (value instanceof Double d) entry.setDouble(d);
    else if (value instanceof Boolean b) entry.setBoolean(b);
    else if (value instanceof String s) entry.setString(s);
    else if (value instanceof Integer i) entry.setInteger(i);
    else if (value instanceof Long l) entry.setInteger(l);
    else
      throw new IllegalArgumentException("Unsupported dashboard type: " + value.getClass());
  }

  private Object getValue(NetworkTableEntry entry) {
    NetworkTableValue value = entry.getValue();
    if (value.isDouble()) return entry.getDouble(0);
    else if (value.isBoolean()) return entry.getBoolean(false);
    else if (value.isString()) return entry.getString("");
    else if (value.isInteger()) return entry.getInteger(0);
    else
      throw new IllegalArgumentException("Unsupported dashboard type: " + value.getType());
  }
}
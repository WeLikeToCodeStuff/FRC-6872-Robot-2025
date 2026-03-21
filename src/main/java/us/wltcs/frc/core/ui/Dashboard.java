package us.wltcs.frc.core.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import lombok.AllArgsConstructor;
import lombok.Data;

public class Dashboard {
  private final NetworkTableInstance inst = NetworkTableInstance.getDefault();
  private final NetworkTable table = inst.getTable("6872 Robot");
  private final Map<String, NetworkTableEntry> entries;
  private final Map<String, Callable> callbackMap;

  public Dashboard() {
    this.entries = new HashMap<>();
    this.callbackMap = new HashMap<>();
  }

  public void update() {
    callbackMap.forEach((key, callback) -> {
      setValue(entries.get(key), callback);
    });
  }

  public <T> void addEntry(String key, Supplier<T> supplier) {
    NetworkTableEntry entry = table.getEntry(key);
    entries.put(key, entry);
    callbackMap.put(entry.getTopic().getName(), supplier::get);
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
}
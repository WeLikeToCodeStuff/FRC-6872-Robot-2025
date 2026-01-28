package us.wltcs.frc.core.ui;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Data @AllArgsConstructor
public class Dashboard {
    private final List<ShuffleboardTab> tabs;
    private final Map<String, GenericEntry> entries;
    private final Map<String, Runnable> updateMap;

    public Dashboard() {
        this.tabs = Collections.singletonList(Shuffleboard.getTab("Robot"));
        this.entries = new HashMap<>();
        this.updateMap = new HashMap<>();
    }

    public void initialize() {

    }

    public <T> void addEntry(String key, GenericEntry entry, Supplier<T> supplier) {
        entries.put(key, entry);
        updateMap.put(entry.getTopic().getName(), () -> {
            T value = supplier.get();

//            TODO: implement more types
            if (value instanceof Double d) entry.setDouble(d);
            else if (value instanceof Boolean b) entry.setBoolean(b);
            else if (value instanceof String s) entry.setString(s);
            else if (value instanceof Integer i) entry.setInteger(i);
            else if (value instanceof Long l) entry.setInteger(l);
            else {
                throw new IllegalArgumentException(
                        "Unsupported Shuffleboard type: " + value.getClass()
                );
            }
        });
    }

    public <T> void addEntry(GenericEntry entry, Supplier<T> supplier) {
        addEntry(entry.getTopic().getName().split("/")[entry.getTopic().getName().split("/").length - 1], entry, supplier);
    }
}

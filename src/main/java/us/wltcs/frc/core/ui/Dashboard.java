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

@Data @AllArgsConstructor
public class Dashboard {
    private final List<ShuffleboardTab> tabs;
    private final Map<String, GenericEntry> entries;

    public Dashboard() {
        this.tabs = Collections.singletonList(Shuffleboard.getTab("Dashboard"));
        this.entries = new HashMap<>();
    }

    public void initialize() {

    }

    public void addEntry(String key, GenericEntry entry) {
        entries.put(key, entry);
    }

    public void addEntry(GenericEntry entry) {
        addEntry(entry.getTopic().getName(), entry);
    }
}

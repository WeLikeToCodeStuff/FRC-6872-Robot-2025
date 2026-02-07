package us.wltcs.frc.core.ui;

import edu.wpi.first.networktables.GenericEntry;
import lombok.Data;
import java.util.function.Supplier;
import us.wltcs.frc.core.api.external.Elastic;

// Elastic dashboard implementation class
@Data
//@AllArgsConstructor
public class Dashboard {

  //  private final List<ShuffleboardTab> tabs;
//  private final Map<String, GenericEntry> entries;
//  private final Map<String, Runnable> updateMap;

  public Dashboard() {

  }

  public void initialize() {

  }

  public <T> void addEntry(String key, GenericEntry entry, Supplier<T> supplier) {

  }

  public <T> void addEntry(GenericEntry entry, Supplier<T> supplier) {
  }
}

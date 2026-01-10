package us.wltcs.frc.core.autonomous;

import lombok.experimental.UtilityClass;
import us.wltcs.frc.core.autonomous.actions.Action;
import us.wltcs.frc.core.autonomous.actions.Drive;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@UtilityClass
public class RecordingPlayer {
  private final Map<String, Action> actions = Map.of("movement", new Drive());
  private List<Recording> currentRecordings = new ArrayList<>();

  private long tickCount = 0;

  public void play(List<Recording> recordings) {
    currentRecordings = recordings;
    tickCount = 0;
  }

  public void next() {
    if (currentRecordings == null) return;

    System.out.print(currentRecordings);
    currentRecordings.stream().filter((recording) -> recording.getTick() == tickCount).forEach((recording) -> {
      Action action = actions.get(recording.getAction());
      if (action != null)
        action.execute(recording.getArguments());
    });

    tickCount++;
  }
}

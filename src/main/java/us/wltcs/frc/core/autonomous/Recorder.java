package us.wltcs.frc.core.autonomous;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;
import us.wltcs.frc.core.logging.Context;
import us.wltcs.frc.util.Json;

@UtilityClass
public class Recorder {
  private final File storageDirectory = new File("/home/lvuser/recordings");
  private final String fileName = "recording.json";

  private int tickCount = 0;
  private final List<Recording> recordingsBuffer = new ArrayList<>();

  public void start() {
    tickCount = 0;
    recordingsBuffer.clear();
  }

  public void tick() {
    tickCount++;
  }

  public void addRecording(String action, String... arguments) {
    recordingsBuffer.add(new Recording(tickCount, action, arguments));
  }

  public void saveRecordings() {
    try {
      if (!storageDirectory.exists())
        storageDirectory.mkdir();

      Json.write(recordingsBuffer, storageDirectory.getPath() + "/" + fileName);

    } catch (Exception exception) {
      final String message = String.format(
        "Failed to save recording to \"%s\", cause: %s", storageDirectory + fileName,
        exception.getCause()
      );
      Context.program.logError(message);
    }
  }
}
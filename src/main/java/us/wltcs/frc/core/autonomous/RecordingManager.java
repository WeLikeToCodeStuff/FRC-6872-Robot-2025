package us.wltcs.frc.core.autonomous;

import lombok.Getter;
import us.wltcs.frc.core.Robot;
import us.wltcs.frc.core.logging.Context;
import us.wltcs.frc.core.logging.Levels;
import us.wltcs.frc.util.Json;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

@Getter
public class RecordingManager {
    private final Map<String, Recording> recordings = new LinkedHashMap<>();
    private final File storageDirectory = new File("/home/lvuser/recordings");

    public void addRecording(String name, Recording recording) {
        recordings.put(name, recording);
    }

    public void clearRecordings() {
        recordings.clear();
    }

    public void loadRecordings() {
        if (!storageDirectory.exists()) storageDirectory.mkdirs();
        for (File file : Objects.requireNonNull(storageDirectory.listFiles())) {
            if (file.getName().endsWith(".json")) {
                try {
                    final Reader reader = new FileReader(file);
//                    Recording[] loadedRecordings = Robot.GSON.fromJson(reader, Recording[].class);
//                    for (Recording recording : loadedRecordings) {
//                        recordings.put(file.getName().replace(".json", ""), recording);
//                    }
                } catch (Exception exception) {
                    Context.program.log(Levels.ERROR,String.format("Failed to load recording from \"%s\", cause: %s", file.getPath(), exception.getCause()));
                }
            }
        }
    }
}

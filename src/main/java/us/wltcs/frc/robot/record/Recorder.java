package us.wltcs.frc.robot.record;

import com.google.gson.Gson;
import us.wltcs.frc.robot.Robot;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Recorder {
    private final File storageDirectory = new File("/home/lvuser/recordings");

    private final Gson gsonSerializer = Robot.gson;
    private int tickCount = 0;
    private final List<Record> storedRecordings = new ArrayList<>();

    public void start() {
        tickCount = 0;
        storedRecordings.clear();
    }

    public void tick() {
        tickCount++;
    }

    public void pushRecord(String action, String... arguments) {
        storedRecordings.add(new Record(tickCount, action, arguments));
    }

    public synchronized void save() {
        List<Record> cloneList = new ArrayList<>(storedRecordings);

        try {
            storageDirectory.mkdirs();

            File autonomousFile = new File(
                    storageDirectory.getPath()
                            + "/"
                            + ("red" == "red" ? "right.flux" : "left.flux")
            );

            if (autonomousFile.exists()) {
                autonomousFile.delete();
            }

            autonomousFile.createNewFile();

            System.out.println( cloneList.stream()
            .map(((record) -> gsonSerializer.toJson(record)))
            .collect(Collectors.toList()));
            Files.write(
                    autonomousFile.toPath(),
                    gsonSerializer.toJson(cloneList).getBytes()
            );
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

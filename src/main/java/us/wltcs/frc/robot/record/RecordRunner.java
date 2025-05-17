package us.wltcs.frc.robot.record;

import us.wltcs.frc.robot.record.reader.MovementExecutor;
import us.wltcs.frc.robot.record.reader.RecordReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecordRunner {
    private Map<String, RecordReader> recordReaders = Map.of(
            "movement", new MovementExecutor()
    );
    private int currentTick = 0;
    private List<Record> currentRecords = new ArrayList<>();

    public void play(List<Record> records) {
        currentRecords = records;
        currentTick = 0;
    }

    public void next() {
        if (currentRecords == null) return;

        System.out.print(currentRecords);
        currentRecords.stream().filter((record) -> record.getTick() == currentTick).forEach((record) -> {
            RecordReader reader = recordReaders.get(record.getAction());
            if (reader != null) {
                reader.execute(record.getArguments());
            }
        });

        currentTick++;
    }
}

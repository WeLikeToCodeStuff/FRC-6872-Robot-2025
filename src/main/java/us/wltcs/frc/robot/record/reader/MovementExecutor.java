package us.wltcs.frc.robot.record.reader;

import edu.wpi.first.wpilibj.drive.MecanumDrive;

public class MovementExecutor implements RecordReader {

    private MecanumDrive mecanumDrive;

    @Override
    public void execute(String[] parameters) {
        mecanumDrive.driveCartesian(
                Double.parseDouble(parameters[0]),
                Double.parseDouble(parameters[1]),
                Double.parseDouble(parameters[2])
        );
    }
}
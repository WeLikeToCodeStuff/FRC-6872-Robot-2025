package us.wltcs.frc.robot.core.autonomous.actions;

import edu.wpi.first.wpilibj.drive.MecanumDrive;

public class Drive implements Action {
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

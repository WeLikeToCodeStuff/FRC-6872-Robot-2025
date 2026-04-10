package us.wltcs.frc.robot.listeners;

import swervelib.SwerveModule;
import swervelib.parser.PIDFConfig;
import us.wltcs.frc.core.Robot;
import us.wltcs.frc.core.api.event.EventListener;
import us.wltcs.frc.core.api.event.EventTarget;
import us.wltcs.frc.core.devices.output.Launcher;
import us.wltcs.frc.robot.events.TeleoperatedPeriodicEvent;

public class LauncherListener {
  private Launcher launcher;

  public LauncherListener(Launcher launcher) {
    this.launcher = launcher;
  }

  @EventTarget
  public final EventListener<TeleoperatedPeriodicEvent> launcherListener = event -> {
    for (int i = 0; i < Robot.getInstance().getSwerveDriver().getSwerveDriver().getModules().length; i++) {
      // compare old PIDF to current one in dash
      PIDFConfig oldPIDF = Robot.getInstance().getSwerveDriver().getSwerveDriver().getModules()[i].getAnglePIDF();
      PIDFConfig currentPIDF = new PIDFConfig((double) Robot.getInstance().getNetworkTables().getValue(String.format("Module P")), (double) Robot.getInstance().getNetworkTables().getValue(String.format("Module I")), (double) Robot.getInstance().getNetworkTables().getValue(String.format("Module D")));
      if (oldPIDF.p != currentPIDF.p || oldPIDF.i != currentPIDF.i || oldPIDF.d != currentPIDF.d) {
        Robot.getInstance().getSwerveDriver().getSwerveDriver().getModules()[i].setAnglePIDF(currentPIDF);
      }
    }
    if (!event.getRobot().getController().buttonPressed(launcher.getIntakeButtonId()) || !event.getRobot().getController().getController().getRawButton(launcher.getLauncherButtonid()) || !event.getRobot().getController().getController().getRawButton(launcher.getReversedIntakeButtonId())) {
      this.launcher.getLeftLauncherMotor().set(0);
      this.launcher.getLeftIntakeMotor().set(0);
      this.launcher.getRightIntakeMotor().set(0);
    }


    if (event.getRobot().getController().buttonPressed(this.launcher.getIntakeButtonId())) this.launcher.intake();

    if (event.getRobot().getController().buttonPressed(this.launcher.getLauncherButtonid())) this.launcher.launch();

    if (event.getRobot().getController().buttonPressed(this.launcher.getReversedIntakeButtonId())) this.launcher.reversedIntake();
  };
}

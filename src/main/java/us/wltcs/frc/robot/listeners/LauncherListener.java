package us.wltcs.frc.robot.listeners;

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
    if (!event.getRobot().getController().buttonPressed(launcher.getIntakeButtonId()) || !event.getRobot().getController().getController().getRawButton(launcher.getLauncherButtonid())) {
      this.launcher.getLeftLauncherMotor().set(0);
      this.launcher.getLeftIntakeMotor().set(0);
      this.launcher.getRightIntakeMotor().set(0);
    }

    if (event.getRobot().getController().buttonPressed(launcher.getIntakeButtonId())) this.launcher.intake();

    if (event.getRobot().getController().buttonPressed(launcher.getLauncherButtonid())) this.launcher.launch();
  };
}

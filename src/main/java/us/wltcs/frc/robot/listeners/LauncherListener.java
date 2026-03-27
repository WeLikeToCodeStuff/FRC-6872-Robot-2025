package us.wltcs.frc.robot.listeners;

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
    if (event.getRobot().getController().getController().getRawButton(1))
      this.launcher.setSpeed(1);
    else
      this.launcher.setSpeed(0);
  };
}

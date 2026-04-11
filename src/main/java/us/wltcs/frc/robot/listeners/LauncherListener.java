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
      Object pObj = Robot.getInstance().getNetworkTables().getValue(String.format("Module P"));
      Object iObj = Robot.getInstance().getNetworkTables().getValue(String.format("Module I"));
      Object dObj = Robot.getInstance().getNetworkTables().getValue(String.format("Module D"));
      double pVal;
      if (pObj instanceof Number) pVal = ((Number) pObj).doubleValue();
      else {
        try { pVal = Double.parseDouble(String.valueOf(pObj)); } catch (Exception ex) { pVal = 0.0; }
      }
      double iVal;
      if (iObj instanceof Number) iVal = ((Number) iObj).doubleValue();
      else {
        try { iVal = Double.parseDouble(String.valueOf(iObj)); } catch (Exception ex) { iVal = 0.0; }
      }
      double dVal;
      if (dObj instanceof Number) dVal = ((Number) dObj).doubleValue();
      else {
        try { dVal = Double.parseDouble(String.valueOf(dObj)); } catch (Exception ex) { dVal = 0.0; }
      }
      PIDFConfig currentPIDF = new PIDFConfig(pVal, iVal, dVal);
      if (oldPIDF.p != currentPIDF.p || oldPIDF.i != currentPIDF.i || oldPIDF.d != currentPIDF.d) {
        // update hardware values to represent dashboard values
        Robot.getInstance().getSwerveDriver().getSwerveDriver().getModules()[i].setAnglePIDF(currentPIDF);
      }
    }
    if (!event.getRobot().getController().buttonPressed(launcher.getIntakeButtonId()) || !event.getRobot().getController().getController().getRawButton(launcher.getLauncherButtonid()) || !event.getRobot().getController().getController().getRawButton(launcher.getReversedIntakeButtonId())) {
      this.launcher.getLeftLauncherMotor().set(0);
      this.launcher.getLeftIntakeMotor().set(0);
      this.launcher.getRightIntakeMotor().set(0);
    }


    if (event.getRobot().getController().buttonPressed(this.launcher.getIntakeButtonId())) this.launcher.intake().execute();

    if (event.getRobot().getController().buttonPressed(this.launcher.getLauncherButtonid())) this.launcher.launch().execute();

    if (event.getRobot().getController().buttonPressed(this.launcher.getReversedIntakeButtonId())) this.launcher.reversedIntake().execute();
  };
}

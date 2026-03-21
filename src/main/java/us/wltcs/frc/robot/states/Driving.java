package us.wltcs.frc.robot.states;

import us.wltcs.frc.core.Driver;
import us.wltcs.frc.core.devices.input.Joystick;
import us.wltcs.frc.core.logging.Context;
import us.wltcs.frc.core.logging.Levels;
import us.wltcs.frc.core.statemachine.State;

public class Driving extends State {
  private final Driver swerveDriver;
  private final Joystick joystick;

  public Driving(Joystick joystick, Driver swerveDriver) {
    this.swerveDriver = swerveDriver;
    this.joystick = joystick;
  }

  @Override
  public void update(Object... args) {

    Context.movement.log(Levels.DEBUG, "Moving driving state");
  }
}

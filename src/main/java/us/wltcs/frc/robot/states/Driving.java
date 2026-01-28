package us.wltcs.frc.robot.states;

import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import us.wltcs.frc.core.devices.input.Joystick;
import us.wltcs.frc.core.logging.Context;
import us.wltcs.frc.core.logging.Levels;
import us.wltcs.frc.core.statemachine.State;

public class Driving extends State {
  private final Joystick joystick;

  public Driving(Joystick joystick) {
    this.joystick = joystick;
  }

  @Override
  public void update(Object... args) {
    if (args.length < 1) return;
    final SwerveDriveKinematics kinematics = args[0] instanceof SwerveDriveKinematics ? (SwerveDriveKinematics) args[0] : null;
    Context.movement.log(Levels.DEBUG, "Moving driving state");
  }
}

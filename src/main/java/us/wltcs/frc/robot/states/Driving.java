package us.wltcs.frc.robot.states;

import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import us.wltcs.frc.core.SwerveDriver;
import us.wltcs.frc.core.devices.input.Joystick;
import us.wltcs.frc.core.logging.Context;
import us.wltcs.frc.core.logging.Levels;
import us.wltcs.frc.core.statemachine.State;

public class Driving extends State {
  private final SwerveDriver swerveDriver;
  private final Joystick joystick;

  public Driving(Joystick joystick, SwerveDriver swerveDriver) {
    this.swerveDriver = swerveDriver;
    this.joystick = joystick;
  }

  @Override
  public void update(Object... args) {

    Context.movement.log(Levels.DEBUG, "Moving driving state");
  }
}

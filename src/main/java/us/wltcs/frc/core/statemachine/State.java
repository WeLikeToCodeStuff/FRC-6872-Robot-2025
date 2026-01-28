package us.wltcs.frc.core.statemachine;

import edu.wpi.first.math.kinematics.SwerveDriveKinematics;

public class State {
  public State(Object... args) {}
  public void enter() {}

  public void exit() {}

  public void update(Object... args) {
//    this should never be called, if it has then something has gone catastrophically wrong and we should probably reinitialize the entire state machine
  }
}

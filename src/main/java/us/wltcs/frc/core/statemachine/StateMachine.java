package us.wltcs.frc.core.statemachine;

import lombok.Getter;
import us.wltcs.frc.robot.states.Idle;

public class StateMachine {
  @Getter
  private State currentState = new Idle();

  public final void update(Object... args) {
    currentState.update(args);
  }

  public final void switchState(State state) {
    currentState.exit();
    this.currentState = state;
    currentState.enter();
  }
}

package us.wltcs.frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import us.wltcs.frc.core.api.event.EventBus;
import us.wltcs.frc.core.api.event.EventListener;
import us.wltcs.frc.core.api.event.EventTarget;
import us.wltcs.frc.core.api.event.EventType;
import us.wltcs.frc.core.impl.events.RobotStartEvent;
import us.wltcs.frc.core.statemachine.StateMachine;

// Robot class defining all the behaviour and actions of the robot
// Learn more about the TimedRobot class here:
// https://austinshalit.github.io/allwpilib/allwpilib/docs/release/java/edu/wpi/first/wpilibj/TimedRobot.html
public class Robot extends TimedRobot {
  private final EventBus eventBus = new EventBus();
  private final StateMachine stateMachine = new StateMachine();

  @Override
  public void robotInit() {
    eventBus.subscribe(this);
    eventBus.post(new RobotStartEvent(EventType.PRE));
    eventBus.post(new RobotStartEvent(EventType.POST));
  }

  @Override
  public void robotPeriodic() {
    stateMachine.update();
  }

  @Override
  public void autonomousInit() {

  }

  @Override
  public void autonomousPeriodic() {
    stateMachine.update();
  }

  @Override
  public void teleopInit() {

  }

  @Override
  public void teleopPeriodic() {
    stateMachine.update();
  }

  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

  @Override
  public void simulationInit() {
  }

  @Override
  public void simulationPeriodic() {
    stateMachine.update();
  }

  @EventTarget
  public final EventListener<RobotStartEvent> robotStartEventListener = event -> {

  };
}
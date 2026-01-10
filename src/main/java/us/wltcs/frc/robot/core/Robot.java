package us.wltcs.frc.robot.core;

import edu.wpi.first.wpilibj.TimedRobot;
import us.wltcs.frc.robot.core.api.event.EventBus;
import us.wltcs.frc.robot.core.api.event.EventListener;
import us.wltcs.frc.robot.core.api.event.EventTarget;
import us.wltcs.frc.robot.core.api.event.EventType;
import us.wltcs.frc.robot.core.impl.events.RobotStartEvent;

// Robot class defining all the behaviour and actions of the robot
// Learn more about the TimedRobot class here:
// https://austinshalit.github.io/allwpilib/allwpilib/docs/release/java/edu/wpi/first/wpilibj/TimedRobot.html
public class Robot extends TimedRobot {
  private final EventBus eventBus = new EventBus();
  @Override
  public void robotInit() {
    eventBus.subscribe(this);
    eventBus.post(new RobotStartEvent(EventType.PRE));
    eventBus.post(new RobotStartEvent(EventType.POST));
  }

  @Override
  public void robotPeriodic() {

  }

  @Override
  public void autonomousInit() {

  }

  @Override
  public void autonomousPeriodic() {

  }

  @Override
  public void teleopInit() {

  }

  @Override
  public void teleopPeriodic() {
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
  }

  @EventTarget
  public final EventListener<RobotStartEvent> robotStartEventListener = event -> {

  };
}
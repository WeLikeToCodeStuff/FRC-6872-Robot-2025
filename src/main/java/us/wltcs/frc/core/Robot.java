package us.wltcs.frc.core;

import edu.wpi.first.wpilibj.TimedRobot;
import us.wltcs.frc.core.api.event.*;
import us.wltcs.frc.core.devices.Camera;
import us.wltcs.frc.core.devices.Joystick;
import us.wltcs.frc.robot.events.RobotPeriodic;
import us.wltcs.frc.robot.events.RobotStart;
import us.wltcs.frc.core.statemachine.StateMachine;

// Robot class defining all the behaviour and actions of the robot
// Learn more about the TimedRobot class here:
// https://austinshalit.github.io/allwpilib/allwpilib/docs/release/java/edu/wpi/first/wpilibj/TimedRobot.html
public class Robot extends TimedRobot {
  private final EventBus eventBus = new EventBus();
  private final StateMachine stateMachine = new StateMachine();
  private final Camera camera = new Camera("Main", 1920, 1080);
  private final Joystick joystick = new Joystick(0);

  @Override
  public void robotInit() {
    eventBus.subscribe(this);
    eventBus.post(new RobotStart(EventType.PRE));
    eventBus.post(new RobotStart(EventType.POST));

  }

  @Override
  public void robotPeriodic() {
    stateMachine.update();
    System.out.println(joystick.getDirection().toString());
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
  public final EventListener<RobotStart> robotStartEventListener = event -> {

  };
}
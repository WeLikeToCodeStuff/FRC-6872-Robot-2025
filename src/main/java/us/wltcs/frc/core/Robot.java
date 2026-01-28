package us.wltcs.frc.core;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.wpilibj.TimedRobot;
import lombok.Getter;
import us.wltcs.frc.core.api.event.*;
import us.wltcs.frc.core.devices.output.Camera;
import us.wltcs.frc.core.devices.input.Joystick;
import us.wltcs.frc.core.logging.Context;
import us.wltcs.frc.core.logging.Levels;
import us.wltcs.frc.core.logging.Logger;
import us.wltcs.frc.robot.Motors;
import us.wltcs.frc.robot.events.RobotStart;
import us.wltcs.frc.core.statemachine.StateMachine;
import us.wltcs.frc.robot.states.Driving;
import us.wltcs.frc.robot.states.Idle;
import java.util.Timer;

// Robot class defining all the behaviour and actions of the robot
// Learn more about the TimedRobot class here:
// https://austinshalit.github.io/allwpilib/allwpilib/docs/release/java/edu/wpi/first/wpilibj/TimedRobot.html
public class Robot extends TimedRobot {
  private final EventBus eventBus = new EventBus();
  private final StateMachine stateMachine = new StateMachine();
  private final SwerveDriveKinematics swerveDriveKinematics = new SwerveDriveKinematics(Motors.m_frontLeftLocation, Motors.m_frontRightLocation, Motors.m_backLeftLocation, Motors.m_backRightLocation);

  @Getter
  private final Camera camera = new Camera("Main", 1920, 1080);

  @Getter
  private final Joystick joystick = new Joystick(0);

  @Override
  public void robotInit() {
    Context.program.log(Levels.INFO, String.format("Configured %s as primary controller", joystick.getJoystick().getName()));
    eventBus.subscribe(this);
    eventBus.post(new RobotStart(EventType.PRE));
    eventBus.post(new RobotStart(EventType.POST));
  }

  @Override
  public void robotPeriodic() {
    if (joystick.getDirection().length() != 0) {
      stateMachine.switchState(new Driving(joystick));
    } else {
      stateMachine.switchState(new Idle());
    }

    stateMachine.update(swerveDriveKinematics);
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
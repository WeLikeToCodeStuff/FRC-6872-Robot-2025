package us.wltcs.frc.core;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import lombok.Getter;
import us.wltcs.frc.core.api.event.*;
import us.wltcs.frc.core.autonomous.RecordingManager;
import us.wltcs.frc.core.devices.input.Controller;
import us.wltcs.frc.core.logging.Context;
import us.wltcs.frc.core.math.vector2.Vector2d;
import us.wltcs.frc.core.ui.NetworkTables;
import us.wltcs.frc.robot.events.RobotStart;
import us.wltcs.frc.core.statemachine.StateMachine;
import us.wltcs.frc.robot.events.TeleoperatedPeriodicEvent;

// Robot class defining all the behaviour and actions of the robot
// Learn more about the TimedRobot class here:
// https://austinshalit.github.io/allwpilib/allwpilib/docs/release/java/edu/wpi/first/wpilibj/TimedRobot.html
public class Robot extends TimedRobot {
  private final EventBus eventBus = new EventBus();
  private final NetworkTables networkTables = new NetworkTables();

  private final StateMachine stateMachine = new StateMachine();
  private final RecordingManager recordingManager = new RecordingManager();

  private final SwerveDriver swerveDriver = new SwerveDriver(5, 5, new Vector2d(5, 5));

  @Getter
  private Controller controller;

  @Override
  public void robotInit() {
    if (isSimulation()) {
//      DriverStation.silenceJoystickConnectionWarning(true);
    }

    //    Context.program.log(Levels.INFO, String.format("Configured %s as primary controller", joystick.getJoystick().getName()));
    eventBus.subscribe(this);
//    eventBus.subscribe(new LauncherListener(new Launcher(new SparkMax(9, SparkLowLevel.MotorType.kBrushless), new SparkMax(10, SparkLowLevel.MotorType.kBrushless))));
    eventBus.post(new RobotStart(EventType.PRE));
    eventBus.post(new RobotStart(EventType.POST));

    // Recordings initialization
    recordingManager.loadRecordings();
    controller =  new Controller(0);
  }

  @Override
  public void robotPeriodic() {
    stateMachine.update();
    networkTables.update();
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
    eventBus.post(new TeleoperatedPeriodicEvent(EventType.PRE, this));
    swerveDriver.drive(controller.getLeftDirection(), controller.getRightDirection(), true);

    stateMachine.update();
    eventBus.post(new TeleoperatedPeriodicEvent(EventType.POST, this));
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
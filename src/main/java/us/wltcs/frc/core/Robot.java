package us.wltcs.frc.core;

import java.io.IOException;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import org.json.simple.parser.ParseException;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.util.FileVersionException;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.motorcontrol.PWMTalonSRX;
import lombok.Getter;
import swervelib.math.SwerveMath;
import us.wltcs.frc.core.api.event.*;
import us.wltcs.frc.core.autonomous.RecordingManager;
import us.wltcs.frc.core.devices.input.Controller;
import us.wltcs.frc.core.devices.output.Launcher;
import us.wltcs.frc.core.math.vector2.Vector2d;
import us.wltcs.frc.core.ui.NetworkTables;
import us.wltcs.frc.robot.events.RobotStart;
import us.wltcs.frc.core.statemachine.StateMachine;
import us.wltcs.frc.robot.events.TeleoperatedPeriodicEvent;
import us.wltcs.frc.robot.listeners.LauncherListener;

// Robot class defining all the behaviour and actions of the robot
// Learn more about the TimedRobot class here:
// https://austinshalit.github.io/allwpilib/allwpilib/docs/release/java/edu/wpi/first/wpilibj/TimedRobot.html
public class Robot extends TimedRobot {
  private final EventBus eventBus = new EventBus();

  @Getter
  private final NetworkTables networkTables = new NetworkTables();

  private final StateMachine stateMachine = new StateMachine();
  private final RecordingManager recordingManager = new RecordingManager();

  private final SwerveDriver swerveDriver = new SwerveDriver(10, 10, new Vector2d(5, 5), this);

  @Getter
  private final Controller controller = new Controller(0);

  @Override
  public void robotInit() {
    if (isSimulation()) {
     DriverStation.silenceJoystickConnectionWarning(true);
    }

    eventBus.subscribe(this);
    eventBus.subscribe(new LauncherListener(new Launcher(new PWMTalonSRX(1), new PWMTalonSRX(2))));
    eventBus.post(new RobotStart(EventType.PRE));
    eventBus.post(new RobotStart(EventType.POST));

    // Recordings initialization
    recordingManager.loadRecordings();
  }

  @Override
  public void robotPeriodic() {
    stateMachine.update();
    networkTables.update();
  }

  @Override
  public void autonomousInit() {
    // call without using scheduler to avoid conflicts with state machine
    new PathPlannerAuto("New Path").schedule();
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
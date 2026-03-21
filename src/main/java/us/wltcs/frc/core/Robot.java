package us.wltcs.frc.core;

import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.wpilibj.TimedRobot;
import lombok.Getter;
import us.wltcs.frc.core.api.event.*;
import us.wltcs.frc.core.autonomous.RecordingManager;
import us.wltcs.frc.core.devices.input.Joystick;
import us.wltcs.frc.core.devices.output.Launcher;
import us.wltcs.frc.core.ui.Dashboard;
import us.wltcs.frc.robot.SwerveModules;
import us.wltcs.frc.robot.events.RobotStart;
import us.wltcs.frc.core.statemachine.StateMachine;
import us.wltcs.frc.robot.events.TeleoperatedPeriodicEvent;
import us.wltcs.frc.robot.listeners.LauncherListener;

// Robot class defining all the behaviour and actions of the robot
// Learn more about the TimedRobot class here:
// https://austinshalit.github.io/allwpilib/allwpilib/docs/release/java/edu/wpi/first/wpilibj/TimedRobot.html
public class Robot extends TimedRobot {
  private final EventBus eventBus = new EventBus();
  private final StateMachine stateMachine = new StateMachine();
  private final RecordingManager recordingManager = new RecordingManager();
  private final Driver driver = new Driver(
    SwerveModules.frontLeftMotorController,
    SwerveModules.frontRightMotorController,
    SwerveModules.rearLeftMotorController,
    SwerveModules.rearRightMotorController,
    5
  );

//  @Getter
//  private final Camera camera = new Camera("Main", 1920, 1080);

  @Getter
  private Joystick joystick;
  private final Dashboard dashboard = new Dashboard();

  @Override
  public void robotInit() {
    this.joystick = new Joystick(0);
//    Context.program.log(Levels.INFO, String.format("Configured %s as primary controller", joystick.getJoystick().getName()));
    eventBus.subscribe(this);
    eventBus.subscribe(new LauncherListener(new Launcher(new SparkMax(9, SparkLowLevel.MotorType.kBrushless), new SparkMax(10, SparkLowLevel.MotorType.kBrushless))));
    eventBus.post(new RobotStart(EventType.PRE));
    eventBus.post(new RobotStart(EventType.POST));

    joystick.init();
    // Recordings initialization
    recordingManager.loadRecordings();

    dashboard.<Double>addEntry("P", 1.0);
    dashboard.<Double>addEntry("I", 1.0);
    dashboard.<Double>addEntry("D", 1.0);
    dashboard.<Double>addEntry("MotorPower", () -> {return driver.getControllerOutput();});
  }

  @Override
  public void robotPeriodic() {
    stateMachine.update();
    joystick.init();
    dashboard.update();
    driver.setPID(
      (double)dashboard.getValue("P"),
      (double)dashboard.getValue("I"),
      (double)dashboard.getValue("D")
    );
  }

  @Override
  public void autonomousInit() {
    joystick.init();
  }

  @Override
  public void autonomousPeriodic() {
    stateMachine.update();
  }

  @Override
  public void teleopInit() {
    joystick.init();
  }

  @Override
  public void teleopPeriodic() {
    eventBus.post(new TeleoperatedPeriodicEvent(EventType.PRE, this));
    if (joystick.getDirection().length() != 0)
      driver.drive(joystick.getDirection(), joystick.getDirection(), true);
    else
      driver.stop();

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
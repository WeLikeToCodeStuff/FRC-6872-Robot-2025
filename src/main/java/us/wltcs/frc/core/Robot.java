package us.wltcs.frc.core;

import edu.wpi.first.wpilibj.motorcontrol.PWMTalonSRX;
import org.littletonrobotics.junction.LogFileUtil;
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;
import org.littletonrobotics.junction.wpilog.WPILOGReader;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;

import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import lombok.Getter;
import us.wltcs.frc.core.api.event.*;
import us.wltcs.frc.core.autonomous.RecordingManager;
import us.wltcs.frc.core.devices.input.Controller;
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
public class Robot extends LoggedRobot {
  private final EventBus eventBus = new EventBus();
  private final StateMachine stateMachine = new StateMachine();
  private final RecordingManager recordingManager = new RecordingManager();

  @Getter
  private Controller controller;
  private final Dashboard dashboard = new Dashboard();

  private final SwerveDriver driver = new SwerveDriver(
    // SwerveModules.frontLeftMotorController,
    // SwerveModules.frontRightMotorController,
    // SwerveModules.rearLeftMotorController,
    // SwerveModules.rearRightMotorController,
    5, dashboard
  );

//  @Getter
//  private final Camera camera = new Camera("Main", 1920, 1080);

  @Override
  public void robotInit() {
    this.controller = new Controller(0);

    if (isSimulation()) {
      DriverStation.silenceJoystickConnectionWarning(true);
    }

    //    Context.program.log(Levels.INFO, String.format("Configured %s as primary controller", joystick.getJoystick().getName()));
    eventBus.subscribe(this);
    eventBus.subscribe(new LauncherListener(new Launcher(new PWMTalonSRX(1), new PWMTalonSRX(2))));
    eventBus.post(new RobotStart(EventType.PRE));
    controller.initialize();
    // Recordings initialization
    recordingManager.loadRecordings();
    Logger.recordMetadata("ProjectName", "FRC-6872");
    if (isReal()) {
    //     // Logger.addDataReceiver(new WPILOGWriter()); // Log to a USB stick ("/U/logs")
        Logger.addDataReceiver(new NT4Publisher()); // Publish data to NetworkTables
    } else {
        setUseTiming(false); // Run as fast as possible
        // String logPath = LogFileUtil.findReplayLog(); // Pull the replay log from AdvantageScope (or prompt the user)
    //     // Logger.setReplaySource(new WPILOGReader(logPath)); // Read replay log
        // Logger.addDataReceiver(new WPILOGWriter(LogFileUtil.addPathSuffix(logPath, "_sim"))); // Save outputs to a new log
    }

    Logger.start(); // Start logging! No more data receivers, replay sources, or metadata values may be added.

//    dashboard.<Double>addEntry("P", 1.0);
//    dashboard.<Double>addEntry("I", 1.0);
//    dashboard.<Double>addEntry("D", 1.0);
//    dashboard.<Double>addEntry("MotorPower", () -> {return driver.getControllerOutput();});
    dashboard.<Double>addEntry("Robot Rotation", () -> { return Math.atan2(controller.getRightDirection().y, controller.getRightDirection().x); });
    eventBus.post(new RobotStart(EventType.PRE));
  }

  @Override
  public void robotPeriodic() {
    stateMachine.update();
    controller.initialize();
    dashboard.update();
  }

  @Override
  public void autonomousInit() {
    controller.initialize();
  }

  @Override
  public void autonomousPeriodic() {
    stateMachine.update();
  }

  @Override
  public void teleopInit() {
    controller.initialize();
  }

  @Override
  public void teleopPeriodic() {
    eventBus.post(new TeleoperatedPeriodicEvent(EventType.PRE, this));
    driver.drive(controller.getLeftDirection(), controller.getRightDirection(), true);

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
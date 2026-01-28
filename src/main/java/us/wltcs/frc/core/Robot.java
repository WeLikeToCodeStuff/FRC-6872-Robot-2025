package us.wltcs.frc.core;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import lombok.Getter;
import us.wltcs.frc.core.api.event.*;
import us.wltcs.frc.core.devices.output.Camera;
import us.wltcs.frc.core.devices.input.Joystick;
import us.wltcs.frc.core.logging.Context;
import us.wltcs.frc.core.logging.Levels;
import us.wltcs.frc.core.logging.Logger;
import us.wltcs.frc.core.ui.Dashboard;
import us.wltcs.frc.robot.Motors;
import us.wltcs.frc.robot.events.RobotStart;
import us.wltcs.frc.core.statemachine.StateMachine;
import us.wltcs.frc.robot.states.Driving;
import us.wltcs.frc.robot.states.Idle;

import java.util.Map;
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
  private final Dashboard dashboard = new Dashboard();

  @Override
  public void robotInit() {
    Context.program.log(Levels.INFO, String.format("Configured %s as primary controller", joystick.getJoystick().getName()));
    eventBus.subscribe(this);
    eventBus.post(new RobotStart(EventType.PRE));
    eventBus.post(new RobotStart(EventType.POST));

    //  Dashboard
    dashboard.initialize();
    dashboard.addEntry(Shuffleboard.getTab("Robot").add("Battery Voltage", RobotController.getBatteryVoltage()).withWidget(BuiltInWidgets.kVoltageView).withProperties(Map.of("min", 0, "max", 14)).getEntry(), RobotController::getBatteryVoltage);
    dashboard.addEntry(Shuffleboard.getTab("Robot").add("Swerve Angle", swerveDriveKinematics.getModules()[0].getAngle().getDegrees()).withWidget(BuiltInWidgets.kGyro).getEntry(), () -> swerveDriveKinematics.getModules()[0].getAngle().getDegrees());
//    dashboard.addEntry();
  }

  @Override
  public void robotPeriodic() {
    if (joystick.getDirection().length() != 0) {
      stateMachine.switchState(new Driving(joystick));
    } else {
      stateMachine.switchState(new Idle());
    }

    stateMachine.update(swerveDriveKinematics);

    dashboard.getEntries().forEach((name, entry) -> {
      Context.program.log(Levels.DEBUG, String.format("%s -> %s", name, entry.get().getValue().toString()));
    });
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
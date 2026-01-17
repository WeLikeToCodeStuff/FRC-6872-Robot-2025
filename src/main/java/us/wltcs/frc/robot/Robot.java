package us.wltcs.frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import us.wltcs.frc.core.api.event.EventBus;
import us.wltcs.frc.core.api.event.EventListener;
import us.wltcs.frc.core.api.event.EventTarget;
import us.wltcs.frc.core.api.event.EventType;
import us.wltcs.frc.core.math.vector2.Vector2d;
import us.wltcs.frc.robot.events.RobotStart;
import us.wltcs.frc.core.statemachine.StateMachine;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;

// Robot class defining all the behaviour and actions of the robot
// Learn more about the TimedRobot class here:
// https://austinshalit.github.io/allwpilib/allwpilib/docs/release/java/edu/wpi/first/wpilibj/TimedRobot.html
public class Robot extends TimedRobot {
  private final EventBus eventBus = new EventBus();
  private final StateMachine stateMachine = new StateMachine();
  private Vector2d direction;

  @Override
  public void robotInit() {
    eventBus.subscribe(this);
    eventBus.post(new RobotStart(EventType.PRE));
    eventBus.post(new RobotStart(EventType.POST));

    CameraServer.startAutomaticCapture();
// Creates the CvSink and connects it to the UsbCamera
    CvSink cvSink = CameraServer.getVideo();
    // Creates the CvSource and MjpegServer [2] and connects them
    CvSource outputStream = CameraServer.putVideo("Blur", 640, 480);

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
  public final EventListener<RobotStart> robotStartEventListener = event -> {

  };
}
package us.wltcs.frc.core;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.motorcontrol.PWMTalonSRX;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import lombok.Getter;
import us.wltcs.frc.core.api.event.*;
import us.wltcs.frc.core.devices.input.Controller;
import us.wltcs.frc.core.devices.output.Launcher;
import us.wltcs.frc.core.logging.Context;
import us.wltcs.frc.core.math.vector2.Vector2d;
import us.wltcs.frc.robot.events.RobotStart;
import us.wltcs.frc.core.statemachine.StateMachine;
import us.wltcs.frc.robot.events.TeleoperatedPeriodicEvent;
import us.wltcs.frc.robot.listeners.LauncherListener;
import java.util.Optional;
import java.util.TreeMap;

// Robot class defining all the behaviour and actions of the robot
// Learn more about the TimedRobot class here:
// https://austinshalit.github.io/allwpilib/allwpilib/docs/release/java/edu/wpi/first/wpilibj/TimedRobot.html
public class Robot extends TimedRobot {
  private final EventBus eventBus = new EventBus();

  @Getter
  private final NetworkTables networkTables = new NetworkTables();

  private final StateMachine stateMachine = new StateMachine();
  private SendableChooser<Command> autoChooser;

  private final SwerveDriver swerveDriver = new SwerveDriver(10, this);
  private final AutonomousDriver autonomousDriver = new AutonomousDriver(
    swerveDriver,
    swerveDriver.getRobotConfig(),
    new PPHolonomicDriveController( // PPHolonomicController is the built in path following controller for holonomic drive trains
      new PIDConstants(5, 0, 0), // Translation PID constants
      new PIDConstants(5, 0, 0) // Rotation PID constants
    )
  );

  // Position of the hoops in inches
  // https://firstfrc.blob.core.windows.net/frc2026/FieldAssets/2026-field-dimension-dwgs.pdf
  private final Vector2d[] hoopsPosition = {
    new Vector2d(182.11, 158.84), // Blue
    new Vector2d(651.22 - 182.11, 158.84) // Red
  };
  private boolean lockedOnHoop = false;
  private Double lastRotation = 0.0;

  @Getter
  private final Controller controller = new Controller(0);

  @Override
  public void robotInit() {
    if (isSimulation()) {
      DriverStation.silenceJoystickConnectionWarning(true);
    }

    this.autoChooser = AutoBuilder.buildAutoChooser();
    controller.update();
    eventBus.subscribe(this);
    eventBus.subscribe(new LauncherListener(new Launcher(new PWMTalonSRX(1), new PWMTalonSRX(2), new PWMTalonSRX(3))));
    eventBus.post(new RobotStart(EventType.PRE));
    eventBus.post(new RobotStart(EventType.POST));

    SmartDashboard.putData("Auto Chooser", autoChooser);
  }

  @Override
  public void robotPeriodic() {
    controller.update();
    stateMachine.update();
    networkTables.update();
  }

  @Override
  public void autonomousInit() {
    autonomousDriver.executeCommand(this.autoChooser.getSelected());
  }

  @Override
  public void autonomousPeriodic() {
//    stateMachine.update();
    // autonomousDriver.periodic();
  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {
    eventBus.post(new TeleoperatedPeriodicEvent(EventType.PRE, this));

    if (controller.buttonPressed(1))
      lockedOnHoop = !lockedOnHoop;

    TreeMap<Double, Vector2d> sortedPositions = new TreeMap<>();
    for (Vector2d position: hoopsPosition) {
      Vector2d direction = swerveDriver.getPositionInches().minus(position);
      sortedPositions.put(direction.length(), position);
    }

    // if (!lockedOnHoop) {
      double rotationAmount = controller.getRightDirection().x;
      swerveDriver.drive(controller.getLeftDirection(), rotationAmount, true);
    // }
    // if (lockedOnHoop) {  
      // Vector2d closestHoop = sortedPositions.firstEntry().getValue();
      // Vector2d hoopDirection = swerveDriver.getPositionInches().minus(closestHoop).normalized();
      // swerveDriver.drive(controller.getLeftDirection(), new Vector2d(hoopDirection.y, hoopDirection.x), true);
    // }

    // stateMachine.update();
    // eventBus.post(new TeleoperatedPeriodicEvent(EventType.POST, this));
    // lastRotation = swerveDriver.getRotationRadians();
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

  static public DriverStation.Alliance getAlliance() {
    Optional<DriverStation.Alliance> alliance = DriverStation.getAlliance();
    if (alliance.isEmpty()) {
      Context.program.logWarning("Alliance not set.");
      return DriverStation.Alliance.Red;
    }

    return alliance.get();
  }
}
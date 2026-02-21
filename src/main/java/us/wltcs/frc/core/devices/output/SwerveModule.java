package us.wltcs.frc.core.devices.output;

import edu.wpi.first.wpilibj.RobotController;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.AnalogInput;
import us.wltcs.frc.core.math.vector2.Vector2d;
import lombok.Getter;

// Swerves uses kinematics to control the wheels.
// https://dev.to/suleyman_sade/understanding-and-coding-a-swerve-module-in-frc-342m
// To simplify calculations, angles are calculated using radians.
// The position of the modules must be in meters.
// Page explaining how PID controller works: https://mikelikesrobots.github.io/blog/understand-pid-controllers/
public class SwerveModule {
  private static final double kMaxSpeedMetersPerSecond = 4.8;
  private static final double kMaxAngularSpeed = 2 * Math.PI; // radians per second
  public static final double kPModuleTurningController = 0.5;
  public static final double kPModuleDriveController = 0;

  // TODO: 'EncoderDistancePerPulse' should be calculated based on the gearing and wheel diameter
  public static final double kDriveEncoderDistancePerPulse = 0.0011265396;


  private final SparkMax driveMotor;
  private final SparkMax turnMotor;

  // Encoder that tracks the rotation velocity of the motor that drives the wheel
  private final RelativeEncoder driveEncoder;

  // Encoder that tracks the rotation velocity of the motor that turns the wheel
  private final AbsoluteEncoder turnEncoder;

  // Encoder that tracks the rotation of the wheel
  private final AnalogInput absoluteEncoder;

  private final PIDController drivingPIDController = new PIDController(0, 0, 0);
  private final PIDController turningPIDController = new PIDController(0.5, 0, 0.0001);

  private final double driveMotorGain = 0;
  private final double wheelAngularOffset = 0;

  @Getter
  private final Vector2d position;

  public SwerveModule(int driveMotorControllerId, int turnMotorControllerId, int absoluteEncoderId, Vector2d position) {
    this.position = position;
    this.driveMotor = new SparkMax(driveMotorControllerId, SparkLowLevel.MotorType.kBrushless);
    this.turnMotor = new SparkMax(turnMotorControllerId, SparkLowLevel.MotorType.kBrushless);

    this.driveEncoder = driveMotor.getEncoder();
    this.turnEncoder = turnMotor.getAbsoluteEncoder();
    this.absoluteEncoder = new AnalogInput(absoluteEncoderId);

    driveEncoder.setPosition(0);
    this.turningPIDController.enableContinuousInput(-Math.PI, Math.PI);
  }

  // Gets the velocity of the motor that drives the wheel
  public double getDriveVelocity() {
    return driveEncoder.getVelocity();
  }

  // Gets the velocity of the motor that turns the wheel
  public double getTurnVelocity() {
    return turnEncoder.getVelocity();
  }

  // Gets the position at which the wheel is located based off how much it has turned
  public double getTurningPosition() {
    return turnEncoder.getPosition();
  }

  public double getWheelRotationRadians() {
    double angle = absoluteEncoder.getVoltage() / RobotController.getVoltage5V();
    angle *= 2.0 * Math.PI;
    angle -= wheelAngularOffset;
//    return angle * (absoluteEncoderReversed ? -1.0 : 1.0);
    return angle;
  }

  public SwerveModuleState getState() {
    return new SwerveModuleState(driveEncoder.getVelocity(), new Rotation2d(getWheelRotationRadians()));
  }

  public void stop() {
    driveMotor.set(0);
    turnMotor.set(0);
  }

  public void setState(SwerveModuleState state) {
    // Prevent jittering at very low speeds
    if (Math.abs(state.speedMetersPerSecond) < 0.001) {
      stop();
      return;
    }

    state = SwerveModuleState.optimize(state, getState().angle);
    driveMotor.set(state.speedMetersPerSecond / kMaxSpeedMetersPerSecond);
    turnMotor.set(turningPIDController.calculate(getTurningPosition(), state.angle.getRadians()));
  }
}

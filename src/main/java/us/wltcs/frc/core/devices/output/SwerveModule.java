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
  private final SparkMax driveMotor;
  private final SparkMax turnMotor;

  // Tracks the rotation velocity of the motor that drives the wheel
  private final RelativeEncoder driveEncoder;

  // Tracks the rotation velocity of the motor that turns the wheel
  private final AbsoluteEncoder turnEncoder;

  // Tracks the rotation of the wheel
  private final AnalogInput absoluteEncoder;

  private final PIDController drivingPIDController = new PIDController(0.01, 0.01, 0);
  private final PIDController turningPIDController = new PIDController(0.5, 0, 0);

  private final double driveMotorGain = 1;

  // Offset of the encoder in radians
  private final double absoluteEncoderOffset;
  private final boolean absoluteEncoderReversed;

  public double getOutput() {
    return driveMotor.getAppliedOutput();
  }

  public void setPID(double p, double i, double d) {
    drivingPIDController.setPID(p, i, d);
  }

  @Getter
  private final Vector2d position;

  public SwerveModule(
    int turnMotorControllerId,
    int driveMotorControllerId,
    int absoluteEncoderId,
    Vector2d position,
    double absoluteEncoderOffset,
    boolean absoluteEncoderReversed
  ) {
    this.position = position;
    this.driveMotor = new SparkMax(driveMotorControllerId, SparkLowLevel.MotorType.kBrushless);
    this.turnMotor = new SparkMax(turnMotorControllerId, SparkLowLevel.MotorType.kBrushless);

    this.driveEncoder = driveMotor.getEncoder();
    this.turnEncoder = turnMotor.getAbsoluteEncoder();
    this.absoluteEncoder = new AnalogInput(absoluteEncoderId);
    this.absoluteEncoderOffset = absoluteEncoderOffset;
    this.absoluteEncoderReversed = absoluteEncoderReversed;

    this.turningPIDController.enableContinuousInput(-Math.PI, Math.PI);

    stop();
  }

  public double getDrivePosition() {
    return driveEncoder.getPosition();
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

  public double getAbsoluteEncoderRadians() {
    double angle = absoluteEncoder.getVoltage() / RobotController.getVoltage5V();
    angle *= 2.0 * Math.PI;
    angle -= absoluteEncoderOffset;
    if (absoluteEncoderReversed)
      angle *= -1;

    return angle;
  }

  public SwerveModuleState getState() {
    return new SwerveModuleState(driveEncoder.getVelocity(), new Rotation2d(getAbsoluteEncoderRadians()));
  }

  public void resetEncoders() {
    driveEncoder.setPosition(0);
  }

  public void stop() {
    driveMotor.set(0);
    turnMotor.set(0);
  }

  public void setState(SwerveModuleState state, float driveSpeed) {
    if (Math.abs(state.speedMetersPerSecond) < 0.001) {
      stop();
      return;
    }

    state.optimize(getState().angle);
    driveMotor.set(state.speedMetersPerSecond / driveSpeed);
    turnMotor.set(turningPIDController.calculate(getTurningPosition(), state.angle.getRadians()));
  }
}

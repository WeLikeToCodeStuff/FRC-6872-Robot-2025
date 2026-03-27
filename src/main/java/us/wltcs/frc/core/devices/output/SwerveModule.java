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
// Encoders types may vary for each swerve module.
//
// Learn more about swerves here.
// https://dev.to/suleyman_sade/understanding-and-coding-a-swerve-module-in-frc-342m
// https://www.youtube.com/watch?v=0Xi9yb1IMyA
//
// To simplify calculations, angles are calculated using radians.
// The position of the modules must be in meters.
// Page explaining how PID controller works: https://mikelikesrobots.github.io/blog/understand-pid-controllers/
//
// Could also later on implement this library instead of writing our own swerve code
// https://github.com/Yet-Another-Software-Suite/YAGSL?tab=readme-ov-file

public class SwerveModule {
  private final SparkMax driveMotor;
  private final SparkMax turnMotor;

  // Encoder for the rotation motor
  private final RelativeEncoder rotationEncoder;

  // Tracks how much the wheel has turned since start
  private final AbsoluteEncoder absoluteEncoder;

  private final PIDController drivingPIDController = new PIDController(0.01, 0.01, 0);
  private final PIDController turningPIDController = new PIDController(0.5, 0, 0);

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
    Vector2d position,
    double absoluteEncoderOffset,
    boolean absoluteEncoderReversed
  ) {
    this.position = position;
    this.driveMotor = new SparkMax(driveMotorControllerId, SparkLowLevel.MotorType.kBrushless);
    this.turnMotor = new SparkMax(turnMotorControllerId, SparkLowLevel.MotorType.kBrushless);
//    driveMotor.restoreFactoryDefaults();
//    steerMotor.restoreFactoryDefaults();
//    absoluteEncoder.getConfigurator().apply(new CANcoderConfiguration());

    this.rotationEncoder = turnMotor.getEncoder();
    this.absoluteEncoder = turnMotor.getAbsoluteEncoder();
    this.turningPIDController.enableContinuousInput(-Math.PI, Math.PI);

    stop();
  }

  public double getAngle() {
    return rotationEncoder.getPosition();
  }

  public void resetEncoders() {
//    driveEncoder.setPosition(0);
  }

  public void stop() {
//    driveMotor.set(0);
//    turnMotor.set(0);
  }

  public void setState(SwerveModuleState state, float driveSpeed) {
//    if (Math.abs(state.speedMetersPerSecond) < 0.001) {
//      stop();
//      return;
//    }
//
//    state.optimize(getState().angle);
//    driveMotor.set(state.speedMetersPerSecond / driveSpeed);
//    turnMotor.set(turningPIDController.calculate(getTurningPosition(), state.angle.getRadians()));
  }
}

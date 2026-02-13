package us.wltcs.frc.core.devices.output;

import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import us.wltcs.frc.core.math.vector2.Vector2d;
import lombok.Getter;

// Swerves uses kinematics to control the wheels.
// https://dev.to/suleyman_sade/understanding-and-coding-a-swerve-module-in-frc-342m
// To simplify calculations, all stored rotations are in degrees.
// The position of the modules must be in meters.
public class SwerveModule {
  private final SparkMax driveMotor;
  private final SparkMax turnMotor;

  private final RelativeEncoder driveEncoder;
  private final AbsoluteEncoder turnEncoder;

  private final SparkClosedLoopController drivingController;
  private final SparkClosedLoopController turningController;

  private double agularOffset = 0;

  @Getter
  private final Vector2d position;

  public SwerveModule(int driveMotorControllerId, int turnMotorControllerId, Vector2d position) {
    this.position = position;
    this.driveMotor = new SparkMax(driveMotorControllerId, SparkLowLevel.MotorType.kBrushless);
    this.turnMotor = new SparkMax(turnMotorControllerId, SparkLowLevel.MotorType.kBrushless);

    this.driveEncoder = driveMotor.getEncoder();
    this.turnEncoder = turnMotor.getAbsoluteEncoder();

    drivingController = driveMotor.getClosedLoopController();
    turningController = turnMotor.getClosedLoopController();

    driveEncoder.setPosition(0);
  }

  private static final double kMaxSpeedMetersPerSecond = 4.8;
  private static final double kMaxAngularSpeed = 2 * Math.PI; // radians per second
  public void setState(SwerveModuleState state) {
    // Apply chassis angular offset to the desired state.
    SwerveModuleState correctedDesiredState = new SwerveModuleState();
    correctedDesiredState.speedMetersPerSecond = state.speedMetersPerSecond;
    correctedDesiredState.angle = state.angle.plus(Rotation2d.fromRadians(agularOffset));

    // Optimize the reference state to avoid spinning further than 90 degrees.
    correctedDesiredState.optimize(new Rotation2d(turnEncoder.getPosition()));

    // Command driving and turning SPARKS towards their respective setpoints.
    drivingController.setSetpoint(correctedDesiredState.speedMetersPerSecond, SparkBase.ControlType.kVelocity);
    turningController.setSetpoint(correctedDesiredState.angle.getRadians(), SparkBase.ControlType.kPosition);
  }
}

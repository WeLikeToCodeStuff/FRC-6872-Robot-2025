package us.wltcs.frc.core.devices.output;

import edu.wpi.first.math.controller.PIDController;
//import com.revrobotics.CANEncoder;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

// To simplify calculations, all rotations are calculated using radians
public class SwerveModule {
  private final MotorController motorController;
  private final MotorController turningMotor;
  private final PIDController drivePIDController;
  private final PIDController turnPIDController;
//  private final AnalogInput absoluteEncoder;
//  private final CANEncoder driveEncoder;
//  private final

//  private final double motorRotationOffset;
//  private final double driveMotorGain;

  public SwerveModule(
    MotorController driveMotor,
    MotorController turningMotor,
    AnalogInput turningEncoder,
    PIDController drivePIDController,
    PIDController turnPIDController,
    double motorRotationOffset,
    double driveMotorGain
  ) {
    this.motorController = driveMotor;
    this.turningMotor = turningMotor;
    this.turnPIDController = turnPIDController;
    this.drivePIDController = drivePIDController;
//    this.motorRotationOffset = motorRotationOffset;
//    this.driveMotorGain = driveMotorGain;
//    this.turningEncoder = turningEncoder;

    this.turnPIDController.enableContinuousInput(-Math.PI, Math.PI);
  }
}

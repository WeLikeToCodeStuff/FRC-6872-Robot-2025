package us.wltcs.frc.core.devices.output;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.RelativeEncoder;

// Swerves uses kinematics to control the wheels.
// https://dev.to/suleyman_sade/understanding-and-coding-a-swerve-module-in-frc-342m
// To simplify calculations, all rotations are calculated using radians
public class SwerveModule {
  private final SparkMax driveMotor;
  private final SparkMax turnMotor;

  private final RelativeEncoder driveEncoder;
  private final AbsoluteEncoder turnEncoder;

  public SwerveModule(SparkMax driveMotor, SparkMax turnMotor) {
    this.driveMotor = driveMotor;
    this.turnMotor = turnMotor;

    this.driveEncoder = driveMotor.getEncoder();
    this.turnEncoder = turnMotor.getAbsoluteEncoder();
  }

  public void resetEncoders() {
    driveEncoder.setPosition(0);
  }
}

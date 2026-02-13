package us.wltcs.frc.core.devices.input;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;

// Single axis gyroscope because too broke to afford 3 axis one
// https://docs.wpilib.org/en/stable/docs/hardware/sensors/gyros-hardware.html
public class Gyroscope {
  private final ADXRS450_Gyro gyro;

  public Gyroscope() {
    this.gyro = new ADXRS450_Gyro();
  }

  public double getDegrees() {
    return gyro.getAngle();
  }

  public double getRadians() {
    return Math.toRadians(gyro.getAngle());
  }
}

package us.wltcs.frc.core;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import us.wltcs.frc.core.devices.input.Gyroscope;
import us.wltcs.frc.core.devices.output.SwerveModule;
import us.wltcs.frc.core.math.vector2.Vector2d;
import us.wltcs.frc.core.ui.Dashboard;
import us.wltcs.frc.robot.SwerveModules;

public class Driver {
  private final Gyroscope gyroscope = new Gyroscope();

  private final SwerveDriveKinematics kinematics;

  private final SwerveModule frontLeftModule;
  private final SwerveModule frontRightModule;
  private final SwerveModule rearLeftModule;
  private final SwerveModule rearRightModule;
  private final float driveSpeed;

  private final Dashboard dashboard;

  public double getControllerOutput() {
    return frontLeftModule.getOutput();
  }

  public Driver(
    SwerveModule frontLeft,
    SwerveModule frontRight,
    SwerveModule rearLeft,
    SwerveModule rearRight,
    float driveSpeed,
    Dashboard dashboard
  ) {
    this.dashboard = dashboard;
    this.driveSpeed = driveSpeed;
    if (Robot.isSimulation() && (frontLeft == null || frontRight == null || rearLeft == null || rearRight == null)) {
      // In simulation, we can create dummy modules if any of them are null to avoid null pointer exceptions.
      this.frontLeftModule = new SwerveModule(0, 0, new Vector2d(-SwerveModules.chassisWidth / 2, SwerveModules.chassisLength / 2), 0, false);
      this.frontRightModule = new SwerveModule(0, 0, new Vector2d(SwerveModules.chassisWidth / 2, SwerveModules.chassisLength / 2), 0, false);
      this.rearLeftModule = new SwerveModule(0, 0, new Vector2d(-SwerveModules.chassisWidth / 2, -SwerveModules.chassisLength / 2), 0, false);
      this.rearRightModule = new SwerveModule(0, 0, new Vector2d(SwerveModules.chassisWidth / 2, -SwerveModules.chassisLength / 2), 0, false);
    } else {
      this.frontLeftModule = frontLeft;
      this.frontRightModule = frontRight;
      this.rearLeftModule = rearLeft;
      this.rearRightModule = rearRight;
    }
    kinematics = new SwerveDriveKinematics(
      new Translation2d(frontLeft.getPosition().x, frontLeft.getPosition().y),
      new Translation2d(frontRight.getPosition().x, frontRight.getPosition().y),
      new Translation2d(rearLeft.getPosition().x, rearLeft.getPosition().y),
      new Translation2d(rearRight.getPosition().x, rearRight.getPosition().y)
    );
  }

  public void drive(Vector2d moveDirection, Vector2d turnDirection, boolean fieldRelative) {
    if (moveDirection.length() == 0 && turnDirection.length() == 0) {
      stop();
      return;
    }

    moveDirection = moveDirection.times(driveSpeed);
    double rotationRadians = Math.atan2(turnDirection.y, turnDirection.x);

    SwerveModuleState[] states;
    if (fieldRelative)
      states = kinematics.toSwerveModuleStates(ChassisSpeeds.fromFieldRelativeSpeeds(
        moveDirection.x, moveDirection.y, rotationRadians, Rotation2d.fromDegrees(-gyroscope.getDegrees())
      ));
    else
      states = kinematics.toSwerveModuleStates(new ChassisSpeeds(moveDirection.y, moveDirection.x, rotationRadians));

//    SwerveDriveKinematics.desaturateWheelSpeeds(states, driveSpeed);
    frontLeftModule.setState(states[0], driveSpeed);
    frontRightModule.setState(states[1], driveSpeed);
    rearLeftModule.setState(states[2], driveSpeed);
    rearRightModule.setState(states[3], driveSpeed);

    System.out.println(states[1].angle);
//    dashboard.<Double>addEntry("FrontLeft", states[0].angle);
//    dashboard.<Double>addEntry("FrontRight", 0.0);
//    dashboard.<Double>addEntry("BackLeft", 0.0);
//    dashboard.<Double>addEntry("BackRight", 0.0);
  }

  public void setPID(double p, double i, double d) {
    frontLeftModule.setPID(p, i, d);
    frontRightModule.setPID(p, i, d);
    rearLeftModule.setPID(p, i, d);
    rearRightModule.setPID(p, i, d);
  }

  public void stop() {
    frontLeftModule.stop();
    frontRightModule.stop();
    rearLeftModule.stop();
    rearRightModule.stop();
  }
}

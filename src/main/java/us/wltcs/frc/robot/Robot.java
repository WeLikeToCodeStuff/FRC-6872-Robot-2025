// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package us.wltcs.frc.robot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.drive.RobotDriveBase;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import us.wltcs.frc.robot.movement.SerializableMovement;
import us.wltcs.frc.robot.util.math.RoundingUtil;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

  private final Joystick joystick = new Joystick(0);
  private final MecanumDrive mecanumDrive = new MecanumDrive(MotorConstants.frontLeftMotorController, MotorConstants.rearLeftMotorController, MotorConstants.frontRightMotorController, MotorConstants.rearRightMotorController);

  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
  }

  @Override
  public void robotPeriodic() {
    try {
      SmartDashboard.putNumber("Main Joystick X value", RoundingUtil.roundNumber((float) joystick.getX(), 3));
      SmartDashboard.putNumber("Main Joystick Y value", RoundingUtil.roundNumber((float) joystick.getY(), 3));
      SmartDashboard.putNumber("Main Joystick Z value", RoundingUtil.roundNumber((float) joystick.getZ(), 3));
    } catch (Exception ignored) {}
  }

  @Override
  public void autonomousInit() {
//    m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  @Override
  public void autonomousPeriodic() {
    m_autoSelected = m_chooser.getSelected();
    System.out.println(m_autoSelected);
    // create JSON-ba
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    // Serialize the movement
    final SerializableMovement movement = new SerializableMovement(joystick);

    // Two methods for driving: driveCartesian() and drivePolar(). Refer to docs for more info.
    mecanumDrive.driveCartesian(joystick.getX(), joystick.getY(), joystick.getZ());

    /* TODO: Add time value to the JSON data to account time later on for recording movements */
    System.out.println(gson.toJson(movement));
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}

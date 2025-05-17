// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package us.wltcs.frc.robot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.gson.stream.JsonReader;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import us.wltcs.frc.robot.movement.SerializableMovement;
import us.wltcs.frc.robot.record.Record;
import us.wltcs.frc.robot.record.RecordRunner;
import us.wltcs.frc.robot.record.Recorder;
import us.wltcs.frc.robot.util.math.RoundingUtil;
import us.wltcs.frc.robot.util.input.JoystickUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

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
  private final Joystick intakeJoystick = new Joystick(1);
  private MecanumDrive mecanumDrive;
  private final Timer timer = new Timer();
  private long startTime;

  // Auto recording
  private final Recorder recorder = new Recorder();
  private final RecordRunner recordRunner = new RecordRunner();

  @Override
  public void robotInit() {
    MotorConstants.frontRightMotorController.setInverted(true);
    MotorConstants.frontLeftMotorController.setInverted(true);
    MotorConstants.topIntakeMotorController.setInverted(true);
    MotorConstants.bottomIntakeMotorController.addFollower(MotorConstants.topIntakeMotorController);
    MotorConstants.leftArmMotorController.addFollower(MotorConstants.rightArmMotorController);

    mecanumDrive = new MecanumDrive(MotorConstants.frontLeftMotorController, MotorConstants.rearLeftMotorController, MotorConstants.frontRightMotorController, MotorConstants.rearRightMotorController);
    timer.start();
    startTime = System.currentTimeMillis(); 
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
    } catch (Exception ignored) {
    }
  }

  @Override
  public void autonomousInit() {
    File autonomousFile = new File(
            "/recordings/"
                    + ("red" == "red" ? "right.flux" : "left.flux")
    );

    if (!autonomousFile.exists()) {
      return;
    }

      try {
          final JsonReader jsonReader = new JsonReader(new FileReader(autonomousFile));
          recordRunner.play(gson.fromJson(jsonReader, Record[].class));
      } catch (FileNotFoundException e) {
          throw new RuntimeException(e);
      }
//    m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
//    System.out.println("Auto selected: " + m_autoSelected);
  }

  @Override
  public void autonomousPeriodic() {
    recordRunner.next();
//    m_autoSelected = m_chooser.getSelected();
//    System.out.println(m_autoSelected);
//
//    // create JSON-ba
//    switch (m_autoSelected) {
//      case kCustomAuto:
//        // Put custom auto code here
//        break;
//      case kDefaultAuto:
//      default:
//        // Put default auto code here
//        break;
//    }
  }

  /**
   * This function is called once when teleop is enabled.
   */
  @Override
  public void teleopInit() {}

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    final double xSpeed = joystick.getX();
    final double ySpeed = joystick.getY();
    final double zRotation = joystick.getZ();
    mecanumDrive.driveCartesian(xSpeed, ySpeed, zRotation, Rotation2d.fromDegrees(0));

//    MotorConstants.leftArmMotorController.set(intakeJoystick.getY());

    if(intakeJoystick.getRawButton(2)) {
      MotorConstants.bottomIntakeMotorController.set(5);
    }

    if (intakeJoystick.getRawButton(3)) {
      MotorConstants.bottomIntakeMotorController.set(-5);
    }

    if (!intakeJoystick.getRawButton(2) && !intakeJoystick.getRawButton(3)) {
      MotorConstants.bottomIntakeMotorController.set(0);
    }

    recorder.pushRecord("movement", Double.toString(xSpeed), Double.toString(ySpeed), Double.toString(zRotation));
    recorder.tick();
  }

  /**
   * This function is called once when the robot is disabled.
   */
  @Override
  public void disabledInit() {
  }

  /**
   * This function is called periodically when disabled.
   */
  @Override
  public void disabledPeriodic() {
  }

  /**
   * This function is called once when test mode is enabled.
   */
  @Override
  public void testInit() {
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
    // Record the movements and stuff
  }

  /**
   * This function is called once when the robot is first started up.
   */
  @Override
  public void simulationInit() {
  }

  /**
   * This function is called periodically whilst in simulation.
   */
  @Override
  public void simulationPeriodic() {
    if (JoystickUtils.isJoystickUsed(joystick, 0.1f)) {
      // Serialize the movement
      final SerializableMovement movement = new SerializableMovement(joystick, System.currentTimeMillis() - startTime);
      System.out.println(gson.toJson(movement));
    }
  }
}

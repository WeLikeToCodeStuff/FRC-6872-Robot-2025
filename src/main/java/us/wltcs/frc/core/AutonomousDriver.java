package us.wltcs.frc.core;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import us.wltcs.frc.core.logging.Context;
import us.wltcs.frc.core.math.vector2.Vector2d;
import java.util.Optional;

// Instead of using Pathplanner's AutoBuilder class, we use our own implementation to avoid the use of commands
// Very proprietary

public class AutonomousDriver {
  private final SwerveDriver swerveDriver;
	private Command driveCommand;
  private final Timer timer = new Timer();
  private boolean isRunning = false;

	public AutonomousDriver(
    SwerveDriver swerveDriver,
    RobotConfig config,
    PPHolonomicDriveController driveController
  ) {
    this.swerveDriver = swerveDriver;
    AutoBuilder.configure(
      this::getPose,
      this::resetOdometry,
      swerveDriver::getRobotRelativeVelocity,
      (speeds, feedforwards) -> swerveDriver.driveAutonomous(speeds),
      driveController,
      config,
      () -> false
    );
  }

	public void runPath(String pathName) {
    PathPlannerPath path;
    try {
      path = PathPlannerPath.fromPathFile(pathName);
    } catch (Exception e) {
      Context.movement.logError("Autonomous failed to load \"%s\" ", pathName + ": " + e.getMessage());
      return;
    }

    timer.reset();

    if (Robot.getAlliance() == DriverStation.Alliance.Red) {
      path = path.flipPath();
      Context.movement.logInfo("Autonomous running on red alliance. Path mirrored automatically");
    }

    Optional<Pose2d> startPose = path.getStartingHolonomicPose();
    if (startPose.isPresent()) {
      resetOdometry(startPose.get());
    }
    timer.start();
    isRunning = true;
	}

  public void periodic() {
    if (driveCommand != null) {
      driveCommand.execute();
      if (driveCommand.isFinished()) {
        driveCommand.end(false);
        driveCommand = null;
        isRunning = false;
        timer.stop();
      }
    }
  }

  private Pose2d getPose() {
    return new Pose2d(
      new Translation2d(swerveDriver.getPositionMeters().x, swerveDriver.getPositionMeters().y),
      new Rotation2d(swerveDriver.getRotationRadians())
    );
  }

  private void resetOdometry(Pose2d pose) {
    swerveDriver.resetOdometry(
      new Vector2d(pose.getTranslation().getX(), pose.getTranslation().getY()),
      pose.getRotation().getRadians()
    );
  }
}

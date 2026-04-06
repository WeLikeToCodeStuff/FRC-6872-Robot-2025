package us.wltcs.frc.core;

import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.trajectory.PathPlannerTrajectory;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.pathplanner.lib.trajectory.PathPlannerTrajectoryState;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import us.wltcs.frc.core.logging.Context;
import us.wltcs.frc.core.math.vector2.Vector2d;
import java.util.Optional;

// Instead of using Pathplanner's AutoBuilder class, we use our own implementation to avoid the use of commands
// Very proprietary

public class AutonomousDriver {
  private final SwerveDriver swerveDriver;
	private final PPHolonomicDriveController driveController;
	private final RobotConfig robotConfig;

	private final Timer timer = new Timer();

	public AutonomousDriver(
    SwerveDriver swerveDriver,
    RobotConfig config,
    PPHolonomicDriveController driveController
  ) {
	  this.robotConfig = config;
    this.swerveDriver = swerveDriver;
    this.driveController = driveController;
  }

	public void runPath(String pathName) {
    // Getting path and trajectory
		PathPlannerPath path;
    try {
      path = PathPlannerPath.fromPathFile(pathName);
    } catch (Exception e) {
      Context.movement.logError("Autonomous failed to load \"%s\" ", pathName + ": " + e.getMessage());
      return;
    }

    PathPlannerTrajectory trajectory = path.getIdealTrajectory(robotConfig).get();
    if (Robot.getAlliance() == DriverStation.Alliance.Red) {
      path = path.flipPath();
      Context.movement.logInfo("Autonomous running on red alliance. Any paths will be mirrored automatically");
    }

    // If the path defines a starting pose, reset odometry
    try {
      Optional<Pose2d> startPose = path.getStartingHolonomicPose();
      if (startPose.isPresent()) {
        Pose2d pose = startPose.get();
        swerveDriver.resetOdometry(new Vector2d(pose.getX(), pose.getY()), pose.getRotation().getRadians());
      }
    } catch (Throwable ignore) {
      Context.movement.logWarning("Start pose unavailable. Odometry position will be defaulted to (0, 0)");
    }

    if (path.getIdealTrajectory(robotConfig).isEmpty())
      return;

    driveController.reset(getPose(), swerveDriver.getRobotRelativeVelocity());

//    double linearVel = Math.hypot(currentSpeeds.vxMetersPerSecond, currentSpeeds.vyMetersPerSecond);
//    if (path.getIdealStartingState() != null) {
//      // Check if we match the ideal starting state
//      boolean idealVelocity =
//              Math.abs(linearVel - path.getIdealStartingState().velocityMPS()) <= 0.25;
//      boolean idealRotation =
//              !robotConfig.isHolonomic
//                      || Math.abs(
//                      currentPose
//                              .getRotation()
//                              .minus(path.getIdealStartingState().rotation())
//                              .getDegrees())
//                      <= 30.0;
//      if (idealVelocity && idealRotation) {
//        // We can use the ideal trajectory
//        trajectory = path.getIdealTrajectory(robotConfig).orElseThrow();
//      } else {
//        // We need to regenerate
//        trajectory = path.generateTrajectory(currentSpeeds, currentPose.getRotation(), robotConfig);
//      }
//    } else {
//      // No ideal starting state, generate the trajectory
//      trajectory = path.generateTrajectory(currentSpeeds, currentPose.getRotation(), robotConfig);
//    }

    timer.reset();
    timer.start();

    double endTime = trajectory.getEndState().timeSeconds;
		while (timer.get() <= endTime) {
			PathPlannerTrajectoryState state = trajectory.sample(timer.get());

			ChassisSpeeds targetSpeeds = driveController.calculateRobotRelativeSpeeds(getPose(), state);
      swerveDriver.driveAutonomous(
        new Vector2d(targetSpeeds.vxMetersPerSecond, targetSpeeds.vyMetersPerSecond),
        targetSpeeds.omegaRadiansPerSecond
      );
		}

		timer.stop();
    swerveDriver.driveAutonomous(new Vector2d(0, 0), 0);
    Context.movement.logInfo("Autonomous has completed \"%s.path\"", pathName);
	}

  private Pose2d getPose() {
    return new Pose2d(
      new Translation2d(swerveDriver.getPositionMeters().x, swerveDriver.getPositionMeters().y),
      new Rotation2d(swerveDriver.getRotationRadians())
    );
  }
}

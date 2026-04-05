package us.wltcs.frc.core.autonomous;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.trajectory.PathPlannerTrajectory;
import com.pathplanner.lib.trajectory.PathPlannerTrajectoryState;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.Timer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A lightweight AutoBuilder replacement that does not require WPILib's command framework.
 * Use {@link #configure(...)} to create an instance and then call {@link #runPathBlocking(String)}
 * or {@link #runPathAsync(String)} to execute a PathPlanner path.
 */
public class CustomAutoBuilder extends AutoBuilder {
	private final Supplier<Pose2d> poseSupplier;
	private final Consumer<Pose2d> resetPose;
	private final Supplier<ChassisSpeeds> robotRelativeSpeedsSupplier;
	private final BiConsumer<ChassisSpeeds, double[]> outputConsumer;
	private final PPHolonomicDriveController controller;
	private final RobotConfig config;
	private final Supplier<Boolean> allianceSupplier;

	private final Timer timer = new Timer();

	private CustomAutoBuilder(
			Supplier<Pose2d> poseSupplier,
			Consumer<Pose2d> resetPose,
			Supplier<ChassisSpeeds> robotRelativeSpeedsSupplier,
			BiConsumer<ChassisSpeeds, double[]> outputConsumer,
			PPHolonomicDriveController controller,
			RobotConfig config,
			Supplier<Boolean> allianceSupplier
    ) {
		this.poseSupplier = poseSupplier;
		this.resetPose = resetPose;
		this.robotRelativeSpeedsSupplier = robotRelativeSpeedsSupplier;
		this.outputConsumer = outputConsumer;
		this.controller = controller;
		this.config = config;
		this.allianceSupplier = allianceSupplier;
	}

	public static CustomAutoBuilder configure(
			Supplier<Pose2d> poseSupplier,
			Consumer<Pose2d> resetPose,
			Supplier<ChassisSpeeds> robotRelativeSpeedsSupplier,
			BiConsumer<ChassisSpeeds, double[]> outputConsumer,
			PPHolonomicDriveController controller,
			RobotConfig config,
			Supplier<Boolean> allianceSupplier
	) {
		return new CustomAutoBuilder(
				poseSupplier,
				resetPose,
				robotRelativeSpeedsSupplier,
				outputConsumer,
				controller,
				config,
				allianceSupplier
		);
	}

	public void runPathBlocking(String pathName) {
		PathPlannerPath path;
		try {
			path = PathPlannerPath.fromPathFile(pathName);
		} catch (Exception e) {
			System.err.println("CustomAutoBuilder: failed to load path '" + pathName + "': " + e.getMessage());
			return;
		}

		PathPlannerTrajectory trajectory = new PathPlannerTrajectory(path, null, null, config);

		// If the path defines a starting pose, reset odometry via the provided consumer
		try {
			var maybeStart = path.getStartingHolonomicPose();
			if (maybeStart.isPresent()) {
				resetPose.accept(maybeStart.get());
			}
		} catch (Throwable ignore) {
			// Some versions may not provide starting pose; ignore if unavailable
		}

		// Query alliance supplier once (some AutoBuilders use this to mirror paths); keep for compatibility
		boolean isRedAlliance = false;
		try {
			isRedAlliance = allianceSupplier.get();
		} catch (Throwable ignore) {
		}
		if (isRedAlliance) {
			// Not mirroring automatically here; just log to aid debugging
			System.out.println("CustomAutoBuilder: running on red alliance (no automatic mirroring applied)");
		}

		timer.reset();
		timer.start();

		double endTime = trajectory.getEndState().timeSeconds;

		while (timer.get() <= endTime) {
			double t = timer.get();
			PathPlannerTrajectoryState state = trajectory.sample(t);
			Pose2d currentPose = poseSupplier.get();
			// read current robot-relative speeds if the caller provided them; this can be used for
			// diagnostics or feedforward calculations. We do a best-effort call and ignore exceptions.
			try {
				var cur = robotRelativeSpeedsSupplier.get();
				// keep the variable around for potential future use (avoid unused warnings)
				if (cur != null) {
					// no-op: intentionally kept for diagnostics
					double unused = Math.hypot(cur.vxMetersPerSecond, cur.vyMetersPerSecond);
					if (Double.isFinite(unused)) {
						// noop
					}
				}
			} catch (Throwable ignore) {
			}

			ChassisSpeeds targetSpeeds = controller.calculateRobotRelativeSpeeds(currentPose, state);

			// feedforwards are optional; we pass an empty array for compatibility
			outputConsumer.accept(targetSpeeds, new double[0]);

			// Basic loop timing (50Hz)
			try {
				Thread.sleep(20);
			} catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
				break;
			}
		}

		timer.stop();
	}

	public Thread runPathAsync(String pathName) {
		Thread t = new Thread(() -> runPathBlocking(pathName), "CustomAutoBuilder-run-" + pathName);
		t.setDaemon(true);
		t.start();
		return t;
	}
}

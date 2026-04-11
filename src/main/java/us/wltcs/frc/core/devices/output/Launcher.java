package us.wltcs.frc.core.devices.output;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.motorcontrol.PWMTalonSRX;
import edu.wpi.first.wpilibj2.command.Command;
import lombok.Getter;
import us.wltcs.frc.core.Robot;

@Getter
public class Launcher {
    private final PWMTalonSRX leftIntakeMotor;
    private final PWMTalonSRX rightIntakeMotor;
    private final PWMTalonSRX leftLauncherMotor;
    @Getter private final int intakeButtonId, launcherButtonid, reversedIntakeButtonId;

    public Launcher(PWMTalonSRX leftIntakeMotor, PWMTalonSRX rightIntakeMotor, PWMTalonSRX leftLauncherMotor) {
        this.leftIntakeMotor = leftIntakeMotor;
        this.rightIntakeMotor = rightIntakeMotor;
        this.leftLauncherMotor = leftLauncherMotor;
        if (Robot.isSimulation()) {
          this.intakeButtonId = 2;
          this.launcherButtonid = 3;
          this.reversedIntakeButtonId = 4;
        } else {
          this.intakeButtonId = 5;
          this.launcherButtonid = 6;
          this.reversedIntakeButtonId = 4;
        }
    }

    public Command intake() {
        // this.leftIntakeMotor.set(1);
        // this.rightIntakeMotor.set(1);
        // this.leftLauncherMotor.set(-1);
        return new IntakeCommand(this);
    }

    public Command launch() {
        // this.leftLauncherMotor.set(-1);
        // this.leftIntakeMotor.set(1);
        // this.rightIntakeMotor.set(-1);
        return new LaunchCommand(this);
    }

    public Command reversedIntake() {
        // this.leftLauncherMotor.set(-1);
        return new ReversedIntakeCommand(this);
    }
}

class IntakeCommand extends Command {
    private final Launcher launcher;
    private final Timer timer = new Timer();

    public IntakeCommand(Launcher launcher) {
        this.launcher = launcher;
    }

    @Override
    public void initialize() {
        timer.reset();
        timer.start();
        launcher.intake();
    }

    @Override
    public void end(boolean interrupted) {
        launcher.getLeftIntakeMotor().set(0);
        launcher.getRightIntakeMotor().set(0);
        launcher.getLeftLauncherMotor().set(0);
    }

    @Override
    public boolean isFinished() {
        return timer.hasElapsed(3);
    }
}

class LaunchCommand extends Command {
    private final Launcher launcher;
    private final Timer timer = new Timer();

    public LaunchCommand(Launcher launcher) {
        this.launcher = launcher;
    }

    @Override
    public void initialize() {
        timer.reset();
        timer.start();
        launcher.launch();
    }

    @Override
    public void end(boolean interrupted) {
        launcher.getLeftIntakeMotor().set(0);
        launcher.getRightIntakeMotor().set(0);
        launcher.getLeftLauncherMotor().set(0);
    }

    @Override
    public boolean isFinished() {
        return timer.hasElapsed(3);
    }
}

class ReversedIntakeCommand extends Command {
    private final Launcher launcher;
    private final Timer timer = new Timer();

    public ReversedIntakeCommand(Launcher launcher) {
        this.launcher = launcher;
    }

    @Override
    public void initialize() {
        timer.reset();
        timer.start();
        launcher.reversedIntake();
    }

    @Override
    public void end(boolean interrupted) {
        launcher.getLeftIntakeMotor().set(0);
        launcher.getRightIntakeMotor().set(0);
        launcher.getLeftLauncherMotor().set(0);
    }

    @Override
    public boolean isFinished() {
        return timer.hasElapsed(3);
    }
}
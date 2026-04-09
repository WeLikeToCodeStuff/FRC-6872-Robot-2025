package us.wltcs.frc.core.devices.output;

import edu.wpi.first.wpilibj.motorcontrol.PWMTalonSRX;
import edu.wpi.first.wpilibj2.command.Command;
import lombok.Getter;
import us.wltcs.frc.core.Robot;

@Getter
public class Launcher {
    private final PWMTalonSRX leftIntakeMotor;
    private final PWMTalonSRX rightIntakeMotor;
    private final PWMTalonSRX leftLauncherMotor;
    @Getter private final int intakeButtonId, launcherButtonid;

    public Launcher(PWMTalonSRX leftIntakeMotor, PWMTalonSRX rightIntakeMotor, PWMTalonSRX leftLauncherMotor) {
        this.leftIntakeMotor = leftIntakeMotor;
        this.rightIntakeMotor = rightIntakeMotor;
        this.leftLauncherMotor = leftLauncherMotor;
        if (Robot.isSimulation()) {
          this.intakeButtonId = 2;
          this.launcherButtonid = 3;
        } else {
          this.intakeButtonId = 5;
          this.launcherButtonid = 6;
        }
    }

    public Command intake() {
        this.leftIntakeMotor.set(1);
        this.rightIntakeMotor.set(-1);
      return null;
    }

    public Command launch() {
        this.leftLauncherMotor.set(1);
        this.leftIntakeMotor.set(1);
        this.rightIntakeMotor.set(-1);
      return null;
    }
}

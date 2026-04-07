package us.wltcs.frc.core.devices.output;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.motorcontrol.PWMTalonSRX;
import lombok.Getter;

@Getter
public class Launcher {
    private final PWMTalonSRX leftIntakeMotor;
    private final PWMTalonSRX rightIntakeMotor;
    private final PWMTalonSRX leftLauncherMotor;

    public Launcher(PWMTalonSRX leftIntakeMotor, PWMTalonSRX rightIntakeMotor, PWMTalonSRX leftLauncherMotor) {
        this.leftIntakeMotor = leftIntakeMotor;
        this.rightIntakeMotor = rightIntakeMotor;
        this.leftLauncherMotor = leftLauncherMotor;
    }

    public void intake() {
        this.leftIntakeMotor.set(1);
        this.rightIntakeMotor.set(-1);
    }

    public void launch() {
        this.leftLauncherMotor.set(1);
        this.leftIntakeMotor.set(1);
        this.rightIntakeMotor.set(-1);
    }
}

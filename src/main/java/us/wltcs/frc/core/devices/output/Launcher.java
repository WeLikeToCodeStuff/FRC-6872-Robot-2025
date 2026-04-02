package us.wltcs.frc.core.devices.output;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.motorcontrol.PWMTalonSRX;
import lombok.Getter;

public class Launcher {
    @Getter private final PWMTalonSRX leftLauncherMotor;
    @Getter private final PWMTalonSRX rightLauncherMotor;

    public Launcher(PWMTalonSRX leftLauncherMotor, PWMTalonSRX rightLauncherMotor) {
        this.leftLauncherMotor = leftLauncherMotor;
        this.rightLauncherMotor = rightLauncherMotor;
    }

//     public Launcher setSpeed(double speed) {
//         leftLauncherMotor.set(speed);
//         rightLauncherMotor.set(-speed);
//         return this;
//     }
}

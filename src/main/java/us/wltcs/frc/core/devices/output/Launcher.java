package us.wltcs.frc.core.devices.output;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

public class Launcher {
    private final SparkMax leftLauncherMotor;
    private final SparkMax rightLauncherMotor;

    public Launcher(SparkMax leftLauncherMotor, SparkMax rightLauncherMotor) {
        this.leftLauncherMotor = leftLauncherMotor;
        this.rightLauncherMotor = rightLauncherMotor;
        SparkMaxConfig config = new SparkMaxConfig();
        config.follow(leftLauncherMotor.getDeviceId());
        config.inverted(true);
        this.rightLauncherMotor.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
    }

    public Launcher setSpeed(double speed) {
        leftLauncherMotor.set(speed);
        return this;
    }
}

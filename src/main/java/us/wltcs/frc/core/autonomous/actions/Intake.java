package us.wltcs.frc.core.autonomous.actions;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;

public class Intake implements Action {
    private final MotorController intakeMotor = new PWMSparkMax(8);
    @Override
    public void execute(String[] parameters) {
        intakeMotor.setVoltage(Double.parseDouble(parameters[0]));
        intakeMotor.set(Double.parseDouble(parameters[1]));
    }
}

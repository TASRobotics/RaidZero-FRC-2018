package org.usfirst.frc.team4253.robot2017.components;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

public class Intake {

    private static final double POWER = 1;

    private CANTalon motor;

    public Intake(int motorID) {
        motor = new CANTalon(motorID);
        motor.changeControlMode(TalonControlMode.PercentVbus);
    }

    public void start() {
        motor.set(POWER);
    }

    public void stop() {
        motor.set(0);
    }

}

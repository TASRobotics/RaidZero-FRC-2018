package org.usfirst.frc.team4253.robot2017.components;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

public class Climb {

    private static final double POWER = 1;

    private CANTalon motor;

    public Climb(int motor1ID, int motor2ID) {
        motor = new CANTalon(motor1ID);
        motor.changeControlMode(TalonControlMode.PercentVbus);
        motor.enableBrakeMode(true);
        motor.setInverted(true);

        CANTalon motor2 = new CANTalon(motor2ID);
        motor2.changeControlMode(TalonControlMode.Follower);
        motor2.set(motor1ID);
        motor2.enableBrakeMode(true);
        motor2.reverseOutput(true);
    }

    public void up() {
        motor.set(POWER);
    }

    public void upHalfPower() {
        motor.set(POWER / 2);
    }

    public void stop() {
        motor.set(0);
    }

}

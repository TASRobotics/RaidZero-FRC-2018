package org.usfirst.frc.team4253.robot2018.components;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

/**
 * The intake.
 */
public class Intake {

    /**
     * The intake motor power.
     */
    private static final double POWER = 1;

    private CANTalon motor;

    /**
     * Constructs an Intake object and sets up the motor.
     * 
     * @param motorID the ID of the intake motor
     */
    public Intake(int motorID) {
        motor = new CANTalon(motorID);
        motor.changeControlMode(TalonControlMode.PercentVbus);
    }

    /**
     * Sets the intake to run forward.
     */
    public void run() {
        motor.set(POWER);
    }

    /**
     * Stops the intake.
     */
    public void stop() {
        motor.set(0);
    }

}

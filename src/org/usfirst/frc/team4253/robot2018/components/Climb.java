package org.usfirst.frc.team4253.robot2018.components;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

/**
 * The climb.
 */
public class Climb {

    /**
     * Scale factor for the climb motor power.
     * 
     * <p>Make this smaller to decrease the speed of the climb.
     */
    private static final double POWER = 1;

    private CANTalon motor;

    /**
     * Constructs a Climb object and sets up the motors.
     * 
     * @param motor1ID the ID of the leader motor.
     * @param motor2ID the ID of the follower motor.
     */
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

    /**
     * Sets the climb to full power.
     */
    public void up() {
        motor.set(POWER);
    }

    /**
     * Sets the climb to half power.
     */
    public void upHalfPower() {
        motor.set(POWER / 2);
    }

    /**
     * Stops the climb.
     */
    public void stop() {
        motor.set(0);
    }

}
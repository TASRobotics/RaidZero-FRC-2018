package org.usfirst.frc.team4253.robot2018.components;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Solenoid;

public class Ramps {

    private static final double MOTORPOWER = 0.5;
    private TalonSRX leftRamp;
    private TalonSRX rightRamp;
    private Solenoid releaser;

    /**
     * Constructs a ramp object and sets up the ramp motors and piston releaser.
     * 
     * @param leftRampID the ID of the left ramp motor
     * @param rightRampID the ID of the right ramp motor
     * @param channel the channel of the piston
     */
    public Ramps(int leftRampID, int rightRampID, int channel) {
        leftRamp = new TalonSRX(leftRampID);
        rightRamp = new TalonSRX(rightRampID);
        releaser = new Solenoid(channel);

        leftRamp.setNeutralMode(NeutralMode.Brake);
        rightRamp.setNeutralMode(NeutralMode.Brake);

        releaser.set(false);
    }

    /**
     * Moves the left ramp up.
     */
    public void moveLeftRamp() {
        leftRamp.set(ControlMode.PercentOutput, MOTORPOWER);
    }

    /**
     * Moves the right ramp up.
     */
    public void moveRightRamp() {
        rightRamp.set(ControlMode.PercentOutput, MOTORPOWER);
    }

    /**
     * Stops the left ramp.
     */
    public void stopLeftRamp() {
        leftRamp.set(ControlMode.PercentOutput, 0);
    }

    /**
     * Stops the right ramp.
     */
    public void stopRightRamp() {
        rightRamp.set(ControlMode.PercentOutput, 0);
    }

    /**
     * Releases the ramps.
     */
    public void releaseRamps() {
        releaser.set(true);
    }
}

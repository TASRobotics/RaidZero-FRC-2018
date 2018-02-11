package org.usfirst.frc.team4253.robot2018.components;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 * The ramps (both sides).
 */
public class Ramps {

    private static final double MOTORPOWER = 0.5;
    private TalonSRX leftRamp;
    private TalonSRX rightRamp;
    private DoubleSolenoid releaser;

    /**
     * Constructs a ramp object and sets up the ramp motors and piston releaser.
     * 
     * @param leftRampID the ID of the left ramp motor
     * @param rightRampID the ID of the right ramp motor
     * @param pullChannel kReverse aka pin in
     * @param nothing kForward aka pin out
     */
    public Ramps(int leftRampID, int rightRampID, int pullChannel, int nothing) {
        leftRamp = new TalonSRX(leftRampID);
        rightRamp = new TalonSRX(rightRampID);
        releaser = new DoubleSolenoid(nothing, pullChannel);

        leftRamp.setNeutralMode(NeutralMode.Brake);
        rightRamp.setNeutralMode(NeutralMode.Brake);

        leftRamp.setInverted(true);
        rightRamp.setInverted(true);

        releaser.set(DoubleSolenoid.Value.kForward);
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
        releaser.set(DoubleSolenoid.Value.kReverse);
    }

    /**
     * Setup the ramp releaser
     */
    public void setup() {
        releaser.set(DoubleSolenoid.Value.kForward);
    }
}

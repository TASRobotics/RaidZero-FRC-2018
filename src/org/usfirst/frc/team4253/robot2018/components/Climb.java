package org.usfirst.frc.team4253.robot2018.components;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 * The ramps (both sides).
 */
public class Climb {

    private static final double WINCHPOWER = 0.5;
    private TalonSRX winch;
    private TalonSRX arm;
    private DoubleSolenoid releaser;

    /**
     * Constructs a ramp object and sets up the ramp motors and piston releaser.
     * 
     * @param armID the ID of the arm motor
     * @param winchID the ID of the winch motor
     * @param pullChannel kReverse aka pin in
     * @param nothing kForward aka pin out
     */
    public Climb(int armID, int winchID, int pullChannel, int nothing) {
        arm = new TalonSRX(armID);
        winch = new TalonSRX(winchID);
        releaser = new DoubleSolenoid(nothing, pullChannel);

        arm.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder,
            MotorSettings.PID_IDX, MotorSettings.TIMEOUT);

        winch.setNeutralMode(NeutralMode.Brake);
        arm.setNeutralMode(NeutralMode.Brake);

        winch.setInverted(false);
        arm.setInverted(true);

        arm.setSensorPhase(true);
        arm.setSelectedSensorPosition(0, MotorSettings.PID_IDX, MotorSettings.TIMEOUT);

        releaser.set(DoubleSolenoid.Value.kForward);
    }

    /**
     * Moves the left ramp up.
     */
    public void moveArm(double power) {
        arm.set(ControlMode.PercentOutput, power / 3);
    }

    public void moveArmTurbo(double power) {
        moveArm(power * 2);
    }

    /**
     * Moves the right ramp up.
     */
    public void moveWinch(double power) {
        winch.set(ControlMode.PercentOutput, power);
    }

    /**
     * Stops the left ramp.
     */
    public void stopArm() {
        arm.set(ControlMode.PercentOutput, 0);
    }

    /**
     * Stops the right ramp.
     */
    public void stopWinch() {
        winch.set(ControlMode.PercentOutput, 0);
    }

    /**
     * Releases the ramps.
     */
    public void releaseArm() {
        releaser.set(DoubleSolenoid.Value.kReverse);
    }

    /**
     * Setup the ramp releaser
     */
    public void setup() {
        releaser.set(DoubleSolenoid.Value.kForward);
    }

    /**
     * Return encoder values
     * 
     * @return the encoder position of the arm
     */
    public int getEncoderPos() {
        return arm.getSelectedSensorPosition(MotorSettings.PID_IDX);
    }
}

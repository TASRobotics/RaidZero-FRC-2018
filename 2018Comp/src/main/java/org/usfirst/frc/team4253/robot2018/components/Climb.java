package org.usfirst.frc.team4253.robot2018.components;

import org.usfirst.frc.team4253.robot2018.MotorSettings;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 * The arm and winch.
 */
public class Climb {

    private static final double WINCHPOWER = 0.5;
    private TalonSRX winch;
    private TalonSRX arm;
    private DoubleSolenoid releaser;

    /**
     * Constructs a Climb object and sets up the arm and winch motors.
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
     * Moves the arm at normal speed.
     * 
     * @param power the motor power (between -1 and 1)
     */
    public void moveArm(double power) {
        arm.set(ControlMode.PercentOutput, power / 3);
    }

    /**
     * Moves the arm at turbo speed.
     * 
     * @param power the motor power (between -1 and 1)
     */
    public void moveArmTurbo(double power) {
        moveArm(power * 2);
    }

    /**
     * Moves the winch.
     */
    public void moveWinch(double power) {
        winch.set(ControlMode.PercentOutput, power);
    }

    /**
     * Stops the arm.
     */
    public void stopArm() {
        arm.set(ControlMode.PercentOutput, 0);
    }

    /**
     * Stops the winch.
     */
    public void stopWinch() {
        winch.set(ControlMode.PercentOutput, 0);
    }

    /**
     * Releases the arm.
     */
    public void releaseArm() {
        releaser.set(DoubleSolenoid.Value.kReverse);
    }

    /**
     * Setup the releaser.
     */
    public void setup() {
        releaser.set(DoubleSolenoid.Value.kForward);
    }

    /**
     * Returns the encoder position of the arm.
     * 
     * @return the encoder position of the arm
     */
    public int getEncoderPos() {
        return arm.getSelectedSensorPosition(MotorSettings.PID_IDX);
    }
}

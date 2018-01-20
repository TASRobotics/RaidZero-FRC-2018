package org.usfirst.frc.team4253.robot2018.components;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Intake {

    private TalonSRX leaderMotor;
    private TalonSRX follower;
    private DoubleSolenoid claw;
    private static final double IDLE_POWER = 0.1;

    /**
     * 
     * @param leaderID the ID for the leader motor
     * @param followerID the ID for the follower motor
     * @param forwardChannel the pneumatic channel for forward
     * @param backwardChannel the pneumatic channel for backward
     */
    public Intake(int leaderID, int followerID, int forwardChannel, int backwardChannel) {
        leaderMotor = new TalonSRX(leaderID);
        follower = new TalonSRX(followerID);
        claw = new DoubleSolenoid(forwardChannel, backwardChannel);

        leaderMotor.setNeutralMode(NeutralMode.Brake);
        follower.setNeutralMode(NeutralMode.Brake);

        follower.set(ControlMode.Follower, leaderID);

        claw.set(DoubleSolenoid.Value.kReverse);
    }

    /**
     * Closes the claw
     */
    public void closeClaw() {
        leaderMotor.set(ControlMode.PercentOutput, 1.0);
        claw.set(DoubleSolenoid.Value.kReverse);
    }

    /**
     * Opens the claw
     */
    public void openClaw() {
        leaderMotor.set(ControlMode.PercentOutput, -1.0);
        claw.set(DoubleSolenoid.Value.kForward);
    }

    /**
     * Sets the motor to a lower output when idle
     */
    public void idle() {
        leaderMotor.set(ControlMode.PercentOutput, IDLE_POWER);
        claw.set(DoubleSolenoid.Value.kReverse);
    }
}

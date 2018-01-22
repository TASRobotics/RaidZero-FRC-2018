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
    // private static DoubleSolenoid.Value kOpen = DoubleSolenoid.Value.kForward,kClose =
    // DoubleSolenoid.Value.kReverse,kOff = DoubleSolenoid.Value.kOff;

    /**
     * Construct an intake object
     * 
     * @param leaderID the ID for the leader motor
     * @param followerID the ID for the follower motor
     * @param forwardChannel the pneumatic channel for opening claw
     * @param backwardChannel the pneumatic channel for closing claw
     */
    public Intake(int leaderID, int followerID, int forwardChannel, int backwardChannel) {
        leaderMotor = new TalonSRX(leaderID);
        follower = new TalonSRX(followerID);
        claw = new DoubleSolenoid(forwardChannel, backwardChannel);

        leaderMotor.setNeutralMode(NeutralMode.Brake);
        follower.setNeutralMode(NeutralMode.Brake);

        follower.set(ControlMode.Follower, leaderID);

        claw.set(SolenoidSetting.kForward);
    }

    /**
     * Closes the claw
     */
    public void closeClaw() {
        leaderMotor.set(ControlMode.PercentOutput, 1.0);
        claw.set(SolenoidSetting.kReverse);
    }

    /**
     * Opens the claw
     */
    public void openClaw() {
        leaderMotor.set(ControlMode.PercentOutput, -1.0);
        claw.set(SolenoidSetting.kForward);
    }

    /**
     * Sets the motor to a lower output when idle
     */
    public void idle() {
        leaderMotor.set(ControlMode.PercentOutput, IDLE_POWER);
        claw.set(SolenoidSetting.kOff);
    }
}

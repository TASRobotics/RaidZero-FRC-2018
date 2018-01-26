package org.usfirst.frc.team4253.robot2018.components;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Intake {

    private static final double IDLE_POWER = 0.1;
    private static DoubleSolenoid.Value OPEN_POSITION = DoubleSolenoid.Value.kForward;
    private static DoubleSolenoid.Value CLOSED_POSITION = DoubleSolenoid.Value.kReverse;

    private TalonSRX leaderMotor;
    private TalonSRX follower;
    private DoubleSolenoid claw;

    /**
     * Constructs an intake object.
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

        follower.setInverted(true);

        follower.set(ControlMode.Follower, leaderID);
    }

    /**
     * Grabs the cube.
     */
    public void grab() {
        leaderMotor.set(ControlMode.PercentOutput, 1.0);
        claw.set(OPEN_POSITION);
    }

    /**
     * Releases the cube.
     */
    public void release() {
        leaderMotor.set(ControlMode.PercentOutput, -1.0);
        claw.set(OPEN_POSITION);
    }

    /**
     * Sets the motor to a lower output when idle.
     */
    public void idle() {
        leaderMotor.set(ControlMode.PercentOutput, IDLE_POWER);
        claw.set(CLOSED_POSITION);
    }

}

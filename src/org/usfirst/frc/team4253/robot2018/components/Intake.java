package org.usfirst.frc.team4253.robot2018.components;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Intake {
    private static double HOLD_VAL = 0.2;
    private TalonSRX leaderMotor;
    private TalonSRX follower;
    private boolean isHolding;
    
    public Intake(int leaderID, int followerID) {
        leaderMotor = new TalonSRX(leaderID);
        follower = new TalonSRX(followerID);
        
        leaderMotor.setNeutralMode(NeutralMode.Brake);
        follower.setNeutralMode(NeutralMode.Brake);
        
        follower.set(ControlMode.Follower, leaderID);
        
        isHolding = false;
    }
    
    public void grab() {
        leaderMotor.set(ControlMode.PercentOutput, 1.0);
    }
    
    public void releaseMotors() {
        leaderMotor.set(ControlMode.PercentOutput, -1.0);
    }
    
    public void holdMotors() {
        if (isHolding) {
            leaderMotor.set(ControlMode.PercentOutput, HOLD_VAL);
        }
        else {
            leaderMotor.set(ControlMode.PercentOutput, 0);
        }
    }
    
    public void hold() {
        isHolding = true;
    }
    
    public void release() {
        isHolding = false;
    }
}

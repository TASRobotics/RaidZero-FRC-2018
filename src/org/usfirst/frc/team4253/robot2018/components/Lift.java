package org.usfirst.frc.team4253.robot2018.components;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import java.util.concurrent.TimeUnit;

public class Lift {
    
    private TalonSRX leaderMotor;
    private TalonSRX follower;
    
    private final int targetVel = 1515; // can be changed
    private final int targetAccel = 6060;
    
    private final int timeout = 10;
    
    private final double PValue = 0.0;
    private final double IValue = 0.0;
    private final double DValue = 0.0;
    private final double FValue = 0.0;
    
    // # of motors can be changed by adding more slave motors
    
    /**
     * Constructs a Lift object and sets up the lift motors.
     * 
     * @param leaderID the ID of the leader lift motor
     * @param followerID the ID of the follower lift motor
     */
    public Lift(int leaderID, int followerID) {
        leaderMotor = new TalonSRX(leaderID);
        follower = new TalonSRX(followerID);
        
        follower.set(ControlMode.Follower, leaderID);
        
        leaderMotor.setNeutralMode(NeutralMode.Brake);
        follower.setNeutralMode(NeutralMode.Brake);
        
        leaderMotor.configMotionCruiseVelocity( targetVel, timeout );
        leaderMotor.configMotionAcceleration( targetAccel, timeout );
        
        leaderMotor.setSelectedSensorPosition(0, 0, timeout);
        follower.setSelectedSensorPosition(0, 0, timeout);
        
        leaderMotor.config_kP(0, PValue, timeout);
        leaderMotor.config_kI(0, IValue, timeout);
        leaderMotor.config_kD(0, DValue, timeout);
        leaderMotor.config_kF(0, FValue, timeout);
    }
    
    /**
     * moves the lift to the target position through motion magic.
     * 
     * @param targetPos the target position for the lift.
     */
    public void move(double targetPos) {
        leaderMotor.set(ControlMode.MotionMagic, targetPos);  
        
        try { TimeUnit.MILLISECONDS.sleep(10);   // may not be needed
        } catch(Exception e) {}
    }
    
}

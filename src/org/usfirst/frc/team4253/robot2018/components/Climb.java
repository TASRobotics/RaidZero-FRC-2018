package org.usfirst.frc.team4253.robot2018.components;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Climb {

    private TalonSRX leaderMotor;
    private TalonSRX follower;
    
    private final static int botEnc = 0, topEnc = 3000;

    private final static int TARGET_VEL = 1515; // can be changed
    private final static int TARGET_ACCEL = 6060;

    private final double P_VALUE = 0.0;
    private final double I_VALUE = 0.0;
    private final double D_VALUE = 0.0;
    private final double F_VALUE = 0.0;
    
 // # of motors can be changed by adding more slave motors

    /**
     * Constructs a Lift object and sets up the lift motors.
     * 
     * @param leaderID the ID of the leader lift motor
     * @param followerID the ID of the follower lift motor
     */
    public Climb(int leaderID, int followerID) {
        leaderMotor = new TalonSRX(leaderID);
        follower = new TalonSRX(followerID);

        follower.set(ControlMode.Follower, leaderID);

        leaderMotor.setNeutralMode(NeutralMode.Brake);
        follower.setNeutralMode(NeutralMode.Brake);

        leaderMotor.configMotionCruiseVelocity(TARGET_VEL, MotorSettings.TIMEOUT);
        leaderMotor.configMotionAcceleration(TARGET_ACCEL, MotorSettings.TIMEOUT);

        leaderMotor.setSelectedSensorPosition(0, 0, MotorSettings.TIMEOUT);
        follower.setSelectedSensorPosition(0, 0, MotorSettings.TIMEOUT);

        leaderMotor.config_kP(0, P_VALUE, MotorSettings.TIMEOUT);
        leaderMotor.config_kI(0, I_VALUE, MotorSettings.TIMEOUT);
        leaderMotor.config_kD(0, D_VALUE, MotorSettings.TIMEOUT);
        leaderMotor.config_kF(0, F_VALUE, MotorSettings.TIMEOUT);
    }

    /**
     * Moves the climb to the target position through motion magic.
     * 
     * @param targetPos the target position for the climb
     */
    public void move(double targetPos) {
        leaderMotor.set(ControlMode.MotionMagic, targetPos);
    }
    
    /**
     * Moves the robot up.
     * 
     * @param speed for climbing up (0~1)
     */
    public void up(double speed) {
        if(leaderMotor.getSelectedSensorPosition(0)<topEnc && 
            leaderMotor.getSelectedSensorPosition(0)>botEnc)
            leaderMotor.set(ControlMode.PercentOutput, speed);
        
    }
    
    /**
     * Moves the robot up.
     * 
     * @param speed for climbing down (0~1)
     */
    public void down(double speed) {
        if(leaderMotor.getSelectedSensorPosition(0)<topEnc && 
            leaderMotor.getSelectedSensorPosition(0)>botEnc)
            leaderMotor.set(ControlMode.PercentOutput, -speed);
    }
}

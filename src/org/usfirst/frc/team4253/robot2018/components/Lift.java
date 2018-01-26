package org.usfirst.frc.team4253.robot2018.components;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Lift {

    private TalonSRX lift;

    private static final int TARGET_VEL = 1515; // can be changed
    private static final int TARGET_ACCEL = 6060;

    private static final double P_VALUE = 0.0;
    private static final double I_VALUE = 0.0;
    private static final double D_VALUE = 0.0;
    private static final double F_VALUE = 0.0;

    // # of motors can be changed by adding more slave motors

    /**
     * Constructs a Lift object and sets up the lift motors.
     * 
     * @param leaderID the ID of the leader lift motor
     * @param followerID the ID of the follower lift motor
     */
    public Lift(int leaderID, int followerID) {
        lift = new TalonSRX(leaderID);

        lift.setNeutralMode(NeutralMode.Brake);

        lift.configMotionCruiseVelocity(TARGET_VEL, MotorSettings.TIMEOUT);
        lift.configMotionAcceleration(TARGET_ACCEL, MotorSettings.TIMEOUT);

        lift.setSelectedSensorPosition(0, 0, MotorSettings.TIMEOUT);

        lift.config_kP(0, P_VALUE, MotorSettings.TIMEOUT);
        lift.config_kI(0, I_VALUE, MotorSettings.TIMEOUT);
        lift.config_kD(0, D_VALUE, MotorSettings.TIMEOUT);
        lift.config_kF(0, F_VALUE, MotorSettings.TIMEOUT);
    }

    /**
     * Moves the lift to the target position through motion magic.
     * 
     * @param targetPos the target position for the lift
     */
    public void move(double targetPos) {
        lift.set(ControlMode.MotionMagic, targetPos);
    }

    /**
     * Moves the lift by percentOutput.
     * 
     * @param percentPower the percent power the run the motor.
     */
    public void movePWM(double percentPower) {
        lift.set(ControlMode.PercentOutput, percentPower);
    }
}

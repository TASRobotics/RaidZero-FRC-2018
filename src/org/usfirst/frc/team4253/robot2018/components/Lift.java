package org.usfirst.frc.team4253.robot2018.components;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Lift {

    public static final int SWITCH_HEIGHT = 15000;
    public static final int SCALE_HEIGHT = 32500;

    private static final int TARGET_VEL = 1000; // can be changed
    private static final int TARGET_ACCEL = 1000;

    private static final double P_VALUE = 3;
    private static final double I_VALUE = 0.0;
    private static final double D_VALUE = 0.0;
    private static final double F_VALUE = 0.3593609487;

    private TalonSRX lift;

    // # of motors can be changed by adding more slave motors

    /**
     * Constructs a Lift object and sets up the lift motor.
     * 
     * @param motorID the ID of the lift motor
     */
    public Lift(int motorID) {
        lift = new TalonSRX(motorID);

        lift.setNeutralMode(NeutralMode.Brake);
        lift.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, MotorSettings.PID_IDX,
            MotorSettings.TIMEOUT);

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
    public boolean move(double targetPos) {
        lift.set(ControlMode.MotionMagic, targetPos);
        return Math.abs(lift.getSelectedSensorVelocity(MotorSettings.PID_IDX)) <= 5
            && Math.abs(targetPos - lift.getSelectedSensorPosition(MotorSettings.PID_IDX)) <= 10;
    }

    /**
     * Moves the lift by percentOutput.
     * 
     * @param percentPower the percent power the run the motor.
     */
    public void movePWM(double percentPower) {
        lift.set(ControlMode.PercentOutput, percentPower);
    }

    public TalonSRX lift() {
        return lift;
    }

    public void resetEnc() {
        lift.setSelectedSensorPosition(0, 0, MotorSettings.TIMEOUT);
    }
}

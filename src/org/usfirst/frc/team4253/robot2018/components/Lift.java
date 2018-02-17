package org.usfirst.frc.team4253.robot2018.components;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

/**
 * The lift.
 */
public class Lift {

    public static final int GRAB_CUBE_HEIGHT = 0;
    public static final int SWITCH_HEIGHT = 3750;
    public static final int SCALE_HEIGHT = 8125; // Highest possible pos : 9800

    private static final int TARGET_VEL = 1000;
    private static final int TARGET_ACCEL = 1000;

    private static final double P_VALUE = 3;
    private static final double I_VALUE = 0.0;
    private static final double D_VALUE = 0.0;
    private static final double F_VALUE = 0.3593609487;

    private static final int VEL_TOLERANCE = 5;
    private static final int POS_TOLERANCE = 10;

    private TalonSRX lift;

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
        lift.setInverted(true);

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

    public boolean checkFinished(double targetPos) {
        int currentVel = lift.getSelectedSensorVelocity(MotorSettings.PID_IDX);
        int currentPos = lift.getSelectedSensorPosition(MotorSettings.PID_IDX);
        return Math.abs(currentVel) <= VEL_TOLERANCE
            && Math.abs(targetPos - currentPos) <= POS_TOLERANCE;
    }

    /**
     * Moves the lift by percentOutput.
     * 
     * @param percentPower the percent power the run the motor.
     */
    public void movePWM(double percentPower) {
        lift.set(ControlMode.PercentOutput, percentPower);
    }

    /**
     * Resets the lift motor encoder.
     */
    public void resetEnc() {
        lift.setSelectedSensorPosition(0, 0, MotorSettings.TIMEOUT);
    }

    /**
     * Return encoder values
     * 
     * @return the encoder position of the lift
     */
    public int getEncoderPos() {
        return lift.getSelectedSensorPosition(MotorSettings.PID_IDX);
    }
}

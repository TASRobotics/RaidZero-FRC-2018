package org.usfirst.frc.team4253.robot2018.components;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Climb {

    private TalonSRX motor;

    private static final int lowEnc = 0, highEnc = 3000;// can be changed

    private static final int TARGET_VEL = 1515; // can be changed
    private static final int TARGET_ACCEL = 6060;

    private static final double P_VALUE = 0.0;
    private static final double I_VALUE = 0.0;
    private static final double D_VALUE = 0.0;
    private static final double F_VALUE = 0.0;
    private static final int IZONE_VALUE = 0;

    /**
     * Constructs a climb object and sets up the climb motor.
     * 
     * @param motorID the ID of the climb motor
     */
    public Climb(int motorID) {
        motor = new TalonSRX(motorID);

        motor.setNeutralMode(NeutralMode.Brake);

        motor.configMotionCruiseVelocity(TARGET_VEL, MotorSettings.TIMEOUT);
        motor.configMotionAcceleration(TARGET_ACCEL, MotorSettings.TIMEOUT);

        motor.setSelectedSensorPosition(0, 0, MotorSettings.TIMEOUT);

        motor.config_kP(0, P_VALUE, MotorSettings.TIMEOUT);
        motor.config_kI(0, I_VALUE, MotorSettings.TIMEOUT);
        motor.config_kD(0, D_VALUE, MotorSettings.TIMEOUT);
        motor.config_kF(0, F_VALUE, MotorSettings.TIMEOUT);
        motor.config_IntegralZone(0, IZONE_VALUE, MotorSettings.TIMEOUT);
    }

    /**
     * Moves the climb to the target position through motion magic.
     * 
     * @param targetPos the target position for the climb
     */
    public void moveMotionMagic(double targetPos) {
        motor.set(ControlMode.MotionMagic, targetPos);
    }

    /**
     * Moves the ramp down.
     * 
     * @param percentV percent voltage (0~1)
     */
    public void move(double percentV) {
        motor.set(ControlMode.PercentOutput, Math.abs(percentV));
    }

    /**
     * Stops the ramp.
     */
    public void stop() {
        motor.set(ControlMode.PercentOutput, 0);
    }
}

package org.usfirst.frc.team4253.robot2018.components;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Climb {

    private TalonSRX leaderMotor;
    private TalonSRX follower;
    private DoubleSolenoid ramp;

    private static final int lowEnc = 0, highEnc = 3000;// can be changed

    private static final int TARGET_VEL = 1515; // can be changed
    private static final int TARGET_ACCEL = 6060;

    private static final double P_VALUE = 0.0;
    private static final double I_VALUE = 0.0;
    private static final double D_VALUE = 0.0;
    private static final double F_VALUE = 0.0;
    private static final int IZONE_VALUE = 0;

    // # of motors can be changed by adding more slave motors

    /**
     * Constructs a climb object and sets up the climb motors and pneumatics.
     * 
     * @param leaderID the ID of the leader climb motor
     * @param followerID the ID of the follower climb motor
     * @param in the forward channel for the ramp pneumatics
     * @param out the reverse channel for the ramp pneumatics
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
        leaderMotor.config_IntegralZone(0, IZONE_VALUE, MotorSettings.TIMEOUT);
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
     * Moves the ramp up.
     * 
     * @param percentV percent Voltage provided for climbing up (0~1)
     */
    public void up(double percentV) {
        if (leaderMotor.getSelectedSensorPosition(0) <= highEnc) {
            leaderMotor.set(ControlMode.PercentOutput, Math.abs(percentV));
        }

    }

    /**
     * Moves the ramp down.
     * 
     * @param percentV percent Voltage provided for climbing down (0~1)
     */
    public void down(double percentV) {
        if (leaderMotor.getSelectedSensorPosition(0) >= lowEnc) {
            leaderMotor.set(ControlMode.PercentOutput, -Math.abs(percentV));
        }
    }

    /**
     * Stop the ramp
     */
    public void idle() {
        leaderMotor.set(ControlMode.PercentOutput, 0);
    }
}

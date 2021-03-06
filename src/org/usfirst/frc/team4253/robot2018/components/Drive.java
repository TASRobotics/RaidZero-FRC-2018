package org.usfirst.frc.team4253.robot2018.components;

import org.usfirst.frc.team4253.robot2018.MotorSettings;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;
import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 * The drive system.
 */
public class Drive {

    private WPI_TalonSRX leftMotor;
    private WPI_TalonSRX rightMotor;
    private DoubleSolenoid gearShift;
    private PigeonIMU pigeon;

    private static final double[] FPID = { 0.717, 2.2, 0.004, 15, 50 };

    /**
     * Constructs a Drive object and sets up the motors and gear shift.
     * 
     * @param leftLeaderID the ID of the left leader motor
     * @param rightLeaderID the ID of the right leader motor
     * @param leftFollowerID the ID of the left follower motor
     * @param rightFollowerID the ID of the right follower motor
     * @param gearShiftForward the forward channel for the gear shift
     * @param gearShiftReverse the reverse channel for the gear shift
     */
    //@formatter:off
    public Drive(
        int leftLeaderID, int rightLeaderID,
        int leftFollowerID, int rightFollowerID,
        int gearShiftForward, int gearShiftReverse
    ) {
        //@formatter:on
        leftMotor = initSide(leftLeaderID, leftFollowerID, false, false);
        // rightMotor = initSide(rightLeaderID, rightFollowerID, true, true);

        rightMotor = initSide(rightLeaderID, rightFollowerID, true, false); // build error so not
                                                                            // inverted
        gearShift = new DoubleSolenoid(gearShiftForward, gearShiftReverse);
    }

    /**
     * Constructs and configures the motors for one side of the robot (i.e. one leader and one
     * follower), and returns the leader motor object.
     * 
     * <p>TODO: Make sure allowing the follower motor to be garbage collected is OK.
     * 
     * @param leaderID the ID of the leader motor
     * @param followerID the ID of the follower motor
     * @return the newly constructed leader motor object
     */
    private WPI_TalonSRX initSide(int leaderID, int followerID, boolean invertMotor,
        boolean invertSensor) {
        WPI_TalonSRX leader = new WPI_TalonSRX(leaderID);
        WPI_TalonSRX follower = new WPI_TalonSRX(followerID);

        leader.setNeutralMode(NeutralMode.Brake);
        follower.setNeutralMode(NeutralMode.Brake);

        leader.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, MotorSettings.PID_IDX,
            MotorSettings.TIMEOUT);

        follower.set(ControlMode.Follower, leaderID);

        leader.setSensorPhase(invertSensor);
        leader.setInverted(invertMotor);
        leader.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10,
            MotorSettings.TIMEOUT);
        leader.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10,
            MotorSettings.TIMEOUT);
        follower.setInverted(invertMotor);
        if (!invertMotor) {
            pigeon = new PigeonIMU(follower);
        }

        setPID(leader);

        return leader;
    }

    /**
     * Sets the PID of the leader Talon SRX.
     * 
     * @param leader the leader TalonSRX to change PID settings for
     */
    public void setPID(TalonSRX leader) {
        leader.selectProfileSlot(MotorSettings.PID_SLOT, MotorSettings.PID_IDX);
        leader.config_kF(MotorSettings.PID_SLOT, FPID[0], MotorSettings.TIMEOUT);
        leader.config_kP(MotorSettings.PID_SLOT, FPID[1], MotorSettings.TIMEOUT);
        leader.config_kI(MotorSettings.PID_SLOT, FPID[2], MotorSettings.TIMEOUT);
        leader.config_kD(MotorSettings.PID_SLOT, FPID[3], MotorSettings.TIMEOUT);
        leader.config_IntegralZone(MotorSettings.PID_SLOT, (int) FPID[4], MotorSettings.TIMEOUT);
    }

    /**
     * Returns the left leader motor.
     * 
     * <p>Anything done to this motor will also be followed by the other left motor.
     * 
     * @return the left leader motor
     */
    public WPI_TalonSRX getLeftMotor() {
        return leftMotor;
    }

    /**
     * Returns the right leader motor.
     * 
     * <p>Anything done to this motor will also be followed by the other right motor.
     * 
     * @return the right leader motor
     */
    public WPI_TalonSRX getRightMotor() {
        return rightMotor;
    }

    /**
     * Returns the pigeon.
     * 
     * @return the pigeon
     */
    public PigeonIMU getPigeon() {
        return pigeon;
    }

    /**
     * Sets the gear shift to low gear.
     */
    public void setLowGear() {
        gearShift.set(DoubleSolenoid.Value.kReverse);
    }

    /**
     * Sets the gear shift to high gear.
     */
    public void setHighGear() {
        gearShift.set(DoubleSolenoid.Value.kForward);
    }

}

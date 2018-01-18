package org.usfirst.frc.team4253.robot2018.components;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
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
    
    private final static double kF = 0.717;
    private final static double kP = 1.5;
    private final static double kI = 0.004;
    private final static double kD = 15;
    private final static int iZone = 50;

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
        int gearShiftForward, int gearShiftReverse,
        int pigeonID
    ) {
        //@formatter:on
        leftMotor = initSide(leftLeaderID, leftFollowerID, false);
        rightMotor = initSide(rightLeaderID, rightFollowerID, true);
        gearShift = new DoubleSolenoid(gearShiftForward, gearShiftReverse);
        
        rightMotor.configPeakOutputForward(0.965, MotorSettings.TIMEOUT);
        rightMotor.configPeakOutputReverse(-0.965, MotorSettings.TIMEOUT);
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
    private WPI_TalonSRX initSide(int leaderID, int followerID, boolean invert) {
        WPI_TalonSRX leader = new WPI_TalonSRX(leaderID);
        WPI_TalonSRX follower = new WPI_TalonSRX(followerID);

        leader.setNeutralMode(NeutralMode.Brake);
        follower.setNeutralMode(NeutralMode.Brake);

        leader.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, MotorSettings.PID_IDX,
            MotorSettings.TIMEOUT);

        follower.set(ControlMode.Follower, leaderID);

        if (invert) {
            leader.setSensorPhase(true);
            leader.setInverted(true);
            follower.setInverted(true);
        } else {
            leader.setSensorPhase(false);
            leader.setInverted(false);
            follower.setInverted(true);
            pigeon = new PigeonIMU(follower);
        }
        
        setPID(leader);
        
        return leader;
    }
    
    /**
     * Sets the PID of the leader Talon SRX
     * 
     * @param leader the leader TalonSRX to change PID settings for
     */
    public void setPID(TalonSRX leader) {
        leader.selectProfileSlot(MotorSettings.PID_SLOT, MotorSettings.PID_IDX);
        leader.config_kF(MotorSettings.PID_SLOT, kF, MotorSettings.TIMEOUT);
        leader.config_kP(MotorSettings.PID_SLOT, kP, MotorSettings.TIMEOUT);
        leader.config_kI(MotorSettings.PID_SLOT, kI, MotorSettings.TIMEOUT);
        leader.config_kD(MotorSettings.PID_SLOT, kD, MotorSettings.TIMEOUT);
        leader.config_IntegralZone(MotorSettings.PID_SLOT, iZone, MotorSettings.TIMEOUT);
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
     * Returns the pigeon
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

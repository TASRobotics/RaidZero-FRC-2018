package org.usfirst.frc.team4253.robot2018.teleop;

import org.usfirst.frc.team4253.robot2018.Utils;
import org.usfirst.frc.team4253.robot2018.components.Drive;
import org.usfirst.frc.team4253.robot2018.components.Lift;

import com.ctre.phoenix.motorcontrol.ControlMode;

public class TeleopDrive {

    private static final double DEFAULTRAMPTIME = 0.0;
    private static final double MAXRAMPTIME = 0.6;
    private Drive drive;
    private Lift lift;

    /**
     * Constructs a TeleopDrive object and assigns the motors from the drive param to
     * differentialDrive.
     * 
     * @param drive the Drive object to use
     */
    public TeleopDrive(Drive drive, Lift lift) {
        this.drive = drive;
        this.lift = lift;
    }

    /**
     * Sets up the TeleopDrive settings.
     * 
     * <p>This should be called when teleop starts.
     */
    public void setup() {
        drive.setLowGear();
        drive.getLeftMotor().configOpenloopRamp(DEFAULTRAMPTIME, 0); // Ramp rate - change second
                                                                     // parameter to not-magic
        drive.getRightMotor().configOpenloopRamp(DEFAULTRAMPTIME, 0);
    }

    /**
     * Drives the robot using tankDrive.
     * 
     * @param leftInput left input of the joystick from -1 to 1
     * @param rightInput right input of the joystick from -1 to 1
     */
    public void drive(double leftInput, double rightInput, boolean defeatRamp) {
        if ((lift.getEncoderPos() > Lift.SWITCH_HEIGHT) && !defeatRamp) {
            drive.getLeftMotor().configOpenloopRamp(Utils.map(lift.getEncoderPos(),
                Lift.SWITCH_HEIGHT, Lift.SCALE_HEIGHT, DEFAULTRAMPTIME, MAXRAMPTIME), 0);
            drive.getRightMotor().configOpenloopRamp(Utils.map(lift.getEncoderPos(),
                Lift.SWITCH_HEIGHT, Lift.SCALE_HEIGHT, DEFAULTRAMPTIME, MAXRAMPTIME), 0);
        } else {
            drive.getLeftMotor().configOpenloopRamp(DEFAULTRAMPTIME, 0); // Ramp rate - change
                                                                         // second
            // parameter to not-magic number
            drive.getRightMotor().configOpenloopRamp(DEFAULTRAMPTIME, 0);

        }

        drive.getLeftMotor().set(ControlMode.PercentOutput, -leftInput);
        drive.getRightMotor().set(ControlMode.PercentOutput, -rightInput);
    }

}

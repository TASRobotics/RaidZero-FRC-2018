package org.usfirst.frc.team4253.robot2018.teleop;

import org.usfirst.frc.team4253.robot2018.components.Drive;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class TeleopDrive {

    private Drive drive;
    private DifferentialDrive differentialDrive;

    /**
     * Constructs a differentialDrive object and assigns up the motors from the drive param.
     * 
     * @param drive the Drive object to use
     */
    public TeleopDrive(Drive drive) {
        this.drive = drive;
        differentialDrive = new DifferentialDrive(drive.getLeftMotor(), drive.getRightMotor());
    }

    /**
     * <p> Sets up the TeleopDrive settings. This should be called when teleopDrive starts.
     */
    public void setup() {
        drive.setLowGear();
    }

    /**
     * drives the robot using tankDrive
     * 
     * @param leftInput the robot's left side speed
     * @param rightInput the robot's right side speed
     */
    public void drive(double leftInput, double rightInput) {
        differentialDrive.tankDrive(-leftInput, -rightInput);
    }

}

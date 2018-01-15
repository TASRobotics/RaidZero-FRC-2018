package org.usfirst.frc.team4253.robot2018.teleop;

import org.usfirst.frc.team4253.robot2018.components.Drive;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class TeleopDrive {

    private Drive drive;
    private DifferentialDrive differentialDrive;

    public TeleopDrive(Drive drive) {
        this.drive = drive;
        differentialDrive = new DifferentialDrive(drive.getLeftMotor(), drive.getRightMotor());
    }

    public void setup() {
        drive.setLowGear();
    }

    public void drive(double leftInput, double rightInput) {
        differentialDrive.tankDrive(-leftInput, -rightInput);
    }

}

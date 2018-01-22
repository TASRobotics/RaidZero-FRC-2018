package org.usfirst.frc.team4253.robot2018.components;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class SolenoidSetting {

    /**
     * value for DoubleSolenoid.Value.kForward.
     * 
     * Intake: open; Drive: high gear; Climb:
     * 
     */
    public static final DoubleSolenoid.Value FORWARD = DoubleSolenoid.Value.kForward;

    /**
     * value for DoubleSolenoid.Value.kReverse.
     * 
     * Intake: close; Drive: low gear; Climb:
     */
    public static final DoubleSolenoid.Value REVERSE = DoubleSolenoid.Value.kForward;

    /**
     * value for DoubleSolenoid.Value.kOff.
     * 
     * Intake: neutral; Drive: null; Climb:
     */
    public static final DoubleSolenoid.Value OFF = DoubleSolenoid.Value.kForward;

}

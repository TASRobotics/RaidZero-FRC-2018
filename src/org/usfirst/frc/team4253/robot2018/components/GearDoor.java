package org.usfirst.frc.team4253.robot2018.components;

import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 * The gear doors.
 */
public class GearDoor {

    private DoubleSolenoid piston;

    /**
     * Constructs a GearDoor object and sets up a DoubleSolenoid on the given channels.
     * 
     * @param forwardChannel the forward channel for the DoubleSolenoid
     * @param reverseChannel the reverse channel for the DoubleSolenoid
     */
    public GearDoor(int forwardChannel, int reverseChannel) {
        piston = new DoubleSolenoid(forwardChannel, reverseChannel);
    }

    /**
     * Opens the gear doors.
     */
    public void open() {
        piston.set(DoubleSolenoid.Value.kForward);
    }

    /**
     * Closes the gear doors.
     */
    public void close() {
        piston.set(DoubleSolenoid.Value.kReverse);
    }

}

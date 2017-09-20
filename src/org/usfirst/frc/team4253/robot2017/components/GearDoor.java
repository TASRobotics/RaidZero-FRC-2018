package org.usfirst.frc.team4253.robot2017.components;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class GearDoor {
    
    private DoubleSolenoid piston;
    
    public GearDoor(int forwardChannel, int reverseChannel) {
        piston = new DoubleSolenoid(forwardChannel, reverseChannel);
    }
    
    public void open() {
        piston.set(DoubleSolenoid.Value.kForward);
    }
    
    public void close() {
        piston.set(DoubleSolenoid.Value.kReverse);
    }
    
}

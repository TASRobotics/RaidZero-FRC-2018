package org.usfirst.frc.team4253.robot2017.components;

import edu.wpi.first.wpilibj.Compressor;

public class Components {

    private static Drive drive;
    private static Climb climb;
    private static GearDoor gearDoor;
    private static Compressor compressor;
    
    public static void initialize() {
        drive = new Drive(1, 2, 3, 4, 5, 6, 0, 1);
        climb = new Climb(15, 13);
        gearDoor = new GearDoor(2, 3);
        compressor = new Compressor();
        compressor.stop();
    }
    
    public static Drive getDrive() {
        return drive;
    }
    
    public static Climb getClimb() {
        return climb;
    }
    
    public static GearDoor getGearDoor() {
        return gearDoor;
    }
    
    public static Compressor getCompressor() {
        return compressor;
    }

}

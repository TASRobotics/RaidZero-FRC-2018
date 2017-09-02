package org.usfirst.frc.team4253.robot2017;

public interface Part {
    
    default void autoStart() {}
    
    default void teleopStart() {}
    
    default void teleopLoop() {}

}

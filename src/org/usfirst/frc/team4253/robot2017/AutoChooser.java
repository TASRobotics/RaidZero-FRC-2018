package org.usfirst.frc.team4253.robot2017;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoChooser {

    private static SendableChooser<Side> sideChooser;
    private static SendableChooser<AutoMode> modeChooser;
    
    public static void initialize() {
        sideChooser = new SendableChooser<>();
        sideChooser.addDefault("Red", Side.Red);
        sideChooser.addObject("Blue", Side.Blue);
        SmartDashboard.putData("Side", sideChooser);
        
        modeChooser = new SendableChooser<>();
        modeChooser.addDefault("Do nothing", AutoMode.DoNothing);
        modeChooser.addObject("Cross the line", AutoMode.CrossLine);
        modeChooser.addObject("Right gear", AutoMode.RightGear);
        modeChooser.addObject("Center gear", AutoMode.CenterGear);
        modeChooser.addObject("Left gear", AutoMode.LeftGear);
        SmartDashboard.putData("Mode", modeChooser);
    }
    
    public static Side getSide() {
        return sideChooser.getSelected();
    }
    
    public static AutoMode getMode() {
        return modeChooser.getSelected();
    }
    
    public static enum Side { Red, Blue }
    
    public static enum AutoMode {
        DoNothing,
        CrossLine,
        RightGear,
        CenterGear,
        LeftGear
    }
    
}

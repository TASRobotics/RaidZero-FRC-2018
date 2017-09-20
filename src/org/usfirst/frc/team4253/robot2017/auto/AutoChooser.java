package org.usfirst.frc.team4253.robot2017.auto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoChooser {

    private static SendableChooser<Color> colorChooser;
    private static SendableChooser<AutoMode> modeChooser;
    
    public static void initialize() {
        colorChooser = new SendableChooser<>();
        switch (DriverStation.getInstance().getAlliance()) {
            case Red:
                colorChooser.addDefault("Red (automatically detected)", Color.Red);
                colorChooser.addObject("Blue", Color.Blue);
                SmartDashboard.putData("Alliance", colorChooser);
                break;
            case Blue:
                colorChooser.addDefault("Blue (automatically detected)", Color.Blue);
                colorChooser.addObject("Red", Color.Red);
                SmartDashboard.putData("Alliance", colorChooser);
                break;
            case Invalid:
                colorChooser.addDefault("Red", Color.Red);
                colorChooser.addObject("Blue", Color.Blue);
                SmartDashboard.putData("Alliance (could not automatically detect)", colorChooser);
                break;
        }
        
        modeChooser = new SendableChooser<>();
        modeChooser.addDefault("Do nothing", AutoMode.DoNothing);
        modeChooser.addObject("Right gear", AutoMode.RightGear);
        modeChooser.addObject("Center gear", AutoMode.CenterGear);
        modeChooser.addObject("Left gear", AutoMode.LeftGear);
        modeChooser.addObject("Cross the line", AutoMode.CrossLine);
        SmartDashboard.putData("Mode", modeChooser);
    }
    
    public static Color getColor() {
        return colorChooser.getSelected();
    }
    
    public static AutoMode getMode() {
        return modeChooser.getSelected();
    }
    
    public static enum Color { Red, Blue }
    
    public static enum AutoMode {
        DoNothing,
        CrossLine,
        LeftGear,
        CenterGear,
        RightGear
    }
    
}

package org.usfirst.frc.team4253.robot2018.auto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * SmartDashboard controls for selecting the correct autonomous routine to run.
 */
public class AutoChooser {

    /**
     * SendableChooser for selecting the alliance the robot is on.
     */
    private static SendableChooser<Color> colorChooser;
    /**
     * SendableChooser for selecting the autonomous mode to run.
     */
    private static SendableChooser<AutoMode> modeChooser;

    /**
     * Attempts to auto-detect the alliance the robot is on, and then places the selection controls
     * onto the SmartDashboard.
     * 
     * <p>If the auto-detection is successful, then the detected alliance will be pre-selected on
     * the autonomous chooser. Otherwise, the default alliance will be red. You can always override
     * the detected alliance. The default autonomous mode is to do nothing.
     */
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
                colorChooser.addDefault("Red (default, could not automatically detect)", Color.Red);
                colorChooser.addObject("Blue", Color.Blue);
                SmartDashboard.putData("Alliance", colorChooser);
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

    /**
     * Returns the selected alliance color on the SmartDashboard.
     * 
     * @return the selected alliance
     */
    public static Color getColor() {
        return colorChooser.getSelected();
    }

    /**
     * Returns the selected autonomous mode on the SmartDashboard.
     * 
     * @return the selected autonomous mode
     */
    public static AutoMode getMode() {
        return modeChooser.getSelected();
    }

    /**
     * An alliance color.
     */
    public static enum Color { Red, Blue }

    /**
     * An autonomous routine.
     */
    public static enum AutoMode {
        DoNothing,
        CrossLine,
        LeftGear,
        CenterGear,
        RightGear
    }

}

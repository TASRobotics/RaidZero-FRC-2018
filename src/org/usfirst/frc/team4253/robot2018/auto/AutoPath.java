package org.usfirst.frc.team4253.robot2018.auto;

/**
 * An autonomous path segment, corresponding to a single GeoGebra file.
 */
public class AutoPath implements Movement {

    private Mode mode;
    private int stage;
    private StartingSide start; // can only be center when stage == 0
    private Side end;
    private boolean reverse;
    private GeoGebraEntry[] motorData;

    /**
     * Constructs an AutoPath object.
     * 
     * @param mode the autonomous routine
     * @param stage the stage of the path (starting from 0)
     * @param start the starting side of the robot
     * @param end the ending side of the robot
     * @param reverse whether the robot drives backwards
     * @param motorData the GeoGebra entries for the motors
     */
    public AutoPath(Mode mode, int stage, StartingSide start, Side end, boolean reverse,
        GeoGebraEntry[] motorData) {
        this.mode = mode;
        this.stage = stage;
        this.start = start;
        this.end = end;
        this.reverse = reverse;
        this.motorData = motorData;
    }

    @Override
    public void run(AutoDrive drive) {
        drive.moveCurve(this);
    }

    @Override
    public double getProgress(AutoDrive drive) {
        return drive.getProgress(this);
    }

    @Override
    public boolean checkFinished(AutoDrive drive) {
        return drive.checkFinished(this);
    }

    @Override
    public void finish(AutoDrive drive) {
        drive.finishPath(this);
    }

    /**
     * Returns the autonomous mode.
     * 
     * @return the mode
     */
    @Override
    public Mode getMode() {
        return mode;
    }

    /**
     * Returns the stage of the path.
     * 
     * @return the stage
     */
    public int getStage() {
        return stage;
    }

    /**
     * Returns the starting side of the path.
     * 
     * @return the starting side
     */
    public StartingSide getStart() {
        return start;
    }

    /**
     * Returns the ending side of the path.
     * 
     * @return the ending side
     */
    public Side getEnd() {
        return end;
    }

    /**
     * Returns whether the robot drives backwards.
     * 
     * @return whether the robot drives backwards
     */
    public boolean getReverse() {
        return reverse;
    }

    /**
     * Returns the GeoGebra entries for the motors.
     * 
     * @return the GeoGebra entries
     */
    public GeoGebraEntry[] getMotorData() {
        return motorData;
    }

}

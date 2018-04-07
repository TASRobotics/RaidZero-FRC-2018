package org.usfirst.frc.team4253.robot2018.auto;

/**
 * Movement in a straight line.
 */
public class Straight implements Movement {

    private Mode mode;
    private int distance;

    /**
     * Constructs a Straight object with the given mode and distance.
     * 
     * @param mode the mode that this segment belongs to
     * @param distance the distance in inches
     */
    public Straight(Mode mode, int distance) {
        this.mode = mode;
        this.distance = distance;
    }

    @Override
    public void run(AutoDrive drive) {
        drive.moveStraight(distance);
    }

    @Override
    public double getProgress(AutoDrive drive) {
        return drive.getStraightProgress(distance);
    }

    @Override
    public boolean checkFinished(AutoDrive drive) {
        return drive.checkStraightFinished(distance);
    }

    @Override
    public void finish(AutoDrive drive) {
        drive.resetEncoders();
    }

    @Override
    public Mode getMode() {
        return mode;
    }

}

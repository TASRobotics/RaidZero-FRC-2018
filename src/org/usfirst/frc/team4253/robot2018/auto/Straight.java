package org.usfirst.frc.team4253.robot2018.auto;

/**
 * Movement in a straight line.
 */
public class Straight implements Movement {

    private Mode mode;
    private int distance;
    private double startingAngle;
    private double theoreticalAngle;

    /**
     * Constructs a Straight object with the given mode and distance.
     * 
     * @param mode the mode that this segment belongs to
     * @param distance the distance in inches
     * @param angle the theoretical starting angle
     */
    public Straight(Mode mode, int distance, int angle) {
        this.mode = mode;
        this.distance = distance;
        theoreticalAngle = angle;
    }

    @Override
    public void startWithAngle(double angle) {
        startingAngle = angle;
    }

    @Override
    public void run(AutoDrive drive) {
        drive.moveStraight(distance, theoreticalAngle);
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

    public double getTheoreticalAngle() {
        return theoreticalAngle;
    }

    @Override
    public Mode getMode() {
        return mode;
    }

}

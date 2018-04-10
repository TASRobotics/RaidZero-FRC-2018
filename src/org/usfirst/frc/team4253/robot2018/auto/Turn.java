package org.usfirst.frc.team4253.robot2018.auto;

/**
 * A turn movement.
 */
public class Turn implements Movement {

    private Mode mode;
    private TurnType type;
    private double angle;
    private double startingAngle;

    /**
     * Constructs a Turn object with the given mode, turn type and angle.
     * 
     * @param mode the mode this turn belongs to
     * @param type the type of turn
     * @param angle the angle in degrees (positive means counterclockwise)
     */
    public Turn(Mode mode, TurnType type, double angle) {
        this.mode = mode;
        this.type = type;
        this.angle = angle;
    }

    public void startWithAngle(double angle) {
        startingAngle = angle;
    }

    @Override
    public void run(AutoDrive drive) {
        drive.turn(type, angle);
    }

    @Override
    public double getProgress(AutoDrive drive) {
        return drive.getTurnProgress(startingAngle, angle);
    }

    @Override
    public boolean checkFinished(AutoDrive drive) {
        return drive.checkTurnFinished(angle);
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

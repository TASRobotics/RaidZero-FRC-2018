package org.usfirst.frc.team4253.robot2018.auto;

/**
 * A turn movement.
 */
public class Turn implements Movement {

    private Mode mode;
    private TurnType type;
    private double angle;
    private double startingAngle;
    private int vel;
    private int acc;

    /**
     * Constructs a Turn object with the given mode, turn type and angle.
     * 
     * @param mode the mode this turn belongs to
     * @param type the type of turn
     * @param angle the angle in degrees (positive means counterclockwise)
     */
    public Turn(Mode mode, TurnType type, double angle) {
        this(mode, type, angle, 0, 0);
    }

    public Turn(Mode mode, TurnType type, double angle, int vel, int acc) {
        this.mode = mode;
        this.type = type;
        this.angle = angle;
        this.vel = vel;
        this.acc = acc;
    }

    public void startWithAngle(double angle) {
        startingAngle = angle;
    }

    @Override
    public void run(AutoDrive drive) {
        if (vel == 0) {
            drive.turn(type, startingAngle, angle);
        } else {
            drive.turn(type, startingAngle, angle, vel, acc);
        }
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

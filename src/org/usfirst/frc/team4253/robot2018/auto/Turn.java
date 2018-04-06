package org.usfirst.frc.team4253.robot2018.auto;


public class Turn implements Movement {

    private double angle;

    public Turn(double angle) {
        this.angle = angle;
    }

    @Override
    public void run(AutoDrive drive) {

    }

    @Override
    public double getProgress(AutoDrive drive) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean checkFinished(AutoDrive drive) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void finish(AutoDrive drive) {
        // TODO Auto-generated method stub

    }

    @Override
    public Mode getMode() {
        // TODO Auto-generated method stub
        return null;
    }

}

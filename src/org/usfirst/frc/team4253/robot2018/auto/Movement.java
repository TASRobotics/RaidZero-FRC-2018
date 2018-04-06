package org.usfirst.frc.team4253.robot2018.auto;

public interface Movement {

    void run(AutoDrive drive);

    double getProgress(AutoDrive drive);

    boolean checkFinished(AutoDrive drive);

    void finish(AutoDrive drive);

    Mode getMode();

}

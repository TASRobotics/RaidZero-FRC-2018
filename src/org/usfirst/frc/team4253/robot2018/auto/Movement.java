package org.usfirst.frc.team4253.robot2018.auto;

/**
 * A movement that can be performed by the drive system.
 */
public interface Movement {

    /**
     * Performs the movement.
     * 
     * @param drive the drive to use for running the movement
     */
    void run(AutoDrive drive);

    /**
     * Returns how much of the movement has been completed.
     * 
     * @param drive the drive to use for checking the progress
     * @return the progress of the movement (0 to 1)
     */
    double getProgress(AutoDrive drive);

    /**
     * Returns whether the movement is finished.
     * 
     * @param drive the drive to use for checking whether it is finished
     * @return whether the movement is finished
     */
    boolean checkFinished(AutoDrive drive);

    /**
     * Resets the drive for the next movement, correcting for errors from this movement.
     * 
     * @param drive the drive to reset
     */
    void finish(AutoDrive drive);

    /**
     * Returns the mode that this movement belongs to.
     * 
     * @return the mode that this movement belongs to
     */
    Mode getMode();

}

package org.usfirst.frc.team4253.robot2018.auto;

/**
 * A type of turn.
 */
public enum TurnType {
    /**
     * Point turn (one side forward and the other side backward).
     */
    PointTurn,
    /**
     * Pivot by moving right side wheels so that the center of rotation is on the left side.
     */
    PivotOnLeft,
    /**
     * Pivot by moving left side wheels so that the center of rotation is on the right side.
     */
    PivotOnRight
}

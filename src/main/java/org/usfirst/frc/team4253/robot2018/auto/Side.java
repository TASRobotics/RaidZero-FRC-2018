package org.usfirst.frc.team4253.robot2018.auto;

/**
 * A side of the field.
 */
public enum Side {

    Left, Right;

    /**
     * Convert this side to a StartingSide value.
     * 
     * @return this side as a StartingSide
     */
    public StartingSide toStartingSide() {
        switch (this) {
            case Left:
                return StartingSide.Left;
            case Right:
                return StartingSide.Right;
            default:
                return null; // this should never happen
        }
    }

    public Side opposite() {
        switch (this) {
            case Left:
                return Side.Right;
            case Right:
                return Side.Left;
            default:
                return null;
        }
    }

}

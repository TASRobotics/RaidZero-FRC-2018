package org.usfirst.frc.team4253.robot2018.auto;

public enum Side {

    Left, Right;

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

}

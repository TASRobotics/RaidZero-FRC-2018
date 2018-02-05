# GeoGebra format

`Path info` | `Path info` | `Angle` | `Percent difference` | (empty) | other cells for calculation
--- | --- | --- | --- | --- | ---
`Stage` | number | number | number | (empty) | ...
`Start` | `Center` or `Left` or `Right` | number | number | (empty) | ...
`End` | `Left` or `Right` | number | number | (empty) | ...
`Direction` | `Forward` or `Backward` | number | number | (empty) | ...
(empty) | (empty) | ... | ... | (empty) | ...

If Stage == 0 then it is the path from start to switch.
If Stage == 1 then it is the path from switch to the place in the middle.
If Stage == 2 then it is the path from the place in the middle to the scale.

If Stage == 0 then Start can be Center, Left, or Right.
Else Start must be Left or Right.

End must always be Left or Right.

# How to use GeoGebra transfer script

## Setup

1. Install node.js (current). https://nodejs.org/en/
2. Run `npm install` in this directory to install dependencies (you must be connected to the internet).
3. To run the program, execute `node main`.

That's all that's required to run the program, but if you want to run the script easily, you can pin it to the taskbar. (Note: Windows only)

3. On your desktop, right click and go to `New > Shortcut`.
4. For the location, put `cmd /k node "path\to\repository\RaidZero-FRC-2018\geogebra-transfer\main.js"` where `path\to\repository` is the path to this repository, and click next.
5. Name the shortcut something like `geogebra transfer`, and click finish.
6. If you want, you can right click on the shortcut and go to `Properties` and change the icon.
7. Right click on the shortcut and click `Pin to taskbar`.

Now you can launch the script from the taskbar.

## Running

Note: Make sure the data is in the new format.

1. Press Ctrl+A or click the top-left square to select all cells.
2. Press Ctrl+C to copy.
3. Make sure you are connected to the robot.
4. Run the script. It should automatically read from the clipboard, convert the data, and upload it to the robot.

# Accessing the RoboRIO filesystem

You can access the RoboRIO filesystem using File Explorer. Type `ftp://roborio-XXXX-frc.local` where `XXXX` is the robot number. The robot program and the geogebra data are stored in `/home/lvuser/`.

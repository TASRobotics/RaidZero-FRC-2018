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

1. Copy the columns you want to transfer in GeoGebra. You can select the columns quickly by clicking and dragging on the column names (the letters) and highlighting the ones you want.
2. Make sure you are connected to the robot.
3. Run the script. It should automatically read from the clipboard, convert the data, and upload it to the robot.

# WolfBot2023

This is the code for TimberWolf robotics FRC 2023.

## Controls & Dashboard

All controls are based on a standard xbox controller.

Please make sure that you use a shuffleboard tile size of 64, rather than the default 128.

### Driver controls

| control           | binding            | description                                    |
| ----------------- | ------------------ | ---------------------------------------------- |
| movement          | Left Analog Stick  |                                                |
| rotation          | Right Analog Stick |                                                |
| slowmode          | Left Bumper        | Reduces speed for precision movement           |
| auto align        | Right Bumper       | Aligns robot with target                       |
| detect april tags | Press X Button     | Sets limelight to detect april tags            |
| detect retro tape | Press Y Button     | Sets limelight to detect retro-reflective tape |
| reset gyro yaw    | Press Start Button | Resets the gyro's yaw readout                  |
| auto balance      | Press A Button     | Auto balances on platform                      |

### Operator Controls

| control    | binding       | description |
| ---------- | ------------- | ----------- |
| open claw  | Hold A Button | Opens Claw  |
| close claw | Hold B Button | Closes Claw |

## Limelight pipeline

Just for explanation, the pipelines for the limelight are created by connecting
to the limelight through the wifi. It was made the way it is so that the tuning
seems to be stable and working well.

### Downloading the pipelines

After tuning everything to be stable, you press the download button for both
pipelines (there are multiple slots for different pipelines under a button
showing a number) - Detecting the retro tape and detecting the april tags. This
will save them to another little button that looks like a download button but
what it really does is show you what you have downloaded. Press this button, and
press on the most recently downloaded ones to bring you to files. From there,
what I did was drag them into the WolfBot2023 folder then into the newly made
limelight_pipeline folder.

### Merging the new pipelines

From there, it should be showing up in VSCode and you can save it, create a new
branch, stage the changes, push for a commit, have a team member review it, and
eventually merge the new branch.

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.configuration;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

/** Add your docs here. */
public class GyroChooser extends SendableChooser<String>{

public static final String NONE = "None";
public static final String NAVX2 = "NavX2";


    public GyroChooser() {
        super();
        setDefaultOption("None", NONE);
        addOption("NavX2", NAVX2);
    }
}

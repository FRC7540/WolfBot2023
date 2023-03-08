// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.configuration;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.commands.OperateCrane;
import frc.robot.commands.OperateCrane.armPreset;

public class PresetChooser extends SendableChooser<Enum<armPreset>>{
    public PresetChooser() {
        super();
        setDefaultOption("Home", OperateCrane.armPreset.HOME);
        addOption("Floor Pickup", OperateCrane.armPreset.FLOOR_PICKUP);
        addOption("Slide Pickup", OperateCrane.armPreset.SHELF_PICKUP);
        addOption("Upper Node", OperateCrane.armPreset.UPPER_NODE);
        addOption("Lower Node", OperateCrane.armPreset.MID_NODE);
        addOption("Hybrid Node", OperateCrane.armPreset.LOWER_NODE);
    }
}

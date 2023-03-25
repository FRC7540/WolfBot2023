// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.suppliers;

import edu.wpi.first.math.controller.PIDController;
import frc.robot.subsystems.CameraSubsystem;

public class AutoAlign {
    private CameraSubsystem cameraSubsystem;
    private PIDController autoAlignController;

    public AutoAlign(CameraSubsystem cameraSubsystem, PIDController autoAlignController) {
        this.cameraSubsystem = cameraSubsystem;
        this.autoAlignController = autoAlignController;
    }

    public Double getOutput() {
        return autoAlignController.calculate(cameraSubsystem.getAngleOffsetX(), 0);
    }
}

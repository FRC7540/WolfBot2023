// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.subsystems.DrivebaseSubsystem;

public class ZeroRotation extends PIDCommand {

  public ZeroRotation(DrivebaseSubsystem drivebaseSubsystem, double setRotation) {
    super(
        // The controller that the command will use
        new PIDController(0.05, 0, 0),
        // This should return the measurement
        () -> drivebaseSubsystem.getYaw() % 360,
        // This should return the setpoint (can also be a constant)
        () -> setRotation % 360,
        // This uses the output
        output -> {
          // Use the output here
          drivebaseSubsystem.Drive(0, 0, output);
        });

    addRequirements(drivebaseSubsystem);
  }
}

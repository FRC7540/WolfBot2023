// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Dashboard;
import frc.robot.subsystems.DrivebaseSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class AutoBalance extends SequentialCommandGroup {
  public AutoBalance(DrivebaseSubsystem drivebaseSubsystem) {
    addCommands(
        new LockedDrive(drivebaseSubsystem, () -> 0, () -> -0.3, () -> true, Dashboard.rotationController)
            .until(() -> drivebaseSubsystem.getPitch() > Dashboard.upperThresholdAngle.get().getFloat()),
        new LockedDrive(drivebaseSubsystem, () -> 0, () -> -0.22, () -> true, Dashboard.rotationController)
            .until(() -> drivebaseSubsystem.getPitch() < Dashboard.finishThresholdAngle.get().getFloat()),
        new LockedDrive(drivebaseSubsystem, () -> 0, () -> 0.22, () -> true, Dashboard.rotationController)
          .withTimeout(0.5));
  }
}

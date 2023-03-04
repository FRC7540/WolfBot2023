// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.subsystems.CraneSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class CraneDown extends SequentialCommandGroup {
  /** Creates a new CraneDown. */
  public CraneDown(CraneSubsystem craneSubsystem) {
                new InstantCommand(() -> craneSubsystem.setMinAngle(Constants.CraneConstants.DEFAULT_MINIMUM_ANGLE_LOW)),
                new InstantCommand(() -> craneSubsystem.ShoulderDown()),
                new WaitCommand(0.25),
                new InstantCommand(() -> craneSubsystem.setAngle(Constants.CraneConstants.AUTO_LOW_ANGLE)),
  }
}

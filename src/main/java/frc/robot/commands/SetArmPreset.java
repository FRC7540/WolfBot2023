// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.Constants;
import frc.robot.subsystems.CraneSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class SetArmPreset extends ConditionalCommand {
  /** Creates a new SetPreset. */
  public SetArmPreset(CraneSubsystem craneSubsystem, boolean shoulder, double angle) {
    super(
        new InstantCommand(() -> craneSubsystem.setAngle(angle), craneSubsystem),
        new ConditionalCommand(
            new SequentialCommandGroup(
                new InstantCommand(() -> craneSubsystem.setAngle(Constants.CraneConstants.Presets.LOWER_ELBOW), craneSubsystem),
                new OperateCrane(craneSubsystem, () -> 0).withTimeout(0.2),
                new InstantCommand(() -> craneSubsystem.ShoulderUp(), craneSubsystem),
                new WaitUntilCommand(() -> craneSubsystem.getShoulderAngle() < 70),
                new InstantCommand(() -> craneSubsystem.setAngle(angle), craneSubsystem)),
            new SequentialCommandGroup(
                new InstantCommand(() -> craneSubsystem.ShoulderDown(), craneSubsystem),
                new WaitUntilCommand(() -> craneSubsystem.getShoulderAngle() > 10),
                new InstantCommand(() -> craneSubsystem.setAngle(angle), craneSubsystem)),
            () -> shoulder),
        () -> craneSubsystem.isArmUp() == shoulder);
  }
}

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ClawSubsystem;

public class ActuateClaw extends CommandBase {
  private ClawSubsystem clawSubsystem;
  private BooleanSupplier openButton;
  private BooleanSupplier closeButton;

  /** Creates a new ActuateClaw. */
  public ActuateClaw(ClawSubsystem clawSubsystem, BooleanSupplier openButton, BooleanSupplier closeButton) {
    this.clawSubsystem = clawSubsystem;
    this.openButton = openButton;
    this.closeButton = closeButton;
    addRequirements(clawSubsystem);
  }

  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    if (openButton.getAsBoolean()) {
      clawSubsystem.actuateClaw(false);
    } else if (closeButton.getAsBoolean()) {
      clawSubsystem.actuateClaw(true);
    } else {
      clawSubsystem.stopClaw();
    }
  }

  @Override
  public void end(boolean interrupted) {
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}

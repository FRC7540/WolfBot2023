// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTableEvent;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.PneumaticsSubsystem;

public class SetCompressor extends CommandBase {
  private PneumaticsSubsystem pneumaticsSubsystem;
  private Boolean enabled;

  /** Creates a new SetCompressor. */
  public SetCompressor(PneumaticsSubsystem pneumaticsSubsystem, NetworkTableEvent event) {
    this.pneumaticsSubsystem = pneumaticsSubsystem;
    enabled = event.valueData.value.getBoolean();
    addRequirements(pneumaticsSubsystem);
  }

  @Override
  public void initialize() {
    if (enabled) {
      pneumaticsSubsystem.EnableCompressor();
    } else {
      pneumaticsSubsystem.DisableCompressor();
    }
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}

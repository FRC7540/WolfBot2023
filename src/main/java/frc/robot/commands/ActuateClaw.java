// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ClawSubsystem;

public class ActuateClaw extends CommandBase {
  private ClawSubsystem clawSubsystem;
  private Enum<directions> direction;

  public enum directions {
    FORWARD, BACKWARD
  }

  public ActuateClaw(ClawSubsystem clawSubsystem, Enum<directions> direction) {
    this.clawSubsystem = clawSubsystem;
    this.direction = direction;
    addRequirements(clawSubsystem);
  }

  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    if (direction == directions.FORWARD) {
      clawSubsystem.CloseClaw();
    } else if (direction == directions.BACKWARD) {
      clawSubsystem.OpenClaw();
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

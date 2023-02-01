// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ClawSubsystem;

public class ActuateClaw extends CommandBase {
  private ClawSubsystem clawSubsystem;
  private Direction direction;

  public enum Direction {
    FORWARD, BACKWARD
  }

  public ActuateClaw(ClawSubsystem clawSubsystem, Direction direction) {
    this.clawSubsystem = clawSubsystem;
    this.direction = direction;
    addRequirements(clawSubsystem);
  }

  @Override
  public void initialize() {
    if (direction == Direction.FORWARD) {
      clawSubsystem.CloseClaw();
    } else if (direction == Direction.BACKWARD) {
      clawSubsystem.OpenClaw();
    }
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}

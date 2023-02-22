// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants;
import frc.robot.Constants.CraneConstants;
import frc.robot.subsystems.CraneSubsystem;

public class OperateCrane extends CommandBase {

  private CraneSubsystem craneSubsystem;
  private DoubleSupplier elbowJoystick;
  private BooleanSupplier shoulderUp;
  private BooleanSupplier shoulderDown;

  public OperateCrane(CraneSubsystem craneSubsystem, DoubleSupplier elbowJoystick, Trigger shoulderUp,
      Trigger shoulderDown) {
    this.craneSubsystem = craneSubsystem;
    this.elbowJoystick = elbowJoystick;
    this.shoulderUp = shoulderUp;
    this.shoulderDown = shoulderDown;

    addRequirements(craneSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (Math.abs(elbowJoystick.getAsDouble()) > Constants.CraneConstants.ELBOW_DEADZONE) {
      craneSubsystem.MoveElbow(elbowJoystick.getAsDouble());
    }

    if (shoulderUp.getAsBoolean()) {
      craneSubsystem.ShoulderUp();
    }

    if (shoulderDown.getAsBoolean()) {
      craneSubsystem.ShoulderDown();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.CraneSubsystem;

public class OperateCrane extends CommandBase {

  private CraneSubsystem craneSubsystem;
  private DoubleSupplier elbowJoystick;
  private BooleanSupplier shoulderUp;
  private BooleanSupplier shoulderDown;
  private double speedMultiplier = Constants.CraneConstants.DEFAULT_SPEED_MULTIPLIER;

  private CraneDown craneDown;
  private CraneUp craneUp;

  private SlewRateLimiter elbowRateLimiter = new SlewRateLimiter(Constants.CraneConstants.DEFAULT_RATE_LIMIT);

  public OperateCrane(CraneSubsystem craneSubsystem, DoubleSupplier elbowJoystick, BooleanSupplier shoulderUp,
      BooleanSupplier shoulderDown) {
    this.craneSubsystem = craneSubsystem;
    this.elbowJoystick = elbowJoystick;
    this.shoulderUp = shoulderUp;
    this.shoulderDown = shoulderDown;

    craneDown = new CraneDown(craneSubsystem);
    craneUp = new CraneUp(craneSubsystem);

    addRequirements(craneSubsystem);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (Math.abs(elbowJoystick.getAsDouble()) > Constants.CraneConstants.ELBOW_DEADZONE) {
      double newAngle = craneSubsystem.getAngleSetPoint() + elbowJoystick.getAsDouble() * speedMultiplier * -1;
      craneSubsystem.setAngle(elbowRateLimiter.calculate(newAngle));
      if (craneSubsystem.getAngleSetPoint() == Constants.CraneConstants.DEFAULT_MINIMUM_ANGLE_LOW
          || craneSubsystem.getAngleSetPoint() == craneSubsystem.getMaxAngle()) {
      }
    }

    craneSubsystem.DriveElbow();

    if (shoulderUp.getAsBoolean() && !craneSubsystem.isArmUp) {
      craneUp.schedule();
    }

    if (shoulderDown.getAsBoolean() && craneSubsystem.isArmUp) {
      craneDown.schedule();
    }
  }

  public void setRateLimit(double rateLimit) {
    elbowRateLimiter = new SlewRateLimiter(rateLimit);
  }

  public void setSpeedMultiplier(double speedMultiplier) {
    this.speedMultiplier = speedMultiplier;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}

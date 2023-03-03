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

  private SlewRateLimiter elbowRateLimiter = new SlewRateLimiter(Constants.CraneConstants.DEFAULT_RATE_LIMIT);

  public OperateCrane(CraneSubsystem craneSubsystem, DoubleSupplier elbowJoystick, BooleanSupplier shoulderUp,
      BooleanSupplier shoulderDown) {
    this.craneSubsystem = craneSubsystem;
    this.elbowJoystick = elbowJoystick;
    this.shoulderUp = shoulderUp;
    this.shoulderDown = shoulderDown;

    addRequirements(craneSubsystem);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (Math.abs(elbowJoystick.getAsDouble()) > Constants.CraneConstants.ELBOW_DEADZONE) {
      double newAngle = craneSubsystem.getAngleSetPoint() + elbowJoystick.getAsDouble() * speedMultiplier * -1;
      craneSubsystem.setAngle(elbowRateLimiter.calculate(newAngle));
    }
    craneSubsystem.DriveElbow();

    if (shoulderUp.getAsBoolean()) {
      craneSubsystem.ShoulderUp();
      if (craneSubsystem.getAngleSetPoint() < Constants.CraneConstants.AUTO_HIGH_ANGLE) {
        craneSubsystem.setAngle(Constants.CraneConstants.AUTO_HIGH_ANGLE);
      }
      craneSubsystem.setMinAngle(Constants.CraneConstants.AUTO_HIGH_ANGLE);
    }

    if (shoulderDown.getAsBoolean()) {
      if (craneSubsystem.getAngleSetPoint() > Constants.CraneConstants.AUTO_LOW_ANGLE) {
        craneSubsystem.setAngle(Constants.CraneConstants.AUTO_LOW_ANGLE);
      }
      craneSubsystem.setMinAngle(Constants.CraneConstants.DEFAULT_MINIMUM_ANGLE);
      craneSubsystem.ShoulderDown();
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

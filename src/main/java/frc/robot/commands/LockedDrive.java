// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.networktables.NetworkTableEvent;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.Dashboard;
import frc.robot.subsystems.DrivebaseSubsystem;

public class LockedDrive extends CommandBase {
  private DrivebaseSubsystem drivebase;
  private DoubleSupplier translateX;
  private DoubleSupplier translateY;
  private BooleanSupplier slowmodeButton;
  private SlewRateLimiter accelLimiterX = new SlewRateLimiter(Constants.DrivebaseConstants.DEFAULT_MAX_ACCELERATION);
  private SlewRateLimiter accelLimiterY = new SlewRateLimiter(Constants.DrivebaseConstants.DEFAULT_MAX_ACCELERATION);

  private float targetHeading;

  private PIDController rotationController;
  /** Creates a new Drive. */
  public LockedDrive(DrivebaseSubsystem drivebase, DoubleSupplier translateX, DoubleSupplier translateY,
      BooleanSupplier slowmodeButton, PIDController rotationController) {
    this.drivebase = drivebase;
    this.translateX = translateX;
    this.translateY = translateY;
    this.slowmodeButton = slowmodeButton;
    this.rotationController = rotationController;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drivebase);
  }

  @Override
  public void initialize() {
    if(Math.abs(drivebase.getYaw()) < 90) {
      targetHeading = 0;
    } else {
      targetHeading = 180;
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // Reversed strafe and rotation axis
    double x = getDeadzone(-translateX.getAsDouble()) * getSpeedMultiplier();
    double y = getDeadzone(translateY.getAsDouble()) * getSpeedMultiplier();
    double error = drivebase.getYaw() - targetHeading;
    if (error < -180) {
      error += 360;
    } else if (error > 180) {
      error -= 360;
    }
    double z = rotationController.calculate(error, 0);

    drivebase.Drive(accelLimiterX.calculate(x), accelLimiterY.calculate(y), -z);
  }

  private double getDeadzone(Double input) {
    if (Math.abs(input) > Dashboard.deadzoneEntry.get().getDouble()) {
      return input;
    } else {
      return 0;
    }
  }

  private double getSpeedMultiplier() {
    boolean slowModeEnabled = !slowmodeButton.getAsBoolean();
    if (slowModeEnabled) {
      return Dashboard.slowmodeSpeed.get().getDouble();
    } else {
      return Dashboard.maxSpeedEntry.get().getDouble();
    }
  }

  public void updateRateLimiters(NetworkTableEvent event) {
    double currentAccelLimit = event.valueData.value.getDouble();
    accelLimiterX = new SlewRateLimiter(currentAccelLimit);
    accelLimiterY = new SlewRateLimiter(currentAccelLimit);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}

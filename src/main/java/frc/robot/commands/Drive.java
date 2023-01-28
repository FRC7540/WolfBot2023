// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.DrivebaseSubsystem;

public class Drive extends CommandBase {
  private DrivebaseSubsystem drivebase;
  private DoubleSupplier translateX;
  private DoubleSupplier translateY;
  private DoubleSupplier rotateZ;
  private BooleanSupplier slowmodeButton;
  private GenericEntry slowmodeSpeed;
  private GenericEntry accelLimitEntry;
  private SlewRateLimiter accelLimiterX = new SlewRateLimiter(Constants.DrivebaseConstants.kDefaultMaxAcceleration);
  private SlewRateLimiter accelLimiterY = new SlewRateLimiter(Constants.DrivebaseConstants.kDefaultMaxAcceleration);
  private double currentAccelLimit = Constants.DrivebaseConstants.kDefaultMaxAcceleration;

  /** Creates a new Drive. */
  public Drive(DrivebaseSubsystem drivebase, DoubleSupplier translateX, DoubleSupplier translateY,
      DoubleSupplier rotateZ, BooleanSupplier slowmodeButton) {
    this.drivebase = drivebase;
    this.translateX = translateX;
    this.translateY = translateY;
    this.rotateZ = rotateZ;
    this.slowmodeButton = slowmodeButton;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drivebase);

    slowmodeSpeed = Shuffleboard.getTab(Constants.ShuffleboardConstants.kTuningTabName)
        .add("Slowmode Speed", 0.5)
        .withWidget(BuiltInWidgets.kNumberSlider)
        .withProperties(Map.of("min", 0, "max", 1))
        .getEntry();

    accelLimitEntry = Shuffleboard.getTab(Constants.ShuffleboardConstants.kTuningTabName)
        .add("Acceleration Limit", Constants.DrivebaseConstants.kDefaultMaxAcceleration)
        .withWidget(BuiltInWidgets.kNumberSlider)
        .withProperties(Map.of("min", 0, "max", 10))
        .getEntry();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double x = translateX.getAsDouble() * getSpeedMultiplier();
    double y = translateY.getAsDouble() * getSpeedMultiplier();
    double z = rotateZ.getAsDouble();

    updateRateLimiters();

    drivebase.Drive(accelLimiterX.calculate(x), accelLimiterY.calculate(y), z);
  }

  private double getSpeedMultiplier() {
    boolean slowModeEnabled = slowmodeButton.getAsBoolean();
    if (slowModeEnabled) {
      return slowmodeSpeed.get().getDouble();
    } else {
      return 1.0;
    }
  }

  private void updateRateLimiters() {
    if (currentAccelLimit != accelLimitEntry.get().getDouble()) {
      currentAccelLimit = accelLimitEntry.get().getDouble();
      accelLimiterX = new SlewRateLimiter(currentAccelLimit);
      accelLimiterY = new SlewRateLimiter(currentAccelLimit);
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}

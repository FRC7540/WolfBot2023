// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.EnumSet;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.networktables.NetworkTableEvent;
import edu.wpi.first.networktables.NetworkTableInstance;
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
  private GenericEntry deadzone;
  private SlewRateLimiter accelLimiterX = new SlewRateLimiter(Constants.DrivebaseConstants.DEFAULT_MAX_ACCELERATION);
  private SlewRateLimiter accelLimiterY = new SlewRateLimiter(Constants.DrivebaseConstants.DEFAULT_MAX_ACCELERATION);

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

    slowmodeSpeed = Shuffleboard.getTab(Constants.ShuffleboardConstants.TUNING_TAB_NAME)
        .add("Slowmode Speed", Constants.DrivebaseConstants.DEFAULT_SLOWMODE_SPEED)
        .withWidget(BuiltInWidgets.kNumberSlider)
        .withProperties(Map.of("min", 0, "max", 1))
        .getEntry();

    accelLimitEntry = Shuffleboard.getTab(Constants.ShuffleboardConstants.TUNING_TAB_NAME)
        .add("Acceleration Limit", Constants.DrivebaseConstants.DEFAULT_MAX_ACCELERATION)
        .withWidget(BuiltInWidgets.kNumberSlider)
        .withProperties(Map.of("min", 0, "max", 10))
        .getEntry();
    NetworkTableInstance inst = NetworkTableInstance.getDefault();
    inst.addListener(accelLimitEntry, EnumSet.of(NetworkTableEvent.Kind.kValueAll),
        (event) -> updateRateLimiters(event));

    deadzone = Shuffleboard.getTab(Constants.ShuffleboardConstants.TUNING_TAB_NAME)
        .add("Deadzone", Constants.DrivebaseConstants.DEFAULT_DEADZONE)
        .withWidget(BuiltInWidgets.kNumberSlider)
        .withProperties(Map.of("min", 0, "max", 1))
        .getEntry();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // Reversed strafe and rotation axis
    double x = getDeadzone(-translateX.getAsDouble()) * getSpeedMultiplier();
    double y = getDeadzone(translateY.getAsDouble()) * getSpeedMultiplier();
    double z = getDeadzone(-rotateZ.getAsDouble());

    drivebase.Drive(accelLimiterX.calculate(x), accelLimiterY.calculate(y), z);
  }

  private double getDeadzone(Double input) {
    if (Math.abs(input) > deadzone.get().getDouble()) {
      return input;
    } else {
      return 0;
    }
  }

  private double getSpeedMultiplier() {
    boolean slowModeEnabled = slowmodeButton.getAsBoolean();
    if (slowModeEnabled) {
      return slowmodeSpeed.get().getDouble();
    } else {
      return 1.0;
    }
  }

  private void updateRateLimiters(NetworkTableEvent event) {
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

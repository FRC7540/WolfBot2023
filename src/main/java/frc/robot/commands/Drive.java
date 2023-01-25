// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

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

    slowmodeSpeed = Shuffleboard.getTab(Constants.ShuffleboardConstants.kGameTabName)
        .add("Slowmode Speed", 0.5)
        .withWidget(BuiltInWidgets.kNumberSlider)
        .withProperties(Map.of("min", 0, "max", 1))
        .getEntry();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double x = translateX.getAsDouble();
    double y = translateY.getAsDouble();
    double z = rotateZ.getAsDouble();
    boolean slowModeEnabled = slowmodeButton.getAsBoolean();

    if (slowModeEnabled) {
      x *= slowmodeSpeed.get().getDouble();
      y *= slowmodeSpeed.get().getDouble();
    }

    drivebase.Drive(x, y, z);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}

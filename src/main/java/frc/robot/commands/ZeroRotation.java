// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Constants;
import frc.robot.subsystems.DrivebaseSubsystem;

public class ZeroRotation extends PIDCommand {
  private ShuffleboardLayout pidLayout = Shuffleboard.getTab(Constants.ShuffleboardConstants.TUNING_TAB_NAME)
      .getLayout("Rotation Zero PID", BuiltInLayouts.kList)
      .withSize(4, 4);

  public ZeroRotation(DrivebaseSubsystem drivebaseSubsystem) {
    super(
        // The controller that the command will use
        new PIDController(0.05, 0, 0),
        // This should return the measurement
        drivebaseSubsystem::getYaw,
        // This should return the setpoint (can also be a constant)
        () -> 0,
        // This uses the output
        output -> {
          // Use the output here
          drivebaseSubsystem.Drive(0, 0, output);
        });
    pidLayout.add("Controller", this.m_controller).withWidget(BuiltInWidgets.kPIDController);

    addRequirements(drivebaseSubsystem);
  }
}

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.Constants.CraneConstants.Presets;
import frc.robot.Dashboard;
import frc.robot.subsystems.CraneSubsystem;

public class OperateCrane extends CommandBase {

  private CraneSubsystem craneSubsystem;
  private DoubleSupplier elbowJoystick;
  private double speedMultiplier = Constants.CraneConstants.DEFAULT_SPEED_MULTIPLIER;

  private CraneDown craneDown;
  private CraneUp craneUp;

  private SlewRateLimiter elbowRateLimiter = new SlewRateLimiter(Constants.CraneConstants.DEFAULT_RATE_LIMIT);

  public enum armPreset {
    HOME,
    FLOOR_PICKUP,
    SHELF_PICKUP,
    UPPER_NODE,
    MID_NODE,
    LOWER_NODE,
    DO_NOTHING
  }

  public OperateCrane(CraneSubsystem craneSubsystem, DoubleSupplier elbowJoystick) {
    this.craneSubsystem = craneSubsystem;
    this.elbowJoystick = elbowJoystick;

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
  }

  public void craneUp() {
    if (!craneSubsystem.isArmUp() && !craneDown.cooldown) {
      craneUp.schedule();
    }
  }

  public void craneDown() {
    if (craneSubsystem.isArmUp() && !craneUp.cooldown) {
      craneDown.schedule();
    }
  } 

  public void setRateLimit(double rateLimit) {
    elbowRateLimiter = new SlewRateLimiter(rateLimit);
  }

  public void setSpeedMultiplier(double speedMultiplier) {
    this.speedMultiplier = speedMultiplier;
  }

  public void recallPreset(Enum<armPreset> preset) {
    boolean shoulder = craneSubsystem.isArmUp();
    double elbow = craneSubsystem.getAngleSetPoint();
    if (preset == armPreset.HOME) {
      shoulder = Presets.HOME_SHOULDER;
      elbow = Dashboard.homeElbowEntry.get().getDouble();
    } else if (preset == armPreset.FLOOR_PICKUP) {
      shoulder = Presets.FLOOR_SHOULDER;
      elbow = Dashboard.floorElbowEntry.get().getDouble();
    } else if (preset == armPreset.SHELF_PICKUP) {
      shoulder = Presets.SHELF_SHOULDER;
      elbow = Dashboard.shelfElbowEntry.get().getDouble();
    } else if (preset == armPreset.UPPER_NODE) {
      shoulder = Presets.UPPER_SHOULDER;
      elbow = Dashboard.upperElbowEntry.get().getDouble();
    } else if (preset == armPreset.MID_NODE) {
      shoulder = Presets.MID_SHOULDER;
      elbow = Dashboard.midElbowEntry.get().getDouble();
    } else if (preset == armPreset.LOWER_NODE) {
      shoulder = Presets.LOWER_SHOULDER;
      elbow = Dashboard.lowerElbowEntry.get().getDouble();
    }

    new SetArmPreset(craneSubsystem, shoulder, elbow).schedule();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}

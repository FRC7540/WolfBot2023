// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.LedConstants;
import frc.robot.subsystems.LedSubsystem;

public class ChangeLeds extends CommandBase {
  private LedSubsystem ledSubsystem;
  private Alliance alliance;
  private int r;
  private int g;
  private int b;

  public ChangeLeds(LedSubsystem ledSubsystem) {
    this.ledSubsystem = ledSubsystem;
    addRequirements(ledSubsystem);
  }

  @Override
  public void initialize() {
    alliance = Alliance.Invalid;
  }

  private void setAlliance() {
    if (alliance == Alliance.Red) {
      r = 255;
      g = 0;
      b = 0;
    } else if (alliance == Alliance.Blue) {
      r = 0;
      g = 0;
      b = 255;
    } else {
      r = LedConstants.TIMBERWOLF_R;
      g = LedConstants.TIMBERWOLF_G;
      b = LedConstants.TIMBERWOLF_B;
    }
    for (int i = 0; i < ledSubsystem.buffer.getLength(); i++) {
      ledSubsystem.buffer.setRGB(i, r, g, b);
    }
  }

  @Override
  public void execute() {
    if (alliance != DriverStation.getAlliance()) {
      alliance = DriverStation.getAlliance();
      setAlliance();
      ledSubsystem.applyBuffer();
    }
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}

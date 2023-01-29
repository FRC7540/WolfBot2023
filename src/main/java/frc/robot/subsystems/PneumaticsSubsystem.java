// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class PneumaticsSubsystem extends SubsystemBase {
  private Compressor compressor = new Compressor(Constants.PneumaticsConstants.PCM_CAN_ID, PneumaticsModuleType.CTREPCM);

  /** Creates a new PneumaticsSubsystem. */
  public PneumaticsSubsystem() {
    CompressorSetup();
  }

  private void CompressorSetup() {
    compressor.enableDigital();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}

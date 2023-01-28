// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PneumaticsSubsystem extends SubsystemBase {

  //Creates a new compressor on the CAN ID 0, as 0 is the default
  private Compressor compressor = new Compressor(PneumaticsModuleType.CTREPCM);

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

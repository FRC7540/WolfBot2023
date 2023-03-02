// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Dashboard;

public class PneumaticsSubsystem extends SubsystemBase {
  private Compressor compressor = new Compressor(Constants.PneumaticsConstants.PCM_CAN_ID,
      PneumaticsModuleType.CTREPCM);

  private AnalogInput pressureSensor = new AnalogInput(Constants.PneumaticsConstants.PRESSURE_SENSOR_ANALOG_PIN);

  /** Creates a new PneumaticsSubsystem. */
  public PneumaticsSubsystem() {
    EnableCompressor();
  }

  public void EnableCompressor() {
    compressor.enableDigital();
  }

  public void DisableCompressor() {
    compressor.disable();
  }

  public double GetPressureReadout() {
    if (pressureSensor.getVoltage() >= 0.5) {
      return (pressureSensor.getVoltage()
          - 0.5) * 37.5;
    } else {
      return 0;
    }
  }

  @Override
  public void periodic() {
    Dashboard.pressureReadout.setDouble(GetPressureReadout());
  }
}

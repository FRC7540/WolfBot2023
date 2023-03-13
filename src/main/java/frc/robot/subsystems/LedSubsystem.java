// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.LedConstants;

public class LedSubsystem extends SubsystemBase {
  public AddressableLEDBuffer buffer = new AddressableLEDBuffer(LedConstants.LED_LENGTH);
  private AddressableLED ziaSymbol = new AddressableLED(LedConstants.LED_PWM_PORT);

  public boolean active;

  public LedSubsystem() {
    ziaSymbol.setLength(buffer.getLength());
    ziaSymbol.setData(buffer);
    ziaSymbol.start();
  }

  public void setLeds(boolean active) {
    if (active) {
      setColor(LedConstants.TIMBERWOLF_R, LedConstants.TIMBERWOLF_G, LedConstants.TIMBERWOLF_B);
    } else {
      setColor(0, 0, 0);
    }
    this.active = active;
  }

  private void setColor(int r, int g, int b) {
    if (active) {
      for (int i = 0; i < buffer.getLength(); i++) {
        buffer.setRGB(i, r, g, b);
      }
      applyBuffer();
    }
  }

  public void applyBuffer() {
    ziaSymbol.setData(buffer);
  }
}

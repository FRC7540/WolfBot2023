// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ClawSubsystem extends SubsystemBase {
  private DoubleSolenoid clawSolenoids = new DoubleSolenoid(
      Constants.PneumaticsConstants.kPcmCanId,
      PneumaticsModuleType.CTREPCM,
      Constants.PneumaticsConstants.kClawSolenoidExtend,
      Constants.PneumaticsConstants.kClawSolenoidRetract);

  /** Creates a new ClawSubsystem. */
  public ClawSubsystem() {
  }

  public void actuateClaw(boolean direction) {
    // Actuates the piston on the claw. If direction true, extends. If false,
    // retracts.
    if (direction) {
      clawSolenoids.set(Value.kForward);
    } else {
      clawSolenoids.set(Value.kReverse);
    }
  }

  public void stopClaw() {
    clawSolenoids.set(Value.kOff);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}

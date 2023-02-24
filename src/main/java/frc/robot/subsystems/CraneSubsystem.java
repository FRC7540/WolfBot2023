// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class CraneSubsystem extends SubsystemBase {
  /** Creates a new CraneSubsystem. */

  private CANSparkMax elbowMotor = new CANSparkMax(Constants.CraneConstants.ELBOW_MOTOR, MotorType.kBrushless);
  private DoubleSolenoid craneSolenoids = new DoubleSolenoid(Constants.PneumaticsConstants.PCM_CAN_ID,
      PneumaticsModuleType.CTREPCM, Constants.PneumaticsConstants.CRANE_SOLENOID_EXTEND, Constants.PneumaticsConstants.CRANE_SOLENOID_RETRACT);

  public CraneSubsystem() {
  }

  public void ShoulderUp() {
    craneSolenoids.set(Value.kForward);
  }

  public void ShoulderDown() {
    craneSolenoids.set(Value.kReverse);
  }

  public void MoveElbow(double elbow) {
    elbowMotor.set(elbow);
  }

}

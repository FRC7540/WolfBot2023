// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class CraneSubsystem extends SubsystemBase {
  /** Creates a new CraneSubsystem. */

  private CANSparkMax elbowMotor = new CANSparkMax(Constants.CraneConstants.ELBOW_MOTOR, MotorType.kBrushless);
  private Solenoid craneSolenoid = new Solenoid(0, PneumaticsModuleType.CTREPCM,
      Constants.PneumaticsConstants.CRANE_SOLENOID);

  public CraneSubsystem() {
  }

  public void ShoulderUp() {
    craneSolenoid.set(true);

  }

  public void ShoulderDown() {
    craneSolenoid.set(false);
  }

  public void MoveElbow(double elbow){
    elbowMotor.set(elbow);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}

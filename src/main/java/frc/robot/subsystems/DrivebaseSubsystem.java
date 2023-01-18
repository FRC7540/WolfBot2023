// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.REVPhysicsSim;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class DrivebaseSubsystem extends SubsystemBase {
  private CANSparkMax m_frontLeftMotor = new CANSparkMax(Constants.DrivebaseConstants.kLeftFrontMotor, MotorType.kBrushed);
  private CANSparkMax m_frontRightMotor = new CANSparkMax(Constants.DrivebaseConstants.kRightFrontMotor, MotorType.kBrushed);
  private CANSparkMax m_rearLeftMotor = new CANSparkMax(Constants.DrivebaseConstants.kLeftRearMotor, MotorType.kBrushed);
  private CANSparkMax m_rearRightMotor = new CANSparkMax(Constants.DrivebaseConstants.kRightRearMotor, MotorType.kBrushed);

  private MecanumDrive m_mecanumDrive = new MecanumDrive(m_frontLeftMotor, m_rearLeftMotor, m_frontRightMotor, m_rearRightMotor);
  
  public DrivebaseSubsystem() {
    REVPhysicsSim physicsSim = REVPhysicsSim.getInstance();
    physicsSim.addSparkMax(m_frontLeftMotor, DCMotor.getCIM(1));
    physicsSim.addSparkMax(m_frontRightMotor, DCMotor.getCIM(1));
    physicsSim.addSparkMax(m_rearLeftMotor, DCMotor.getCIM(1));
    physicsSim.addSparkMax(m_rearRightMotor, DCMotor.getCIM(1));
  }

  public void Drive(double xSpeed, double ySpeed, double zRotation) {
    m_mecanumDrive.driveCartesian(xSpeed, ySpeed, zRotation);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}

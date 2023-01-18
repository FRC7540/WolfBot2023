// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.REVPhysicsSim;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class DrivebaseSubsystem extends SubsystemBase {
  //Motor Setup with Constant IDs
  private CANSparkMax m_frontLeftMotor = new CANSparkMax(Constants.DrivebaseConstants.kLeftFrontMotor, MotorType.kBrushed);
  private CANSparkMax m_frontRightMotor = new CANSparkMax(Constants.DrivebaseConstants.kRightFrontMotor, MotorType.kBrushed);
  private CANSparkMax m_rearLeftMotor = new CANSparkMax(Constants.DrivebaseConstants.kLeftRearMotor, MotorType.kBrushed);
  private CANSparkMax m_rearRightMotor = new CANSparkMax(Constants.DrivebaseConstants.kRightRearMotor, MotorType.kBrushed);

  //MecanumDrive Setup
  private MecanumDrive m_mecanumDrive = new MecanumDrive(m_frontLeftMotor, m_rearLeftMotor, m_frontRightMotor, m_rearRightMotor);
  
  private double speed = Constants.DrivebaseConstants.kDriveSpeed;

  //If true, use Joystick instead of Xbox controller
  private boolean joystick = false;
  
  public DrivebaseSubsystem() {
    //Simulation setup
    REVPhysicsSim physicsSim = REVPhysicsSim.getInstance();
    physicsSim.addSparkMax(m_frontLeftMotor, DCMotor.getCIM(1));
    physicsSim.addSparkMax(m_frontRightMotor, DCMotor.getCIM(1));
    physicsSim.addSparkMax(m_rearLeftMotor, DCMotor.getCIM(1));
    physicsSim.addSparkMax(m_rearRightMotor, DCMotor.getCIM(1));
  }

  //Drives the robot
  public void Drive(double xSpeedXbox, double ySpeedXbox, double zRotationXbox, double xSpeedJoystick, double ySpeedJoystick, double zRotationJoystick) {
    if(joystick)
      m_mecanumDrive.driveCartesian(xSpeedXbox * speed, ySpeedXbox * speed, zRotationXbox * speed);
    else
      m_mecanumDrive.driveCartesian(xSpeedJoystick, ySpeedJoystick, zRotationJoystick);
  }

  //Slowmode setting
  public void setSpeedMode(boolean slowMode) {
    if(slowMode)
      speed = Constants.DrivebaseConstants.kDriveSpeed;
    else
      speed = Constants.DrivebaseConstants.kSlowmodeSpeed;
  }

  public void setInputMode(boolean joystick) {
    this.joystick = joystick;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}

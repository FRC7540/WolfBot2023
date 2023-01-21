// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.REVPhysicsSim;

import edu.wpi.first.hal.SimDouble;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.simulation.SimDeviceSim;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class DrivebaseSubsystem extends SubsystemBase {
  // MecanumDrive Setup
  private MecanumDrive m_mecanumDrive;

  private double speed = 1;

  private CANSparkMax[] motors = new CANSparkMax[4];
  private SimDeviceSim[] simDevices = new SimDeviceSim[4];

  public DrivebaseSubsystem() {
    MotorType motorType = MotorType.kBrushed;
    if (RobotBase.isSimulation()) {
      motorType = MotorType.kBrushless;
    }

    CANSparkMax m_frontLeftMotor = new CANSparkMax(Constants.DrivebaseConstants.kLeftFrontMotor, motorType);
    CANSparkMax m_frontRightMotor = new CANSparkMax(Constants.DrivebaseConstants.kRightFrontMotor, motorType);
    CANSparkMax m_rearLeftMotor = new CANSparkMax(Constants.DrivebaseConstants.kLeftRearMotor, motorType);
    CANSparkMax m_rearRightMotor = new CANSparkMax(Constants.DrivebaseConstants.kRightRearMotor, motorType);
    m_mecanumDrive = new MecanumDrive(m_frontLeftMotor, m_rearLeftMotor, m_frontRightMotor, m_rearRightMotor);

    motors = new CANSparkMax[] {
        m_frontLeftMotor,
        m_frontRightMotor,
        m_rearLeftMotor,
        m_rearRightMotor
    };

    // Simulation setup
    if (RobotBase.isSimulation()) {
      simulationInit();
    }
  }

  private void simulationInit() {
    REVPhysicsSim physicsSim = REVPhysicsSim.getInstance();
    for (int i = 0; i < motors.length; i++) {
      CANSparkMax motor = motors[i];
      simDevices[i] = new SimDeviceSim("SPARK MAX ", i + 1);
      physicsSim.addSparkMax(motor, DCMotor.getNEO(1));
    }
  }

  @Override
  public void simulationPeriodic() {
    for (int i = 0; i < motors.length; i++) {
      CANSparkMax motor = motors[i];
      SimDeviceSim simDevice = simDevices[i];
      updateMotorSimDevice(motor, simDevice);
    }
  }

  private void updateMotorSimDevice(CANSparkMax motor, SimDeviceSim simDevice) {
    SimDouble velocityEntry = simDevice.getDouble("Velocity");
    velocityEntry.set(motor.get());
  }

  // Drives the robot
  public void Drive(double xSpeedXbox, double ySpeedXbox, double zRotationXbox) {
    m_mecanumDrive.driveCartesian(xSpeedXbox * speed, ySpeedXbox * speed, zRotationXbox * speed);
  }

  // Slowmode setting
  public void setSpeedMode(boolean slowMode) {
    if (slowMode)
      speed = 1;
    else
      speed = Constants.DrivebaseConstants.kSlowmodeSpeed;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}

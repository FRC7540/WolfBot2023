// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.EnumSet;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.REVPhysicsSim;

import edu.wpi.first.hal.SimDouble;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableEvent;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.simulation.SimDeviceSim;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.configuration.GyroChooser;

public class DrivebaseSubsystem extends SubsystemBase {
  // MecanumDrive Setup
  private MecanumDrive mecanumDrive;
  private AHRS ahrs = null;

  private CANSparkMax[] motors = new CANSparkMax[4];
  private SimDeviceSim[] simDevices = new SimDeviceSim[4];

  private GenericEntry fieldOrientationEntry;

  public DrivebaseSubsystem() {

    MotorType motorType = MotorType.kBrushless;

    CANSparkMax frontLeftMotor = new CANSparkMax(Constants.DrivebaseConstants.FRONT_LEFT_MOTOR, motorType);
    CANSparkMax frontRightMotor = new CANSparkMax(Constants.DrivebaseConstants.FRONT_RIGHT_MOTOR, motorType);
    CANSparkMax rearLeftMotor = new CANSparkMax(Constants.DrivebaseConstants.REAR_LEFT_MOTOR, motorType);
    CANSparkMax rearRightMotor = new CANSparkMax(Constants.DrivebaseConstants.REAR_RIGHT_MOTOR, motorType);
    mecanumDrive = new MecanumDrive(frontLeftMotor, frontRightMotor, rearLeftMotor, rearRightMotor);
    // Due to issues with the MecanumDrive class, frontRightMotor and rearLeftMotor
    // are switched.

    motors = new CANSparkMax[] {
        frontLeftMotor,
        frontRightMotor,
        rearLeftMotor,
        rearRightMotor
    };

    // Simulation setup
    if (RobotBase.isSimulation()) {
      simulationInit();
    }

    // Creates a shuffleboard combobox widget for gyro selection
    Shuffleboard.getTab(Constants.ShuffleboardConstants.TUNING_TAB_NAME)
        .add("Gyro Selection", new GyroChooser())
        .withWidget(BuiltInWidgets.kComboBoxChooser);

    NetworkTableInstance inst = NetworkTableInstance.getDefault();
    NetworkTable table = inst.getTable("Shuffleboard");
    NetworkTableEntry gyroSelectEntry = table.getEntry("Tuning/Gyro Selection/active");
    inst.addListener(gyroSelectEntry, EnumSet.of(NetworkTableEvent.Kind.kValueAll), this::onUpdateAHRS);

    fieldOrientationEntry = Shuffleboard.getTab(Constants.ShuffleboardConstants.TUNING_TAB_NAME)
        .add("Field Oriented Drive", false)
        .withWidget(BuiltInWidgets.kToggleSwitch)
        .getEntry();
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
    if (isFieldOrientedDriveEnabled()) {
      mecanumDrive.driveCartesian(xSpeedXbox, ySpeedXbox, zRotationXbox, ahrs.getRotation2d());
    } else {
      mecanumDrive.driveCartesian(xSpeedXbox, ySpeedXbox, zRotationXbox);
    }

    // driveStrategy.drive(xSpeedXBox, ySpeedXBox, zRotationXBox);
  }

  private boolean isFieldOrientedDriveEnabled() {
    return fieldOrientationEntry.get().getBoolean() && ahrs != null;
  }

  private void onUpdateAHRS(NetworkTableEvent event) {
    String gyroSelection = event.valueData.value.getString();
    switch (gyroSelection) {
      case GyroChooser.NAVX2:
        setAHRS(new AHRS(SPI.Port.kMXP));
        break;
      case GyroChooser.NONE:
      default:
        setAHRS(null);
        break;
    }
  }

  private void setAHRS(AHRS newAHRS) {
    if (ahrs != null) {
      ahrs.close();
    }
    ahrs = newAHRS;
  }

  public void resetYaw() {
    if (ahrs != null) {
      ahrs.zeroYaw();
      System.out.println("The Gyro has been reset!");
    } else {
      System.out.println("No gyro is configured... Yaw Reset Cancelled.");
    }
  }
}

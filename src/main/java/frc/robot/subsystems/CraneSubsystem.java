// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.REVPhysicsSim;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.hal.SimDouble;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.simulation.DutyCycleEncoderSim;
import edu.wpi.first.wpilibj.simulation.SimDeviceSim;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Dashboard;

public class CraneSubsystem extends SubsystemBase {
  /** Creates a new CraneSubsystem. */

  private CANSparkMax elbowMotor = new CANSparkMax(Constants.CraneConstants.ELBOW_MOTOR, MotorType.kBrushless);
  private DoubleSolenoid craneSolenoids = new DoubleSolenoid(Constants.PneumaticsConstants.PCM_CAN_ID,
      PneumaticsModuleType.CTREPCM, Constants.PneumaticsConstants.CRANE_SOLENOID_EXTEND,
      Constants.PneumaticsConstants.CRANE_SOLENOID_RETRACT);

  public PIDController elbowPidController = new PIDController(0.012, 0.0007, 0.0005);
  private double elbowAngleSetPoint = 0d;
  private double minAngle = Constants.CraneConstants.DEFAULT_MINIMUM_ANGLE_LOW;
  private double maxAngleHigh = Constants.CraneConstants.DEFAULT_MAXIMUM_ANGLE_HIGH;
  private double maxAngleLow = Constants.CraneConstants.DEFAULT_MAXIMUM_ANGLE_LOW;
  public boolean isArmUp = false;

  private DutyCycleEncoder absoluteEncoder = new DutyCycleEncoder(Constants.CraneConstants.ENCODER_PIN_1);

  private SimDeviceSim simDeviceSim;
  private DutyCycleEncoderSim encoderSim;

  private ShuffleboardLayout pidLayout = Shuffleboard.getTab(Constants.ShuffleboardConstants.TUNING_TAB_NAME)
      .getLayout("Arm PID", BuiltInLayouts.kList)
      .withSize(4, 4)
      .withPosition(11, 4);

  public CraneSubsystem() {
    absoluteEncoder.setDutyCycleRange(1.0/1025.0, 1024.0/1025.0);
    absoluteEncoder.setDistancePerRotation(360);
    absoluteEncoder.setPositionOffset(Constants.CraneConstants.DEFAULT_ANGLE_OFFSET);
    elbowAngleSetPoint = absoluteEncoder.getAbsolutePosition();

    pidLayout.add("controller", this.elbowPidController).withWidget(BuiltInWidgets.kPIDController);

    setAngle(getEncoderOutput());

    if (RobotBase.isSimulation()) {
      SimulationInit();
    }
  }

  public void ShoulderUp() {
    isArmUp = true;
    craneSolenoids.set(Value.kForward);
  }

  public void ShoulderDown() {
    isArmUp = false;
    craneSolenoids.set(Value.kReverse);
  }

  public void DriveElbow() {
    double position = getEncoderOutput();
    double output = elbowPidController.calculate(position, elbowAngleSetPoint);
    if (output < -1) {
      output = -1;
    } else if (output > 1) {
      output = 1;
    }
    elbowMotor.set(output);
  }

  public double getEncoderOutput() {
    double output = (absoluteEncoder.getAbsolutePosition() * 360) + Constants.CraneConstants.DEFAULT_ANGLE_OFFSET;
    return output % 360;
  }

  public void setEncoderOffset(double offset) {
    absoluteEncoder.setPositionOffset(offset);
  }

  public void setAngle(double angle) {
    double maxAngle = getMaxAngle();
    if (angle < minAngle) {
      angle = minAngle;
    } else if (angle > maxAngle) {
      angle = maxAngle;
    }
    elbowAngleSetPoint = angle;
  }

  public double getAngleSetPoint() {
    return elbowAngleSetPoint;
  }

  public void setMinAngle(double minAngle) {
    this.minAngle = minAngle;
  }

  public void setMaxAngleHigh(double maxAngle) {
    this.maxAngleHigh = maxAngle;
  }

  public void setMaxAngleLow(double maxAngle) {
    this.maxAngleLow = maxAngle;
  }

  public double getMaxAngle() {
    if (isArmUp) {
      return maxAngleHigh;
    } else {
      return maxAngleLow;
    }
  }

  @Override
  public void periodic() {
    Dashboard.armRotationReadout.setDouble(getEncoderOutput());
    Dashboard.armSetPointReadout.setDouble(getAngleSetPoint());
  }

  private void SimulationInit() {
    REVPhysicsSim physicsSim = REVPhysicsSim.getInstance();
    physicsSim.addSparkMax(elbowMotor, DCMotor.getNEO(1));
    simDeviceSim = new SimDeviceSim("SPARK MAX ", 5);
    encoderSim = new DutyCycleEncoderSim(absoluteEncoder);
  }

  @Override
  public void simulationPeriodic() {
    SimDouble velocityEntry = simDeviceSim.getDouble("Velocity");
    velocityEntry.set(elbowMotor.get());
    encoderSim.set(absoluteEncoder.get() + elbowMotor.get() * 10);
  }
}

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

  public PIDController elbowPidController = new PIDController(0.5, 0, 0);
  private double elbowAngle = 0d;
  private double minAngle = Constants.CraneConstants.DEFAULT_MINIMUM_ANGLE;
  private double maxAngle = Constants.CraneConstants.DEFAULT_MAXIMUM_ANGLE;

  private DutyCycleEncoder absoluteEncoder = new DutyCycleEncoder(Constants.CraneConstants.ENCODER_PIN_1);

  private SimDeviceSim simDeviceSim;
  private DutyCycleEncoderSim encoderSim;

  public CraneSubsystem() {
    absoluteEncoder.setPositionOffset(Constants.CraneConstants.DEFAULT_ANGLE_OFFSET);
    elbowAngle = absoluteEncoder.getAbsolutePosition();

    if (RobotBase.isSimulation()) {
      SimulationInit();
    }
  }

  public void ShoulderUp() {
    craneSolenoids.set(Value.kForward);
  }

  public void ShoulderDown() {
    craneSolenoids.set(Value.kReverse);
  }

  public void DriveElbow() {
    double position = absoluteEncoder.get();
    double output = elbowPidController.calculate(position, elbowAngle);
    if (output < -1) {
      output = -1;
    } else if (output > 1) {
      output = 1;
    }
    elbowMotor.set(output);
  }

  public double getEncoderOutput() {
    return absoluteEncoder.get();
  }

  public void setEncoderOffset(double offset){
    absoluteEncoder.setPositionOffset(offset);
  }

  public void setAngle(double angle) {
    if (angle < minAngle) {
      angle = minAngle;
    } else if (angle > maxAngle) {
      angle = maxAngle;
    }
    elbowAngle = angle;
  }

  public double getAngle() {
    return elbowAngle;
  }

  public void setMinAngle(double minAngle) {
    this.minAngle = minAngle;
  }

  public void setMaxAngle(double maxAngle) {
    this.maxAngle = maxAngle;
  }

  @Override
  public void periodic() {
    Dashboard.armRotationReadout.setDouble(absoluteEncoder.getAbsolutePosition());
    Dashboard.armSetPointReadout.setDouble(getAngle());
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
    encoderSim.set(absoluteEncoder.get() + elbowMotor.get() * 0.5);
  }
}

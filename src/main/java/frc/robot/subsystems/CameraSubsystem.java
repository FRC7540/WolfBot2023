// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class CameraSubsystem extends SubsystemBase {
  private NetworkTable limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
  private NetworkTableEntry pipelineEntry = limelightTable.getEntry("pipeline");

  /** Creates a new CameraSubsystem. */
  public CameraSubsystem() {
  }

  public double getTargetArea() {
    return limelightTable.getEntry("ta").getDouble(0);
  }

  public double getAngleOffsetX() {
    return limelightTable.getEntry("tx").getDouble(0);
  }

  public double getAngleOffsetY() {
    return limelightTable.getEntry("ty").getDouble(0);
  }

  public boolean isTargetVisible() {
    double targetVisibleDouble = limelightTable.getEntry("tv").getDouble(0);
    return targetVisibleDouble != 0;
  }

  public enum Pipeline {
    APRIL_TAG(0),
    RETRO_TAPE(1);

    public final int pipelineId;

    private Pipeline(int pipelineId) {
      this.pipelineId = pipelineId;
    }
  }

  public Pipeline getPipeline() {
    double pipelineIdDouble = pipelineEntry.getDouble(0);
    int pipelineIdInt = (int) Math.round(pipelineIdDouble);
    if (pipelineIdInt == 0) {
      return Pipeline.APRIL_TAG;
    } else {
      return Pipeline.RETRO_TAPE;
    }
  }

  public void setPipeline(Pipeline newPipeline) {
    int pipelineIdInt = newPipeline.pipelineId;
    pipelineEntry.setDouble(pipelineIdInt);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}

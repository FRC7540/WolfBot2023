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
  private NetworkTableEntry targetAreaEntry = limelightTable.getEntry("ta");
  private NetworkTableEntry targetXOffsetEntry = limelightTable.getEntry("tx");
  private NetworkTableEntry targetYOffsetEntry = limelightTable.getEntry("ty");
  private NetworkTableEntry targetVisibleEntry = limelightTable.getEntry("tv");

  /** Creates a new CameraSubsystem. */
  public CameraSubsystem() {
    targetAreaEntry.setDouble(0);
    targetXOffsetEntry.setDouble(0);
    targetYOffsetEntry.setDouble(0);
    targetVisibleEntry.setDouble(0);
    pipelineEntry.setDouble(0);
  }

  public double getTargetArea() {
    return targetAreaEntry.getDouble(0);
  }

  public double getAngleOffsetX() {
    return targetXOffsetEntry.getDouble(0);
  }

  public double getAngleOffsetY() {
    return targetYOffsetEntry.getDouble(0);
  }

  public boolean isTargetVisible() {
    double targetVisibleDouble = targetVisibleEntry.getDouble(0);
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

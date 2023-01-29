// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.HttpCamera;
import edu.wpi.first.cscore.MjpegServer;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class CameraSubsystem extends SubsystemBase {
  private NetworkTable table = NetworkTableInstance.getDefault().getTable("SmartDashboard");

  /** Creates a new CameraSubsystem. */
  public CameraSubsystem() {
    widgetSetup();
  }

  private void widgetSetup() {
    String limelightUrl = table.getEntry("limelight_Stream").getString("http://10.75.40.1:5800");
    HttpCamera video = new HttpCamera(Constants.CameraConstants.LIMELIGHT_NAME, limelightUrl);
    MjpegServer server = CameraServer.addSwitchedCamera("Limelight");
    server.setSource(video);

    Shuffleboard.getTab(Constants.ShuffleboardConstants.GAME_TAB_NAME)
        .add(server.getSource())
        .withWidget(BuiltInWidgets.kCameraStream);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}

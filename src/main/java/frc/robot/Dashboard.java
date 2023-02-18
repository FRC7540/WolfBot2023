// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.Map;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.HttpCamera;
import edu.wpi.first.cscore.MjpegServer;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.DrivebaseSubsystem;

public class Dashboard extends SubsystemBase {

        private NetworkTable smartDashboard = NetworkTableInstance.getDefault().getTable("SmartDashboard");
        public static NetworkTableInstance networkTableInstance = NetworkTableInstance.getDefault();
        public static NetworkTable table = networkTableInstance.getTable("Shuffleboard");

        public static GenericEntry fieldOrientationEntry;
        public static GenericEntry compressorEnabled;
        public static GenericEntry slowmodeSpeed;
        public static GenericEntry accelLimitEntry;
        public static GenericEntry deadzone;
        public static NetworkTableEntry gyroSelectEntry = table.getEntry("Tuning/Gyro Selection/active");
        private DrivebaseSubsystem drivebaseSubsystem;

        /** Creates a new ShuffleboardSubsystem. */
        public Dashboard(DrivebaseSubsystem drivebaseSubsystem) {
                this.drivebaseSubsystem = drivebaseSubsystem;
        }

        public void ShuffleboardSetup() {
                // Drive Shuffleboard Widgets
                fieldOrientationEntry = Shuffleboard.getTab(Constants.ShuffleboardConstants.TUNING_TAB_NAME)
                                .add("Field Oriented Drive", false)
                                .withWidget(BuiltInWidgets.kToggleSwitch)
                                .getEntry();

                slowmodeSpeed = Shuffleboard.getTab(Constants.ShuffleboardConstants.TUNING_TAB_NAME)
                                .add("Slowmode Speed", Constants.DrivebaseConstants.DEFAULT_SLOWMODE_SPEED)
                                .withWidget(BuiltInWidgets.kNumberSlider)
                                .withProperties(Map.of("min", 0, "max", 1))
                                .getEntry();

                accelLimitEntry = Shuffleboard.getTab(Constants.ShuffleboardConstants.TUNING_TAB_NAME)
                                .add("Acceleration Limit", Constants.DrivebaseConstants.DEFAULT_MAX_ACCELERATION)
                                .withWidget(BuiltInWidgets.kNumberSlider)
                                .withProperties(Map.of("min", 0, "max", 10))
                                .getEntry();

                deadzone = Shuffleboard.getTab(Constants.ShuffleboardConstants.TUNING_TAB_NAME)
                                .add("Deadzone", Constants.DrivebaseConstants.DEFAULT_DEADZONE)
                                .withWidget(BuiltInWidgets.kNumberSlider)
                                .withProperties(Map.of("min", 0, "max", 1))
                                .getEntry();

                Shuffleboard.getTab(Constants.ShuffleboardConstants.GAME_TAB_NAME)
                                .add("Drivebase", drivebaseSubsystem.mecanumDrive)
                                .withWidget(BuiltInWidgets.kMecanumDrive);

                Shuffleboard.getTab(Constants.ShuffleboardConstants.GAME_TAB_NAME)
                                .add("Gyro", drivebaseSubsystem.ahrs)
                                .withWidget(BuiltInWidgets.kGyro);

                // Pneumatics Widgets
                compressorEnabled = Shuffleboard.getTab(Constants.ShuffleboardConstants.GAME_TAB_NAME)
                                .add("Enable Compressor", true)
                                .withWidget(BuiltInWidgets.kToggleSwitch)
                                .getEntry();

                // Limelight widget
                String limelightUrl = smartDashboard.getEntry("limelight_Stream").getString("http://10.75.40.1:5800");
                HttpCamera video = new HttpCamera(Constants.CameraConstants.LIMELIGHT_NAME, limelightUrl);
                MjpegServer server = CameraServer.addSwitchedCamera("Limelight");
                server.setSource(video);
                Shuffleboard.getTab(Constants.ShuffleboardConstants.GAME_TAB_NAME)
                                .add(server.getSource())
                                .withWidget(BuiltInWidgets.kCameraStream);

        }
}
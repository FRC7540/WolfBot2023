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
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.DrivebaseSubsystem;

public class Dashboard extends SubsystemBase {

        private NetworkTable smartDashboard = NetworkTableInstance.getDefault().getTable("SmartDashboard");
        public static NetworkTableInstance networkTableInstance = NetworkTableInstance.getDefault();
        public static NetworkTable table = networkTableInstance.getTable("Shuffleboard");

        public static GenericEntry fieldOrientationEntry;
        public static GenericEntry compressorEnabled;
        public static GenericEntry armRotationReadout;
        public static GenericEntry slowmodeSpeed;
        public static GenericEntry accelLimitEntry;
        public static GenericEntry deadzoneEntry;
        public static GenericEntry maxSpeedEntry;
        public static GenericEntry maxRotationSpeedEntry;
        public static GenericEntry armEncoderOffset;
        public static GenericEntry armMinimumAngle;
        public static GenericEntry armMaximumAngleHigh;
        public static GenericEntry armMaximumAngleLow;
        public static GenericEntry armRateLimit;
        public static GenericEntry armSpeedMultiplier;
        public static GenericEntry armSetPointReadout;
        public static NetworkTableEntry gyroSelectEntry = table.getEntry("Tuning/Gyro Selection/active");
        private DrivebaseSubsystem drivebaseSubsystem;

        private ShuffleboardLayout driveTuningLayout;
        private ShuffleboardLayout telemetryLayout;
        private ShuffleboardLayout craneTuningLayout;

        /** Creates a new ShuffleboardSubsystem. */
        public Dashboard(DrivebaseSubsystem drivebaseSubsystem) {
                this.drivebaseSubsystem = drivebaseSubsystem;
        }

        public void ShuffleboardSetup() {
                // Drive Shuffleboard Widgets

                craneTuningLayout = Shuffleboard.getTab(Constants.ShuffleboardConstants.TUNING_TAB_NAME)
                                .getLayout("Crane Tuning", BuiltInLayouts.kList)
                                .withSize(4, 7);

                armEncoderOffset = craneTuningLayout
                                .add("Encoder Offset", Constants.CraneConstants.DEFAULT_ANGLE_OFFSET)
                                .withWidget(BuiltInWidgets.kNumberSlider)
                                .withProperties(Map.of("min", -360, "max", 360))
                                .getEntry();

                armMinimumAngle = craneTuningLayout.add("Min Angle", Constants.CraneConstants.DEFAULT_MINIMUM_ANGLE)
                                .withWidget(BuiltInWidgets.kNumberSlider)
                                .withProperties(Map.of("min", -360, "max", 360))
                                .getEntry();

                armMaximumAngleHigh = craneTuningLayout
                                .add("Max Angle High", Constants.CraneConstants.DEFAULT_MAXIMUM_ANGLE_HIGH)
                                .withWidget(BuiltInWidgets.kNumberSlider)
                                .withProperties(Map.of("min", -360, "max", 360))
                                .getEntry();

                armMaximumAngleLow = craneTuningLayout
                                .add("Max Angle Low", Constants.CraneConstants.DEFAULT_MAXIMUM_ANGLE_LOW)
                                .withWidget(BuiltInWidgets.kNumberSlider)
                                .withProperties(Map.of("min", -360, "max", 360))
                                .getEntry();

                armRateLimit = craneTuningLayout.add("Rate Limit", Constants.CraneConstants.DEFAULT_RATE_LIMIT)
                                .withWidget(BuiltInWidgets.kNumberSlider)
                                .withProperties(Map.of("min", 0, "max", 180))
                                .getEntry();

                armSpeedMultiplier = craneTuningLayout
                                .add("Speed Multiplier", Constants.CraneConstants.DEFAULT_SPEED_MULTIPLIER)
                                .withWidget(BuiltInWidgets.kNumberSlider)
                                .withProperties(Map.of("min", 0, "max", 50))
                                .getEntry();

                driveTuningLayout = Shuffleboard.getTab(Constants.ShuffleboardConstants.TUNING_TAB_NAME)
                                .getLayout("Drive Tuning", BuiltInLayouts.kList)
                                .withSize(4, 7);

                slowmodeSpeed = driveTuningLayout
                                .add("Slowmode Speed", Constants.DrivebaseConstants.DEFAULT_SLOWMODE_SPEED)
                                .withWidget(BuiltInWidgets.kNumberSlider)
                                .withProperties(Map.of("min", 0, "max", 1))
                                .getEntry();

                accelLimitEntry = driveTuningLayout
                                .add("Acceleration Limit", Constants.DrivebaseConstants.DEFAULT_MAX_ACCELERATION)
                                .withWidget(BuiltInWidgets.kNumberSlider)
                                .withProperties(Map.of("min", 0, "max", 10))
                                .getEntry();

                deadzoneEntry = driveTuningLayout
                                .add("Deadzone", Constants.DrivebaseConstants.DEFAULT_DEADZONE)
                                .withWidget(BuiltInWidgets.kNumberSlider)
                                .withProperties(Map.of("min", 0, "max", 1))
                                .getEntry();

                maxSpeedEntry = driveTuningLayout
                                .add("Speed Limit", Constants.DrivebaseConstants.DEFAULT_SPEED_LIMIT)
                                .withWidget(BuiltInWidgets.kNumberSlider)
                                .withProperties(Map.of("min", 0, "max", 1))
                                .getEntry();

                maxRotationSpeedEntry = driveTuningLayout
                                .add("Rotation Speed Limit", Constants.DrivebaseConstants.DEFAULT_ROTATION_SPEED_LIMIT)
                                .withWidget(BuiltInWidgets.kNumberSlider)
                                .withProperties(Map.of("min", 0, "max", 1))
                                .getEntry();

                telemetryLayout = Shuffleboard.getTab(Constants.ShuffleboardConstants.GAME_TAB_NAME)
                                .getLayout("Telemetry", BuiltInLayouts.kGrid)
                                .withProperties(Map.of("Number of columns", 2, "Number of rows", 1))
                                .withSize(9, 5)
                                .withPosition(6, 0);

                telemetryLayout.add("Gyro", drivebaseSubsystem.ahrs)
                                .withWidget(BuiltInWidgets.kGyro);

                telemetryLayout.add("Drivebase", drivebaseSubsystem.mecanumDrive)
                                .withWidget(BuiltInWidgets.kMecanumDrive);

                fieldOrientationEntry = Shuffleboard.getTab(Constants.ShuffleboardConstants.GAME_TAB_NAME)
                                .add("Field Oriented Drive", false)
                                .withWidget(BuiltInWidgets.kToggleSwitch)
                                .withSize(2, 1)
                                .withPosition(0, 4)
                                .getEntry();

                // Pneumatics Widgets
                compressorEnabled = Shuffleboard.getTab(Constants.ShuffleboardConstants.GAME_TAB_NAME)
                                .add("Enable Compressor", true)
                                .withWidget(BuiltInWidgets.kToggleSwitch)
                                .withSize(2, 1)
                                .withPosition(2, 4)
                                .getEntry();

                armRotationReadout = Shuffleboard.getTab(Constants.ShuffleboardConstants.GAME_TAB_NAME)
                                .add("Elbow Rotation", 0)
                                .withWidget(BuiltInWidgets.kDial)
                                .withProperties(Map.of("min", -360, "max", 360))
                                .withPosition(0, 5)
                                .getEntry();

                armSetPointReadout = Shuffleboard.getTab(Constants.ShuffleboardConstants.GAME_TAB_NAME)
                                .add("Set Point", 0)
                                .withWidget(BuiltInWidgets.kDial)
                                .withProperties(Map.of("min", -360, "max", 360))
                                .withPosition(3, 5)
                                .getEntry();

                // Limelight widget
                String limelightUrl = smartDashboard.getEntry("limelight_Stream")
                                .getString("http://limelight-twolf.local:5800");
                HttpCamera video = new HttpCamera(Constants.CameraConstants.LIMELIGHT_NAME, limelightUrl);
                MjpegServer server = CameraServer.addSwitchedCamera("Limelight");
                server.setSource(video);
                Shuffleboard.getTab(Constants.ShuffleboardConstants.GAME_TAB_NAME)
                                .add(server.getSource())
                                .withWidget(BuiltInWidgets.kCameraStream)
                                .withPosition(0, 0);
        }
}
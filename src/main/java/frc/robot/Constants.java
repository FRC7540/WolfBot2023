// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean
 * constants. This class should not be used for any other purpose. All constants
 * should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static class OperatorConstants {
    public static final int kDriverXboxControllerPort = 0;
    public static final int kDriverJoystickPort = 1;
  }

  public static class DrivebaseConstants {
    // Motor CAN IDs
    public static final int kLeftFrontMotor = 1;
    public static final int kRightFrontMotor = 2;
    public static final int kLeftRearMotor = 3;
    public static final int kRightRearMotor = 4;

    // Speed Calibration
    public static final double kSlowmodeSpeed = 0.5;
    public static final double kDefaultMaxAcceleration = 5.0;
  }

  public static class ShuffleboardConstants {
    public static final String kGameTabName = "Game";
    public static final String kTuningTabName = "Tuning";
  }

  public static class CameraConstants {
    public static final String kLimelightName = "Limelight";
  }

  public static class PneumaticsConstants {
    // CAN ID of CTRE PCM, default 0
    public static final int kPcmCanId = 0;
  }
}

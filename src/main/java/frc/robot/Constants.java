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
    public static final int DRIVER_XBOX_CONTROLLER_PORT = 0;
    public static final int OPERATOR_XBOX_CONTROLLER_PORT = 1;
  }

  public static class DrivebaseConstants {
    // Motor CAN IDs
    public static final int FRONT_LEFT_MOTOR = 2;
    public static final int FRONT_RIGHT_MOTOR = 1;
    public static final int REAR_LEFT_MOTOR = 4;
    public static final int REAR_RIGHT_MOTOR = 3;

    // Speed Calibration
    public static final double DEFAULT_SLOWMODE_SPEED = 0.34;
    public static final double DEFAULT_MAX_ACCELERATION = 5.0;
    public static final double DEFAULT_DEADZONE = 0.2;
    public static final double DEFAULT_SPEED_LIMIT = 0.8;
    public static final double DEFAULT_ROTATION_SPEED_LIMIT = 0.6;
  }

  public static class ShuffleboardConstants {
    public static final String GAME_TAB_NAME = "Game";
    public static final String TUNING_TAB_NAME = "Tuning";
  }

  public static class CameraConstants {
    public static final String LIMELIGHT_NAME = "Limelight";
  }

  public static class PneumaticsConstants {
    // CAN ID of CTRE PCM, default 0
    public static final int PCM_CAN_ID = 0;

    // PCM Channels for solenoids
    public static final int CLAW_SOLENOID_EXTEND = 0;
    public static final int CLAW_SOLENOID_RETRACT = 1;
  }
}

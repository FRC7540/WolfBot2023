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

    public static final double DEFAULT_DEBOUNCE_DELAY = 0.1;

    public static final double DEFAULT_ALIGNMENT_OVERRIDE_MULTIPLIER = 0.3;
  }

  public static class DrivebaseConstants {
    // Motor CAN IDs
    public static final int FRONT_LEFT_MOTOR = 2;
    public static final int FRONT_RIGHT_MOTOR = 1;
    public static final int REAR_LEFT_MOTOR = 4;
    public static final int REAR_RIGHT_MOTOR = 3;

    // Speed Calibration
    public static final double DEFAULT_SLOWMODE_SPEED = 0.2;
    public static final double DEFAULT_MAX_ACCELERATION = 5.0;
    public static final double DEFAULT_DEADZONE = 0.2;
    public static final double DEFAULT_SPEED_LIMIT = 1;
    public static final double DEFAULT_ROTATION_SPEED_LIMIT = 0.6;
  }

  public static class ShuffleboardConstants {
    public static final String GAME_TAB_NAME = "Game";
    public static final String TUNING_TAB_NAME = "Tuning";
    public static final String PRESET_TAB_NAME = "Presets";
    public static final String AUTO_TUNING_TAB_NAME = "Auto Tuning";
  }

  public static class CameraConstants {
    public static final String LIMELIGHT_NAME = "Limelight";
  }

  public static class PneumaticsConstants {
    // CAN ID of CTRE PCM, default 0
    public static final int PCM_CAN_ID = 0;

    public static final int PRESSURE_SENSOR_ANALOG_PIN = 0;

    // PCM Channels for solenoids
    public static final int CLAW_SOLENOID_EXTEND = 2;
    public static final int CLAW_SOLENOID_RETRACT = 3;

    public static final int CRANE_SOLENOID_EXTEND = 0;
    public static final int CRANE_SOLENOID_RETRACT = 1;
  }

  public static class CraneConstants {
    public static final int ELBOW_MOTOR = 5;
    public static final double ELBOW_DEADZONE = 0.2;

    public static final int ELBOW_ENCODER_PIN = 0;
    public static final int SHOULDER_ENCODER_PIN = 1;

    public static final double SHOULDER_DOWN_ANGLE = 5;

    public static final double DEFAULT_MINIMUM_ANGLE_LOW = 15.0;
    public static final double DEFAULT_MINIMUM_ANGLE_HIGH = 145;
    public static final double DEFAULT_MAXIMUM_ANGLE_HIGH = 270.0;
    public static final double DEFAULT_MAXIMUM_ANGLE_LOW = 107.0;
    public static final double DEFAULT_ANGLE_OFFSET = 223.0;
    public static final double DEFAULT_RATE_LIMIT = 80;
    public static final double AUTO_HIGH_ANGLE = 200;
    public static final double AUTO_LOW_ANGLE = 90;

    public static final double DEFAULT_SPEED_MULTIPLIER = 2;

    public static class Presets {
      // Home Preset
      public static final boolean HOME_SHOULDER = false;
      public static final double HOME_ELBOW = 15;

      // Floor Pickup Preset
      public static final boolean FLOOR_SHOULDER = false;
      public static final double FLOOR_ELBOW = 110;

      // Slide Pickup Preset
      public static final boolean SHELF_SHOULDER = true;
      public static final double SHELF_ELBOW = 202;

      // Upper Node Preset
      public static final boolean UPPER_SHOULDER = true;
      public static final double UPPER_ELBOW = 165;

      // Lower Node Preset
      public static final boolean MID_SHOULDER = true;
      public static final double MID_ELBOW = 200;

      // Hybrid Node Preset
      public static final boolean LOWER_SHOULDER = false;
      public static final double LOWER_ELBOW = 90;
    }

  }

  public static class Autonomous {
    public static final double DRIVE_BACKWARD_DISTANCE = -0.75;
    public static final double DRIVE_FORWARD_DISTANCE = 4;
    public static final float BALANCE_TRIGGER_ANGLE = 8;
    public static final float BALANCE_GREATER_THRESHOLD_ANGLE = 13;
    public static final float BALANCE_FINISH_THRESHOLD_ANGLE = 10;
  }

  public static class LedConstants {
    public static final int LED_PWM_PORT = 1;
    public static final int LED_LENGTH = 47;

    public static final int TIMBERWOLF_R = 50;
    public static final int TIMBERWOLF_G = 50;
    public static final int TIMBERWOLF_B = 255;
  }
}

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
    public static final double DEFAULT_SLOWMODE_SPEED = 0.2;
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

    public static final int ENCODER_PIN_1 = 0;
    public static final int ENOCDER_PIN_2 = 1;
    public static final int ENOCDER_PIN_3 = 2;
    public static final int ENOCDER_PIN_4 = 3;
    public static final int LIMIT_SWITCH = 4;

    public static final double DEFAULT_MINIMUM_ANGLE_LOW = 15.0;
    public static final double DEFAULT_MINIMUM_ANGLE_HIGH = 145;
    public static final double DEFAULT_MAXIMUM_ANGLE_HIGH = 270.0;
    public static final double DEFAULT_MAXIMUM_ANGLE_LOW = 110.0;
    public static final double DEFAULT_ANGLE_OFFSET = 220.0;
    public static final double DEFAULT_RATE_LIMIT = 80;
    public static final double AUTO_HIGH_ANGLE = 170;
    public static final double AUTO_LOW_ANGLE = 100;
    
    public static final double DEFAULT_SPEED_MULTIPLIER = 1;
  }
}

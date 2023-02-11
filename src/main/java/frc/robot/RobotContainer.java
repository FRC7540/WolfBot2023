// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.ActuateClaw;
import frc.robot.commands.ActuateClaw.Direction;
import frc.robot.commands.Drive;
import frc.robot.commands.SetCompressor;
import frc.robot.commands.SetVisionPipeline;
import frc.robot.subsystems.CameraSubsystem;
import frc.robot.subsystems.ClawSubsystem;
import frc.robot.subsystems.DrivebaseSubsystem;
import frc.robot.subsystems.PneumaticsSubsystem;
import frc.robot.subsystems.CameraSubsystem.Pipeline;

import java.util.EnumSet;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.networktables.NetworkTableEvent;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // Subsystem Instantiation
  private final DrivebaseSubsystem drivebaseSubsystem = new DrivebaseSubsystem();
  private final CameraSubsystem cameraSubsystem = new CameraSubsystem();
  private final PneumaticsSubsystem pneumaticsSubsystem = new PneumaticsSubsystem();
  private final ClawSubsystem clawSubsystem = new ClawSubsystem();

  private final NetworkTableInstance networkTableInstance = NetworkTableInstance.getDefault();

  // Controller Setup
  private final CommandXboxController driverXboxController = new CommandXboxController(
      OperatorConstants.DRIVER_XBOX_CONTROLLER_PORT);
  private final CommandXboxController operatorXboxController = new CommandXboxController(
      OperatorConstants.OPERATOR_XBOX_CONTROLLER_PORT);

  // Shuffleboard Entries
  private GenericEntry compressorEnabled;

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    configureBindings();
    configureDefaultCommands();
    ShuffleboardSetup();
  }

  private void configureDefaultCommands() {
    // Drivebase default command
    Trigger leftBumper = driverXboxController.leftBumper();
    Drive driveCommand = new Drive(drivebaseSubsystem, driverXboxController::getLeftX,
        driverXboxController::getLeftY, driverXboxController::getRightX, leftBumper::getAsBoolean);
    drivebaseSubsystem.setDefaultCommand(driveCommand);

    // Claw default command
    clawSubsystem.setDefaultCommand(new InstantCommand(() -> clawSubsystem.stopClaw(), clawSubsystem));
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be
   * created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with
   * an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for
   * {@link
   * CommandXboxController
   * Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or
   * {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    // Operator Controller Bindings
    operatorXboxController.a().whileTrue(new ActuateClaw(clawSubsystem, Direction.BACKWARD));
    operatorXboxController.b().whileTrue(new ActuateClaw(clawSubsystem, Direction.FORWARD));

    // Driver controller Bindings
    driverXboxController.x().onTrue(new SetVisionPipeline(cameraSubsystem, Pipeline.APRIL_TAG));
    driverXboxController.y().onTrue(new SetVisionPipeline(cameraSubsystem, Pipeline.RETRO_TAPE));
  }

  private void ShuffleboardSetup() {
    // Air Compressor Entry
    compressorEnabled = Shuffleboard.getTab(Constants.ShuffleboardConstants.GAME_TAB_NAME)
        .add("Enable Compressor", true)
        .withWidget(BuiltInWidgets.kToggleSwitch)
        .getEntry();
    networkTableInstance.addListener(compressorEnabled,
        EnumSet.of(NetworkTableEvent.Kind.kValueAll),
        (event) -> new SetCompressor(pneumaticsSubsystem, event).schedule());
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return null;
  }
}

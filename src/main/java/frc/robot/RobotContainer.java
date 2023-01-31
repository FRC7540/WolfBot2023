// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.ActuateClaw;
import frc.robot.commands.Drive;
import frc.robot.subsystems.CameraSubsystem;
import frc.robot.subsystems.ClawSubsystem;
import frc.robot.subsystems.DrivebaseSubsystem;
import frc.robot.subsystems.PneumaticsSubsystem;
import frc.robot.subsystems.ClawSubsystem.directions;
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
  private final DrivebaseSubsystem m_drivebaseSubsystem = new DrivebaseSubsystem();
  private final CameraSubsystem m_cameraSubsystem = new CameraSubsystem();
  private final PneumaticsSubsystem m_pneumaticsSubsystem = new PneumaticsSubsystem();
  private final ClawSubsystem m_clawSubsystem = new ClawSubsystem();

  // Controller Setup
  private final CommandXboxController m_driverXboxController = new CommandXboxController(
      OperatorConstants.kDriverXboxControllerPort);

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    configureBindings();
    configureDefaultCommands();
  }

  private void configureDefaultCommands() {
    // Drivebase default command
    Trigger leftBumper = m_driverXboxController.leftBumper();
    Drive driveCommand = new Drive(m_drivebaseSubsystem, m_driverXboxController::getLeftX,
        m_driverXboxController::getLeftY, m_driverXboxController::getRightX, leftBumper::getAsBoolean);
    m_drivebaseSubsystem.setDefaultCommand(driveCommand);

    // Claw default command
    m_clawSubsystem.setDefaultCommand(new InstantCommand(() -> m_clawSubsystem.stopClaw(), m_clawSubsystem));
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
    m_driverXboxController.a().whileTrue(new ActuateClaw(m_clawSubsystem, directions.BACKWARD));
    m_driverXboxController.b().whileTrue(new ActuateClaw(m_clawSubsystem, directions.FORWARD));
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

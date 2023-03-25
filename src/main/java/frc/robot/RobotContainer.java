// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.ActuateClaw;
import frc.robot.suppliers.AutoAlign;
import frc.robot.commands.AutoBalance;
import frc.robot.commands.ChangeLeds;
import frc.robot.commands.CraneDown;
import frc.robot.commands.CraneUp;
import frc.robot.commands.ActuateClaw.Direction;
import frc.robot.commands.OperateCrane.armPreset;
import frc.robot.commands.Drive;
import frc.robot.commands.LockedDrive;
import frc.robot.commands.OperateCrane;
import frc.robot.commands.SetCompressor;
import frc.robot.commands.SetVisionPipeline;
import frc.robot.subsystems.CameraSubsystem;
import frc.robot.subsystems.ClawSubsystem;
import frc.robot.subsystems.CraneSubsystem;
import frc.robot.subsystems.DrivebaseSubsystem;
import frc.robot.subsystems.LedSubsystem;
import frc.robot.subsystems.PneumaticsSubsystem;
import frc.robot.subsystems.CameraSubsystem.Pipeline;

import java.util.EnumSet;

import edu.wpi.first.networktables.NetworkTableEvent;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
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
        private final CraneSubsystem craneSubsystem = new CraneSubsystem();
        private final Dashboard dashboard = new Dashboard(drivebaseSubsystem);
        private final LedSubsystem ledSubsystem = new LedSubsystem();

        // Controller Setup
        private final CommandXboxController driverXboxController = new CommandXboxController(
                        OperatorConstants.DRIVER_XBOX_CONTROLLER_PORT);
        private final CommandXboxController operatorXboxController = new CommandXboxController(
                        OperatorConstants.OPERATOR_XBOX_CONTROLLER_PORT);
        private Drive drive;
        private OperateCrane operateCrane;

        // Shuffleboard Entries

        /**
         * The container for the robot. Contains subsystems, OI devices, and commands.
         */
        public RobotContainer() {
                configureDefaultCommands();
                configureBindings();
                dashboard.ShuffleboardSetup();
                networkTableListenerSetup();
        }

        private void configureDefaultCommands() {
                // Drivebase default command
                Trigger leftBumper = driverXboxController.leftBumper();
                drive = new Drive(drivebaseSubsystem, driverXboxController::getLeftX,
                                driverXboxController::getLeftY, driverXboxController::getRightX,
                                leftBumper::getAsBoolean);
                drivebaseSubsystem.setDefaultCommand(drive);

                // Claw default command
                clawSubsystem.setDefaultCommand(new InstantCommand(() -> clawSubsystem.stopClaw(), clawSubsystem));

                // Crane default command
                operateCrane = new OperateCrane(craneSubsystem, operatorXboxController::getLeftY);
                craneSubsystem.setDefaultCommand(operateCrane);

                ledSubsystem.setDefaultCommand(new ChangeLeds(ledSubsystem));
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
                operatorXboxController.rightTrigger().debounce(Constants.OperatorConstants.DEFAULT_DEBOUNCE_DELAY)
                                .whileTrue(new ActuateClaw(clawSubsystem, Direction.BACKWARD));
                operatorXboxController.leftTrigger().debounce(Constants.OperatorConstants.DEFAULT_DEBOUNCE_DELAY)
                                .whileTrue(new ActuateClaw(clawSubsystem, Direction.FORWARD));
                operatorXboxController.povUp().debounce(Constants.OperatorConstants.DEFAULT_DEBOUNCE_DELAY)
                                .onTrue(new CraneUp(craneSubsystem));
                operatorXboxController.povDown().debounce(Constants.OperatorConstants.DEFAULT_DEBOUNCE_DELAY)
                                .onTrue(new CraneDown(craneSubsystem));
                operatorXboxController.x().debounce(Constants.OperatorConstants.DEFAULT_DEBOUNCE_DELAY)
                                .onTrue(new InstantCommand(() -> operateCrane.recallPreset(armPreset.HOME),
                                                craneSubsystem));
                operatorXboxController.a().debounce(Constants.OperatorConstants.DEFAULT_DEBOUNCE_DELAY)
                                .onTrue(new InstantCommand(
                                                () -> operateCrane.recallPreset(
                                                                operatorXboxController.leftBumper().getAsBoolean()
                                                                                ? armPreset.LOWER_NODE
                                                                                : armPreset.FLOOR_PICKUP),
                                                craneSubsystem));
                operatorXboxController.b().debounce(Constants.OperatorConstants.DEFAULT_DEBOUNCE_DELAY)
                                .onTrue(new InstantCommand(
                                                () -> operateCrane
                                                                .recallPreset(operatorXboxController.leftBumper()
                                                                                .getAsBoolean() ? armPreset.MID_NODE
                                                                                                : armPreset.DO_NOTHING),
                                                craneSubsystem));
                operatorXboxController.y().debounce(Constants.OperatorConstants.DEFAULT_DEBOUNCE_DELAY)
                                .onTrue(new InstantCommand(
                                                () -> operateCrane.recallPreset(
                                                                operatorXboxController.leftBumper().getAsBoolean()
                                                                                ? armPreset.UPPER_NODE
                                                                                : armPreset.SHELF_PICKUP),
                                                craneSubsystem));
                operatorXboxController.rightBumper().debounce(Constants.OperatorConstants.DEFAULT_DEBOUNCE_DELAY)
                                .onTrue(new ConditionalCommand(
                                                new SetVisionPipeline(cameraSubsystem, Pipeline.RETRO_TAPE),
                                                new SetVisionPipeline(cameraSubsystem, Pipeline.APRIL_TAG),
                                                () -> cameraSubsystem.getPipeline() == Pipeline.APRIL_TAG));

                // Driver controller Bindings
                driverXboxController.x().debounce(Constants.OperatorConstants.DEFAULT_DEBOUNCE_DELAY)
                                .onTrue(new SetVisionPipeline(cameraSubsystem, Pipeline.APRIL_TAG));
                driverXboxController.y().debounce(Constants.OperatorConstants.DEFAULT_DEBOUNCE_DELAY)
                                .onTrue(new SetVisionPipeline(cameraSubsystem, Pipeline.RETRO_TAPE));
                Trigger leftBumper = driverXboxController.leftBumper();
                driverXboxController.start().debounce(Constants.OperatorConstants.DEFAULT_DEBOUNCE_DELAY)
                                .onTrue(new InstantCommand(() -> drivebaseSubsystem.resetYaw(), drivebaseSubsystem));
                driverXboxController.b().debounce(Constants.OperatorConstants.DEFAULT_DEBOUNCE_DELAY)
                                .whileTrue(new LockedDrive(drivebaseSubsystem, driverXboxController::getLeftX,
                                                driverXboxController::getLeftY, leftBumper::getAsBoolean, Dashboard.rotationController));
                AutoAlign autoAlign = new AutoAlign(cameraSubsystem, Dashboard.autoAlignController);
                driverXboxController.rightBumper().debounce(Constants.OperatorConstants.DEFAULT_DEBOUNCE_DELAY)
                                .whileTrue(new LockedDrive(drivebaseSubsystem, autoAlign::getOutput, driverXboxController::getLeftY, () -> false, Dashboard.rotationController));
                Trigger driverRightTrigger = driverXboxController.rightTrigger();
                driverRightTrigger.onTrue(new InstantCommand(()-> drivebaseSubsystem.setFieldOrientedDriveEnabled(false)));
                driverRightTrigger.onFalse(new InstantCommand(()-> drivebaseSubsystem.setFieldOrientedDriveEnabled(true)));
        }

        private void networkTableListenerSetup() {
                Dashboard.networkTableInstance.addListener(Dashboard.compressorEnabled,
                                EnumSet.of(NetworkTableEvent.Kind.kValueAll),
                                (event) -> new SetCompressor(pneumaticsSubsystem, event).schedule());

                Dashboard.networkTableInstance.addListener(Dashboard.accelLimitEntry,
                                EnumSet.of(NetworkTableEvent.Kind.kValueAll),
                                (event) -> drive.updateRateLimiters(event));

                Dashboard.networkTableInstance.addListener(Dashboard.fieldOrientationEntry,
                                EnumSet.of(NetworkTableEvent.Kind.kValueAll), (e) -> {
                                        drivebaseSubsystem.setFieldOrientedDriveEnabled(e.valueData.value.getBoolean());
                                });

                Dashboard.networkTableInstance.addListener(
                                Dashboard.armMinimumAngle,
                                EnumSet.of(NetworkTableEvent.Kind.kValueAll),
                                (e) -> craneSubsystem.setMinAngle(e.valueData.value.getDouble()));
                Dashboard.networkTableInstance.addListener(
                                Dashboard.armMaximumAngleHigh,
                                EnumSet.of(NetworkTableEvent.Kind.kValueAll),
                                (e) -> craneSubsystem.setMaxAngleHigh(e.valueData.value.getDouble()));
                Dashboard.networkTableInstance.addListener(
                                Dashboard.armMaximumAngleLow,
                                EnumSet.of(NetworkTableEvent.Kind.kValueAll),
                                (e) -> craneSubsystem.setMaxAngleLow(e.valueData.value.getDouble()));
                Dashboard.networkTableInstance.addListener(
                                Dashboard.armEncoderOffset,
                                EnumSet.of(NetworkTableEvent.Kind.kValueAll),
                                (e) -> craneSubsystem.setEncoderOffset(e.valueData.value.getDouble()));
                Dashboard.networkTableInstance.addListener(
                                Dashboard.armRateLimit,
                                EnumSet.of(NetworkTableEvent.Kind.kValueAll),
                                (e) -> operateCrane.setRateLimit(e.valueData.value.getDouble()));
                Dashboard.networkTableInstance.addListener(
                                Dashboard.armSpeedMultiplier,
                                EnumSet.of(NetworkTableEvent.Kind.kValueAll),
                                (e) -> operateCrane.setSpeedMultiplier(e.valueData.value.getDouble()));
        }

        /**
         * Use this to pass the autonomous command to the main {@link Robot} class.
         *
         * @return the command to run in autonomous
         */
        public Command getAutonomousCommand() {
                return autonomousCommand;
        }

        public Command autonomousCommand = new SequentialCommandGroup(
                        new InstantCommand(() -> drivebaseSubsystem.resetNav(), drivebaseSubsystem),
                        new LockedDrive(drivebaseSubsystem, () -> 0, () -> 0.3, () -> true, Dashboard.rotationController)
                                        .until(() -> drivebaseSubsystem
                                                        .getDisplacementY() <= Constants.Autonomous.DRIVE_BACKWARD_DISTANCE)
                                        .withTimeout(0.5),
                        new InstantCommand(() -> drivebaseSubsystem.resetDisplacement(), drivebaseSubsystem),
                        new LockedDrive(drivebaseSubsystem, () -> 0, () -> -0.4, () -> true, Dashboard.rotationController)
                                        .until(() -> (drivebaseSubsystem
                                                        .getPitch() >= Dashboard.balanceTriggerAngle.get().getDouble())
                                                        || (drivebaseSubsystem
                                                                        .getDisplacementY() >= Constants.Autonomous.DRIVE_FORWARD_DISTANCE))
                                        .withTimeout(3.5),
                        new ConditionalCommand(new AutoBalance(drivebaseSubsystem), new WaitCommand(0),
                                        () -> Math.abs(drivebaseSubsystem.getPitch()) > Dashboard.balanceTriggerAngle
                                                        .get().getDouble()));
}

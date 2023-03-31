package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Dashboard;
import frc.robot.commands.ActuateClaw.Direction;
import frc.robot.commands.OperateCrane.armPreset;
import frc.robot.subsystems.ClawSubsystem;
import frc.robot.subsystems.CraneSubsystem;
import frc.robot.subsystems.DrivebaseSubsystem;

public class AutoPlaceCone extends SequentialCommandGroup {

    public AutoPlaceCone(DrivebaseSubsystem drivebaseSubsystem, ClawSubsystem clawSubsystem, OperateCrane operateCrane,
            CraneSubsystem craneSubsystem) {
        addCommands(
                new InstantCommand(() -> drivebaseSubsystem.resetNav(), drivebaseSubsystem),
                // Stage 1: place a cone, rotate

                // raise arm
                new InstantCommand(() -> operateCrane.recallPreset(armPreset.UPPER_NODE), craneSubsystem),

                new WaitCommand(Dashboard.autonomousWaitToRaise.get().getDouble()),

                // drive up
                new LockedDrive(drivebaseSubsystem, () -> Dashboard.autonomousDriveToPlaceSpeed.get().getDouble(),
                        () -> 0.0, () -> true, Dashboard.rotationController)
                        .until(() -> drivebaseSubsystem
                                .getDisplacementY() <= Dashboard.autonomousDriveToPlaceDistance.get().getDouble())
                        .withTimeout(0.5),

                // open claw
                new WaitCommand(Dashboard.autonomousWaitToPlaceDelay.get().getDouble()),

                new ActuateClaw(clawSubsystem, Direction.BACKWARD),

                new WaitCommand(Dashboard.autonomousWaitAfterPlaceDelay.get().getDouble()),

                // drive back
                new InstantCommand(() -> drivebaseSubsystem.resetNav(), drivebaseSubsystem),
                new LockedDrive(drivebaseSubsystem, () -> Dashboard.autonomousReverseFromPlaceSpeed.get().getDouble(),
                        () -> 0.0, () -> true, Dashboard.rotationController)
                        .until(() -> drivebaseSubsystem
                                .getDisplacementY() <= Dashboard.autonomousReverseFromPlaceDistance.get().getDouble()),

                // lower crane
                new InstantCommand(() -> operateCrane.recallPreset(armPreset.HOME), craneSubsystem),

                new WaitCommand(Dashboard.autonomousWaitToLower.get().getDouble()),

                // rotate 180
                new InstantCommand(() -> drivebaseSubsystem.resetNav(), drivebaseSubsystem),
                new Drive(drivebaseSubsystem, () -> 0.0, () -> 0.0,
                        () -> Dashboard.autonomousRotateSpeed.get().getDouble(), () -> true)
                        .withTimeout(Dashboard.autonomousRotateTimeout.get().getDouble()),
                new LockedDrive(drivebaseSubsystem, () -> 0.0, () -> 0.0, () -> true, Dashboard.rotationController),

                new InstantCommand(() -> drivebaseSubsystem.resetNav(), drivebaseSubsystem)

        );
    }

}

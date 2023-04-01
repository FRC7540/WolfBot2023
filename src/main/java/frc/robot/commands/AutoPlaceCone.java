package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Dashboard;
import frc.robot.commands.ActuateClaw.Direction;
import frc.robot.commands.OperateCrane.armPreset;
import frc.robot.subsystems.ClawSubsystem;
import frc.robot.subsystems.CraneSubsystem;
import frc.robot.subsystems.DrivebaseSubsystem;

public class AutoPlaceCone extends SequentialCommandGroup {

    public AutoPlaceCone(DrivebaseSubsystem drivebaseSubsystem, ClawSubsystem clawSubsystem,
            CraneSubsystem craneSubsystem) {
                OperateCrane operateCrane = new OperateCrane(craneSubsystem, () -> 0.0);
        addCommands(
                new PrintCommand("Starting Cone Place"),

                new InstantCommand(() -> drivebaseSubsystem.resetNav(), drivebaseSubsystem),
                // Stage 1: place a cone, rotate

                // raise arm
                new InstantCommand(() -> operateCrane.recallPreset(armPreset.UPPER_NODE), craneSubsystem),

                Commands.deadline(new WaitCommand(Dashboard.autonomousWaitToRaise.get().getDouble()), new Drive(drivebaseSubsystem, () -> 0.0, () -> 0.0, () -> 0.0, () -> true)),

                new PrintCommand("Driving forward"),
                // drive up
                new LockedDrive(drivebaseSubsystem, () -> 0.0, () -> Dashboard.autonomousDriveToPlaceSpeed.get().getDouble(),
                         () -> true, Dashboard.rotationController)
                        .until(() -> drivebaseSubsystem
                                .getDisplacementY() <= Dashboard.autonomousDriveToPlaceDistance.get().getDouble())
                        .withTimeout(0.5),

                // open claw


                Commands.deadline(new WaitCommand(Dashboard.autonomousWaitToPlaceDelay.get().getDouble()), new Drive(drivebaseSubsystem, () -> 0.0, () -> 0.0, () -> 0.0, () -> true)),

                new PrintCommand("Drop cone"),

                new ActuateClaw(clawSubsystem, Direction.BACKWARD),

                Commands.deadline(new WaitCommand(Dashboard.autonomousWaitAfterPlaceDelay.get().getDouble()), new Drive(drivebaseSubsystem, () -> 0.0, () -> 0.0, () -> 0.0, () -> true)),

                // drive back

                new PrintCommand("Drive back"),

                new InstantCommand(() -> drivebaseSubsystem.resetNav(), drivebaseSubsystem),
                new LockedDrive(drivebaseSubsystem, () -> 0.0, () -> Dashboard.autonomousReverseFromPlaceSpeed.get().getDouble(),
                        () -> true, Dashboard.rotationController)
                        .until(() -> drivebaseSubsystem
                                .getDisplacementY() <= Dashboard.autonomousReverseFromPlaceDistance.get().getDouble()).withTimeout(0.5),

                // lower crane
                new PrintCommand("Lower crane"),

                new InstantCommand(() -> operateCrane.recallPreset(armPreset.HOME), craneSubsystem),

                Commands.deadline(new WaitCommand(Dashboard.autonomousWaitToLower.get().getDouble()),new Drive(drivebaseSubsystem, () -> 0.0, () -> 0.0, () -> 0.0, () -> true)),

                // rotate 180
                new PrintCommand("Rotate 180"),

                new InstantCommand(() -> drivebaseSubsystem.resetNav(), drivebaseSubsystem),
                new Drive(drivebaseSubsystem, () -> 0.0, () -> 0.0,
                        () -> Dashboard.autonomousRotateSpeed.get().getDouble(), () -> true)
                        .withTimeout(Dashboard.autonomousRotateTimeout.get().getDouble()),
                new LockedDrive(drivebaseSubsystem, () -> 0.0, () -> 0.0, () -> true, Dashboard.rotationController).withTimeout(0.5),

                new InstantCommand(() -> drivebaseSubsystem.resetNav(), drivebaseSubsystem)

        );
    }

}

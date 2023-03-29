package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.Dashboard;
import frc.robot.commands.ActuateClaw.Direction;
import frc.robot.commands.OperateCrane.armPreset;
import frc.robot.subsystems.ClawSubsystem;
import frc.robot.subsystems.DrivebaseSubsystem;

//This needs ot bew separate to prevent a race condition, if its not it will try to access the shuffleboard values before the dashboard class has had time to fully initialize and throw a null pointer
public class AutonomousCommand extends SequentialCommandGroup{

    public AutonomousCommand(DrivebaseSubsystem drivebaseSubsystem, ClawSubsystem clawSubsystem, OperateCrane operateCrane ) {
        addCommands(
            new InstantCommand(() -> drivebaseSubsystem.resetNav(), drivebaseSubsystem),
            //Stage 1: place a cone, rotate

            //raise arm
            new InstantCommand(() -> operateCrane.recallPreset(armPreset.UPPER_NODE)),

            //drive up
            new LockedDrive(drivebaseSubsystem, () -> Dashboard.autonomousDriveToPlaceSpeed.get().getDouble(), () -> 0.0, () -> true, Dashboard.rotationController)
            .until(() -> drivebaseSubsystem
                            .getDisplacementY() <= Dashboard.autonomousDriveToPlaceDistance.get().getDouble())
            .withTimeout(0.5),

            //open claw
            new WaitCommand(Dashboard.autonomousWaitToPlaceDelay.get().getDouble()),

            new ActuateClaw(clawSubsystem, Direction.BACKWARD),

            new WaitCommand(Dashboard.autonomousWaitAfterPlaceDelay.get().getDouble()),

            //drive back
            new InstantCommand(() -> drivebaseSubsystem.resetNav(), drivebaseSubsystem),
            new LockedDrive(drivebaseSubsystem, () -> Dashboard.autonomousReverseFromPlaceSpeed.get().getDouble(), () -> 0.0, () -> true, Dashboard.rotationController)
            .until(() -> drivebaseSubsystem
                            .getDisplacementY() <= Dashboard.autonomousReverseFromPlaceDistance.get().getDouble()),

            //lower crane
            new InstantCommand(() -> operateCrane.recallPreset(armPreset.HOME)),

            //rotate 180
            new InstantCommand(() -> drivebaseSubsystem.resetNav(), drivebaseSubsystem),
            new Drive(drivebaseSubsystem, () -> 0.0, () -> 0.0, () -> Dashboard.autonomousRotateSpeed.get().getDouble(), () -> true).withTimeout(Dashboard.autonomousRotateTimeout.get().getDouble()),
            new LockedDrive(drivebaseSubsystem, () -> 0.0, () -> 0.0, () -> true, Dashboard.rotationController),

            new InstantCommand(() -> drivebaseSubsystem.resetNav(), drivebaseSubsystem),

            //Begin stage 2: drive forward, blance if needed
            new LockedDrive(drivebaseSubsystem, () -> 0, () -> -0.4, () -> true, Dashboard.rotationController)
                            .until(() -> (drivebaseSubsystem
                                            .getPitch() >= Dashboard.balanceTriggerAngle.get().getFloat())
                                            || (drivebaseSubsystem
                                                            .getDisplacementY() >= Constants.Autonomous.DRIVE_FORWARD_DISTANCE))
                            .withTimeout(3.5),
            new AutoBalance(drivebaseSubsystem).unless(() -> drivebaseSubsystem.getPitch() < 5)


        );
    }

}

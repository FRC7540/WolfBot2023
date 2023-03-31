package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.Dashboard;
import frc.robot.subsystems.DrivebaseSubsystem;

public class AutoBalanceOrDrive extends SequentialCommandGroup {

    public AutoBalanceOrDrive(DrivebaseSubsystem drivebaseSubsystem) {
        addCommands( // Begin stage 2: drive forward, blance if needed
                new LockedDrive(drivebaseSubsystem, () -> 0, () -> -0.4, () -> true, Dashboard.rotationController)
                        .until(() -> (drivebaseSubsystem
                                .getPitch() >= Dashboard.balanceTriggerAngle.get().getFloat())
                                || (drivebaseSubsystem
                                        .getDisplacementY() >= Constants.Autonomous.DRIVE_FORWARD_DISTANCE))
                        .withTimeout(3.5),
                new AutoBalance(drivebaseSubsystem).unless(() -> drivebaseSubsystem.getPitch() < 5));
    }

}

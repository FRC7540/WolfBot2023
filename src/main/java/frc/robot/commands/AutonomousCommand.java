package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.ClawSubsystem;
import frc.robot.subsystems.CraneSubsystem;
import frc.robot.subsystems.DrivebaseSubsystem;

//This needs ot bew separate to prevent a race condition, if its not it will try to access the shuffleboard values before the dashboard class has had time to fully initialize and throw a null pointer
public class AutonomousCommand extends SequentialCommandGroup{

    public AutonomousCommand(DrivebaseSubsystem drivebaseSubsystem, ClawSubsystem clawSubsystem, CraneSubsystem craneSubsystem ) {
        addCommands(
            new AutoPlaceCone(drivebaseSubsystem, clawSubsystem, craneSubsystem),
            new AutoBalanceOrDrive(drivebaseSubsystem)
        );
    }

}

/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;

public class AutoDriveForwardCommand extends CommandBase {
  /**
   * Creates a new AutoDriveForwardCommand.
   */

  DriveSubsystem m_subsystem;
  
  public AutoDriveForwardCommand(DriveSubsystem subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_subsystem = subsystem;
    addRequirements(m_subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_subsystem.drive(0.5, 0.5);
    System.out.println("Goin' forward");
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_subsystem.drive(0, 0);
    System.out.println("Finished Going Forwards");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}

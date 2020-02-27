/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.LifterSubsystem;

public class DriveLifterCommand extends CommandBase {
  /**
   * Creates a new DriveLifterCommand
   *.
   */

  private final LifterSubsystem m_subsystem;
  private final boolean m_reverse; 
  public DriveLifterCommand(LifterSubsystem subsystem, boolean reverse) {
    m_subsystem = subsystem;
    m_reverse = reverse;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (m_reverse)
      m_subsystem.driveLifter(-1 * Constants.lifterSpeed);
    else
      m_subsystem.driveLifter(Constants.lifterSpeed);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_subsystem.driveLifter(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}

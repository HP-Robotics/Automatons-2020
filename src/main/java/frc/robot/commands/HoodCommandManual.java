/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.HoodSubsystem;

public class HoodCommandManual extends CommandBase {
  /**
   * Creates a new HoodCommandManual.
   */

  private final HoodSubsystem m_subsystem;
  private final int m_reverse;
  
  public HoodCommandManual(HoodSubsystem subsystem, boolean reverse) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_subsystem = subsystem;
    if(reverse)
      m_reverse = -1;
    else
      m_reverse = 1;
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_subsystem.setHoodPositionManual(m_reverse * Constants.hoodMovement);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}

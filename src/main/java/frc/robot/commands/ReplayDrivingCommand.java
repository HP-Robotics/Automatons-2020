/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ReplaySubsystem;

public class ReplayDrivingCommand extends CommandBase {
  /**
   * Creates a new ReplayDrivingCommand.
   */
  private ReplaySubsystem m_subsystem;
  private DriveSubsystem m_driveSubsystem;

  private String m_filename;

  private boolean m_end;
    
  public ReplayDrivingCommand(ReplaySubsystem subsystem, DriveSubsystem driverSubsystem, String filename) {
    m_subsystem = subsystem;
    m_driveSubsystem = driverSubsystem;
    m_filename = filename;

    addRequirements(m_subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    boolean opened;
    if (m_filename == null) {
      opened = m_subsystem.openReadingCSV("supplyafilename.csv");
    } else {
      opened = m_subsystem.openReadingCSV(m_filename);
    }
    if(!opened) {
      m_end = true;
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double[] values = m_subsystem.readLine();
    if(values == null || values.length < 2) {
      m_end = true;
      return;
    }
    new DriveCommand(m_driveSubsystem, () -> values[0], () -> values[1]);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_end = false;
    m_subsystem.closeCSV();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_end;
  }
}

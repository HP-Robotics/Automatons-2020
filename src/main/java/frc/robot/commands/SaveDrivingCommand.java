/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ReplaySubsystem;

public class SaveDrivingCommand extends CommandBase {

  private final ReplaySubsystem m_subsystem;
  private final DriveSubsystem m_driveSubsystem;

  private final String m_filename;
  private boolean m_end;
  /**
   * Creates a new SaveDrivingCommand.
   */
  public SaveDrivingCommand(ReplaySubsystem subsystem, DriveSubsystem driveSubsystem, String filename) {
    m_subsystem = subsystem;
    m_driveSubsystem = driveSubsystem;
    m_filename = filename;

    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    boolean opened;
    if (m_filename == null) {
      opened = m_subsystem.openWritingCSV("supplyafilename.csv");
    } else {
      opened = m_subsystem.openWritingCSV(m_filename);
    }
    if(!opened) {
      m_end = true;
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double outputs[] = m_driveSubsystem.getDriveOutputs();
    String line = Double.toString(outputs[0]) + "," + Double.toString(outputs[1]) + "," + Double.toString(Timer.getFPGATimestamp());

    m_subsystem.writeCSV(line);
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

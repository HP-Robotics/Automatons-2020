/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.SnazzyLog;
import frc.robot.subsystems.DriveSubsystem;

public class CalibrateDrive extends CommandBase {
  /**
   * Creates a new CalibrateTurret.
   */

  DriveSubsystem m_subsystem;
  Timer m_timer = new Timer();
  SnazzyLog m_log;

  public CalibrateDrive(DriveSubsystem subsystem) {
    m_subsystem = subsystem;
    m_log = new SnazzyLog();
    m_log.open("drivetrain.csv", "time, position, velocity\n");
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_timer.start();
    m_timer.reset();
    m_subsystem.drive(0.75, 0.75);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_log.write(String.format("%g,%d,%d\n", Timer.getFPGATimestamp(), m_subsystem.frontRightController.getSelectedSensorPosition(0), m_subsystem.frontRightController.getSelectedSensorVelocity(0)));
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_subsystem.drive(0.0, 0.0);
    m_log.close();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_timer.hasElapsed(1.1);
  }
}

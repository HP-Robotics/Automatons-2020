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
import frc.robot.subsystems.ShooterSubsystem;

public class LimeLightCommand extends CommandBase {
  /**
   * Creates a new LimeLightControl.
   */

  ShooterSubsystem m_shooter;
  HoodSubsystem m_hood;
  public LimeLightCommand(ShooterSubsystem shooter, HoodSubsystem hood) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_shooter = shooter;
    m_hood = hood;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_shooter.getTv() != 0.0) {
      double command = 0;
      if (m_shooter.getTx() > 5.0) {
        command = 1;
      } else if (m_shooter.getTx() < -5.0) {
        command = -1;
      } else if (Math.abs(m_shooter.getTx()) <= 1) {
        command = 0;
      } else {
        command = m_shooter.getTx() / 5.0;
      }
      m_shooter.setManualTurretAngle(command * Constants.turretMovement);
    }
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

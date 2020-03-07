/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.WashingMachineSubsystem;
import frc.robot.Constants;
import frc.robot.subsystems.ShooterSubsystem;

public class SmartWasherCommand extends CommandBase {
  /**
   * Creates a new SmartWasherCommand.
   */

  WashingMachineSubsystem m_washer;
  ShooterSubsystem m_shooter;
  boolean m_reversing;
  boolean m_atSpeed;
  Timer m_timer;
  
  public SmartWasherCommand(WashingMachineSubsystem washer, ShooterSubsystem shooter) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_washer = washer;
    m_shooter = shooter;
    m_reversing = false;
    m_atSpeed = false;
    m_timer = new Timer();
    addRequirements(m_washer);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_timer.start();
    m_timer.reset();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (!m_reversing) {
      if (m_shooter.isAtSpeed()) {
        m_washer.setVelocity(Constants.washingMachineSpeed, Constants.intakeSpeed);
        if (!m_atSpeed) {
          m_atSpeed = true;
          m_timer.reset();
        }
      } else {
        m_washer.setVelocity(Constants.washingMachineSpeed / 2, Constants.intakeSpeed / 2);
        if (m_atSpeed) {
          m_atSpeed = false;
          m_timer.reset();
        }
      }

      if (Math.abs(m_washer.getVelocity()) < 1000 && m_timer.hasElapsed(0.25)) {
        m_reversing = true;
        m_timer.reset();
      }
    } else {
      m_washer.setVelocity(-Constants.washingMachineSpeed, Constants.uptakeReverse);
      if (m_timer.hasElapsed(0.25)) {
        m_reversing = false;
        m_timer.reset();
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_washer.setVelocity(Constants.washingMachineOff, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}

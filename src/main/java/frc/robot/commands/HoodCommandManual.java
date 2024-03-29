/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.HoodSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class HoodCommandManual extends CommandBase {
  /**
   * Creates a new HoodCommandManual.
   */

  private final HoodSubsystem m_subsystem;
  private final DoubleSupplier m_power;
  private final ShooterSubsystem m_shooterSubsystem;
  
  public HoodCommandManual(HoodSubsystem subsystem, ShooterSubsystem shooter, DoubleSupplier power) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_subsystem = subsystem;
    m_shooterSubsystem = shooter;
    m_power = power;
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_subsystem.setHoodPositionManual(m_power.getAsDouble() * Constants.hoodMovement);

    m_shooterSubsystem.setShooterSpeed(Constants.shooterSpeedSlope * m_subsystem.getHoodTarget() + Constants.shooterSpeedAtTwo);
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
    return true;
  }
}

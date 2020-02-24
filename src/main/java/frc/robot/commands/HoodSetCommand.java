/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.HoodSubsystem;

public class HoodSetCommand extends CommandBase {
  /**
   * Creates a new HoodSetCommand.
   */

  private final HoodSubsystem m_subsystem;
  DoubleSupplier m_target;
  
  public HoodSetCommand(HoodSubsystem subsystem, DoubleSupplier target) {
    m_subsystem = subsystem;
    m_target = target;
 
      addRequirements(m_subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    double target = m_target.getAsDouble();
    if (target < 50)
      target = 50;
    if (target > 1000)
       target = 1000;
      m_subsystem.setHoodPosition(target);
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

/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ShooterSubsystem;

public class TurretSetCommand extends CommandBase {
  /**
   * Creates a new TurretSetCommand.
   */

  ShooterSubsystem m_shooterSubsystem;
  DoubleSupplier m_target;
  public TurretSetCommand(ShooterSubsystem subsystem, DoubleSupplier target) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_shooterSubsystem = subsystem;
    m_target = target;
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    double target = m_target.getAsDouble();

    if (target < 0) {
      target = -0.0;
    }
    if (target > 3000 ) {
      target = 3000;
    }
    System.out.println("Turret Set");
    m_shooterSubsystem.setTurretAngle(target);
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

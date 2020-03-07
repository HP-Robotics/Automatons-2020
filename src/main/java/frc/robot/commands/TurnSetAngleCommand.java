/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.util.function.IntSupplier;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.Constants;

public class TurnSetAngleCommand extends CommandBase {
  /**
   * Creates a new TurnSetAngleCommand.
   */

  DriveSubsystem m_driveSubsystem;
  IntSupplier m_target;

  public TurnSetAngleCommand(DriveSubsystem drive, IntSupplier target) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_driveSubsystem = drive;
    m_target = target;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_driveSubsystem.frontLeftController.setSelectedSensorPosition(0, 0, Constants.shooterTimeout);
    m_driveSubsystem.frontRightController.setSelectedSensorPosition(0, 0, Constants.shooterTimeout);
    m_driveSubsystem.frontLeftController.setNeutralMode(NeutralMode.Brake);
    m_driveSubsystem.frontRightController.setNeutralMode(NeutralMode.Brake);
    m_driveSubsystem.frontLeftController.set(ControlMode.MotionMagic, m_target.getAsInt());
    m_driveSubsystem.frontRightController.set(ControlMode.MotionMagic, m_target.getAsInt());
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_driveSubsystem.frontRightController.set(ControlMode.PercentOutput, 0.0);
    m_driveSubsystem.frontLeftController.set(ControlMode.PercentOutput, 0.0);
    m_driveSubsystem.frontRightController.setNeutralMode(NeutralMode.Coast);
    m_driveSubsystem.frontLeftController.setNeutralMode(NeutralMode.Coast);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (Math.abs(m_driveSubsystem.frontLeftController.getClosedLoopError()) < 100 && Math.abs(m_driveSubsystem.frontLeftController.getSelectedSensorVelocity(0)) < 5) && (Math.abs(m_driveSubsystem.frontRightController.getClosedLoopError()) < 100 && Math.abs(m_driveSubsystem.frontRightController.getSelectedSensorVelocity(0)) < 5);
  }
}

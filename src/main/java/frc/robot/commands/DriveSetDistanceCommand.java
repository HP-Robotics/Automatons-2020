/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.util.function.IntSupplier;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.DriveSubsystem;

public class DriveSetDistanceCommand extends CommandBase {
  /**
   * Creates a new DriveSetDistanceCommand.
   */

  DriveSubsystem m_subsystem;
  IntSupplier m_distance;

  public DriveSetDistanceCommand(DriveSubsystem subsystem, IntSupplier distance) {
    m_subsystem = subsystem;
    m_distance = distance;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_subsystem.frontLeftController.setSelectedSensorPosition(0, 0, Constants.shooterTimeout);
    m_subsystem.frontRightController.setSelectedSensorPosition(0, 0, Constants.shooterTimeout);
    m_subsystem.frontLeftController.setNeutralMode(NeutralMode.Brake);
    m_subsystem.frontRightController.setNeutralMode(NeutralMode.Brake);
    m_subsystem.frontLeftController.set(ControlMode.MotionMagic, m_distance.getAsInt());
    m_subsystem.frontRightController.set(ControlMode.MotionMagic, m_distance.getAsInt());
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_subsystem.frontLeftController.set(ControlMode.PercentOutput, 0.0);
    m_subsystem.frontRightController.set(ControlMode.PercentOutput, 0.0);
    m_subsystem.frontLeftController.setNeutralMode(NeutralMode.Coast);
    m_subsystem.frontRightController.setNeutralMode(NeutralMode.Coast);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return Math.abs(m_subsystem.frontLeftController.getClosedLoopTarget()-m_subsystem.frontLeftController.getSelectedSensorPosition(0)) < 100 && Math.abs(m_subsystem.frontLeftController.getSelectedSensorVelocity(0)) < 5 &&
       Math.abs(m_subsystem.frontRightController.getClosedLoopTarget()-m_subsystem.frontRightController.getSelectedSensorPosition(0)) < 100 && Math.abs(m_subsystem.frontRightController.getSelectedSensorVelocity(0)) < 5;
  }
}

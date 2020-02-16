/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class LifterSubsystem extends SubsystemBase {
  /**
   * Creates a new LifterSubsystem.
   */
  private final TalonSRX m_winchController;
  // private final TalonSRX m_lifterController;

  public LifterSubsystem() {
    m_winchController = new TalonSRX(Constants.winchMotorId);
    // m_lifterController = new TalonSRX(Constants.lifterMotorId);
  }

  public void driveWinch(double speed) {
    m_winchController.set(ControlMode.PercentOutput, speed);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}

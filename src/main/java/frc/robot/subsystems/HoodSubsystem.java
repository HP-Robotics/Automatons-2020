/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.controller.PIDController;
import frc.robot.Constants;

public class HoodSubsystem extends SubsystemBase {
  /**
   * Creates a new HoodSubsystem.
   */
  
  TalonSRX m_hoodController;
  PIDController m_PidController;

  DutyCycleEncoder m_revAbsolute;
  double m_offset;
  int m_iterationCounter;
  
  public HoodSubsystem() {
    m_iterationCounter = 0;
    m_offset = 0.0;

    // m_revAbsolute = new DutyCycleEncoder(Constants.hoodAbsoluteEncoder);
    // m_revAbsolute.reset();
    // m_revAbsolute.setDistancePerRotation(Constants.johnsonTicks);

    m_hoodController = new TalonSRX(Constants.hoodMotorId);
    m_PidController = new PIDController(Constants.hoodP, Constants.hoodI, Constants.hoodD);

    
    m_hoodController.configFactoryDefault();
    m_hoodController.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, Constants.shooterTimeout);
    m_hoodController.setSelectedSensorPosition(0, 0, Constants.shooterTimeout);
    m_hoodController.setSensorPhase(false);
    m_hoodController.setInverted(false);
    m_hoodController.config_kP(0, Constants.hoodP);
    m_hoodController.config_kI(0, Constants.hoodI);
    m_hoodController.config_kD(0, Constants.hoodD);
    m_hoodController.config_IntegralZone(0, Constants.hoodIntegralZone);
    m_hoodController.configClosedloopRamp(Constants.hoodRamp);
    
    m_hoodController.configNominalOutputForward(0, Constants.shooterTimeout);
    m_hoodController.configNominalOutputReverse(0, Constants.shooterTimeout);
    m_hoodController.configPeakOutputForward(1, Constants.shooterTimeout);
    m_hoodController.configPeakOutputReverse(-1, Constants.shooterTimeout);
    
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    // SmartDashboard.putNumber("Through Bore Encoder Value", m_revAbsolute.getDistance());
    //System.out.println("TBE: " + m_revAbsolute.getDistance() + ", VPE: " + m_hoodController.getSelectedSensorPosition());

    /*
    m_iterationCounter++;
    if (m_iterationCounter % 15 == 0) {
      m_offset = -m_revAbsolute.getDistance() + m_hoodController.getSelectedSensorPosition() - Constants.absZeroOffset;
    } */
    // System.out.println("Offset: " + m_offset);
    System.out.println("Target: " + m_hoodController.getClosedLoopTarget(0) + " Position: " + m_hoodController.getSelectedSensorPosition(0) + " Error: " + m_hoodController.getClosedLoopError(0));
    if(m_hoodController.getControlMode() == ControlMode.Position) {
      if(m_hoodController.getClosedLoopTarget(0) <= 0)
        m_hoodController.set(ControlMode.Position, 0);
      if(m_hoodController.getClosedLoopTarget(0) >= 675)
        m_hoodController.set(ControlMode.Position, 675);
    }

  }

  public void setHoodPosition (double position) {
    m_hoodController.set(ControlMode.Position, position);
    System.out.println("Hood Position set to " + SmartDashboard.getNumber("Hood Position", 0));
  }

  public void setHoodPositionManual (double difference) {
    if (Constants.turretSafetyDisabled) {
      m_hoodController.set(ControlMode.Position, m_hoodController.getClosedLoopTarget(0) + difference);
    }
  }

  public void setHoodOff () {
    m_hoodController.set(ControlMode.PercentOutput, 0.0);
    System.out.println("Hood Set Off");
  }
}

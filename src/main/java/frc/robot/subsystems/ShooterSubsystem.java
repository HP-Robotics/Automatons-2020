/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.VelocityMeasPeriod;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ShooterSubsystem extends SubsystemBase {
  /**
   * Creates a new ShooterSubsystem.
   */

  TalonFX m_shooterController;
  TalonFX m_shooterFollower;

  TalonSRX m_turretController;

  public ShooterSubsystem() {
    m_shooterController = new TalonFX(Constants.shooterMotor2Id);
    m_shooterFollower = new TalonFX(Constants.shooterMotor1Id);
    m_turretController = new TalonSRX(Constants.turretRingMotorId);
    
    m_shooterController.configFactoryDefault();
    m_shooterFollower.configFactoryDefault();

    m_shooterFollower.setInverted(true);
    m_shooterFollower.follow(m_shooterController);

    /* Config sensor used for Primary PID [Velocity] */
    m_shooterController.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0,
            Constants.shooterTimeout);

    /**
     * Phase sensor accordingly. Positive Sensor Reading should match Green
     * (blinking) Leds on Talon
     */
    
    /* Config the peak and nominal outputs */
    m_shooterController.configNominalOutputForward(0, Constants.shooterTimeout);
    m_shooterController.configNominalOutputReverse(0, Constants.shooterTimeout);
    m_shooterController.configPeakOutputForward(1, Constants.shooterTimeout);
    m_shooterController.configPeakOutputReverse(-1, Constants.shooterTimeout);

    /* Config the Velocity closed loop gains in slot0 */
    m_shooterController.config_kF(0, Constants.shooterF, Constants.shooterTimeout);
    m_shooterController.config_kP(0, Constants.shooterP, Constants.shooterTimeout);
    m_shooterController.config_kI(0, Constants.shooterI, Constants.shooterTimeout);
    m_shooterController.config_kD(0, Constants.shooterD, Constants.shooterTimeout);

    m_shooterController.config_IntegralZone(0, 1000);
    m_shooterController.configVelocityMeasurementWindow(1);
    m_shooterController.configVelocityMeasurementPeriod(VelocityMeasPeriod.Period_10Ms);

    m_turretController.configFactoryDefault();
    m_turretController.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, Constants.shooterTimeout);
    m_turretController.config_kP(0, 0.1);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    System.out.println(m_shooterController.getMotorOutputPercent() + " Percent, " + m_shooterController.getSupplyCurrent() + " Supply, " + m_shooterController.getStatorCurrent() + " Stator");
    
  }

  public void setShooter(double speed) {
    if(speed == 0.0)
      m_shooterController.set(ControlMode.PercentOutput, 0.0);
    else
      m_shooterController.set(ControlMode.Velocity, speed);
  }

  public void setTurretSpeed(double horizontalSpeed, double verticalSpeed) {
    if (Constants.turretSafetyDisabled) {
      m_turretController.set(ControlMode.PercentOutput, horizontalSpeed * Constants.turretCoefficient);
     // m_hoodController.set(ControlMode.PercentOutput, verticalSpeed * Constants.hoodCoefficient);
    }
  }

  public void disableTurret() {
    m_turretController.set(ControlMode.PercentOutput, 0);
  }

  public void setTurretAngle(int angle) {
    m_turretController.set(ControlMode.Position, angle);
  }

  public void setManualTurretAngle(int difference) {
    if (Constants.turretSafetyDisabled) {
      m_turretController.set(ControlMode.Position, m_turretController.getClosedLoopTarget(0) + difference);
    }
  }
} 
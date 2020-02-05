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

import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ShooterSubsystem extends SubsystemBase {
  /**
   * Creates a new ShooterSubsystem.
   */

  TalonFX m_shooterController;
  TalonFX m_shooterFollower;

  TalonSRX m_turretController;
  TalonSRX m_hoodController;

  DutyCycleEncoder m_revAbsolute;
  Double m_offset;
  int m_iterationCounter;

  public ShooterSubsystem() {
    m_iterationCounter = 0;
    m_offset = 0.0;

    m_shooterController = new TalonFX(Constants.shooterMotor2Id);
    m_shooterFollower = new TalonFX(Constants.shooterMotor1Id);
    m_revAbsolute = new DutyCycleEncoder(Constants.hoodAbsoluteEncoder);
    m_revAbsolute.setDistancePerRotation(Constants.hoodRelativeResolution); //TODO Compute Correct Number

    m_turretController = new TalonSRX(Constants.turretRingMotorId);
    m_hoodController = new TalonSRX(Constants.hoodMotorId);
    
    m_shooterController.configFactoryDefault();
    m_shooterFollower.configFactoryDefault();

    m_hoodController.configFactoryDefault();
    m_hoodController.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, Constants.shooterTimeout);
    m_hoodController.setSensorPhase(true);
    m_hoodController.config_kP(0, Constants.hoodP);
    m_hoodController.config_kI(0, Constants.hoodI);
    m_hoodController.config_kD(0, Constants.hoodD);

    m_hoodController.configNominalOutputForward(0, Constants.shooterTimeout);
    m_hoodController.configNominalOutputReverse(0, Constants.shooterTimeout);
    m_hoodController.configPeakOutputForward(1, Constants.shooterTimeout);
    m_hoodController.configPeakOutputReverse(-1, Constants.shooterTimeout);

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

    
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Through Bore Encoder Value", m_revAbsolute.getDistance());
    m_iterationCounter++;
    if (m_iterationCounter % 15 == 0) {
      m_offset = m_revAbsolute.get() - (double) m_hoodController.getSelectedSensorPosition();
    }
  }

  public void setShooter(double speed) {
    if(speed == 0.0)
      m_shooterController.set(ControlMode.PercentOutput, 0.0);
    else
      m_shooterController.set(ControlMode.Velocity, speed);
  }

  public void setTurretSpeed(double horizontalSpeed, double verticalSpeed) {
    if (Constants.allowTurretPercentOutput) {
      m_turretController.set(ControlMode.PercentOutput, horizontalSpeed * Constants.turretCoefficient);
     // m_hoodController.set(ControlMode.PercentOutput, verticalSpeed * Constants.hoodCoefficient);
    }
  }

  public void setHoodPosition (double position) {
    m_hoodController.set(ControlMode.Position, position + m_offset);
    System.out.println("Setting Hood position... " + position + m_offset);
  }

  public void setHoodOff () {
    m_hoodController.set(ControlMode.PercentOutput, 0.0);
    System.out.println("Hood Set Off");
  }
} 
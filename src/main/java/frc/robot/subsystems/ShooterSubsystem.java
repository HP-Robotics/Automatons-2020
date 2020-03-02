/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.VelocityMeasPeriod;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
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

  NetworkTable limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
  NetworkTableEntry ledMode = limelightTable.getEntry("ledMode");
  private double m_shooterSpeed = Constants.shooterFullspeed;
  

  boolean on;
  
  public ShooterSubsystem() {
    m_shooterController = new TalonFX(Constants.shooterMotor1Id);
    m_shooterFollower = new TalonFX(Constants.shooterMotor2Id);
    m_turretController = new TalonSRX(Constants.turretRingMotorId);
    
    m_shooterController.configFactoryDefault();
    m_shooterFollower.configFactoryDefault();

    m_shooterFollower.follow(m_shooterController);
    m_shooterFollower.setInverted(InvertType.OpposeMaster);
    m_shooterController.configNominalOutputForward(0, Constants.shooterTimeout);
    m_shooterController.configNominalOutputReverse(0, Constants.shooterTimeout);
    m_shooterController.configPeakOutputForward(1, Constants.shooterTimeout);
    m_shooterController.configPeakOutputReverse(-1, Constants.shooterTimeout);
    m_shooterController.configClosedloopRamp(1.0);

    /* Config the Velocity closed loop gains in slot0 */
    m_shooterController.config_kF(0, Constants.shooterF, Constants.shooterTimeout);
    m_shooterController.config_kP(0, Constants.shooterP, Constants.shooterTimeout);
    m_shooterController.config_kI(0, Constants.shooterI, Constants.shooterTimeout);
    m_shooterController.config_kD(0, Constants.shooterD, Constants.shooterTimeout);

    m_shooterController.config_IntegralZone(0, 1000);
    m_shooterController.configVelocityMeasurementWindow(1);
    m_shooterController.configVelocityMeasurementPeriod(VelocityMeasPeriod.Period_10Ms);

    m_turretController.configFactoryDefault();
    m_turretController.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, Constants.shooterTimeout);
    m_turretController.setInverted(false);
    m_turretController.setSensorPhase(true);
    m_turretController.configNominalOutputForward(0, Constants.shooterTimeout);
    m_turretController.configNominalOutputReverse(0, Constants.shooterTimeout);
    m_turretController.configPeakOutputForward(1, Constants.shooterTimeout);
    m_turretController.configPeakOutputReverse(-1, Constants.shooterTimeout);
    m_turretController.config_kP(0, 8);
    
    //ledMode.setNumber(1);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putBoolean("Shooter Spinning: ", m_shooterController.getSelectedSensorVelocity() != 0);
    SmartDashboard.putNumber("Shooter Speed", m_shooterController.getSelectedSensorVelocity());
    // SmartDashboard.putNumber("Follower Speed", m_shooterFollower.getSelectedSensorVelocity());
    SmartDashboard.putNumber("Turret Current Position", m_turretController.getSelectedSensorPosition(0));
    //SmartDashboard.putBoolean("Shooter Commanded: ", m_shooterController.getClosedLoopTarget() != 0);
    //System.out.println("Turret Setpoint: " + m_turretController.getClosedLoopTarget(0) + " Turret Position: " + m_turretController.getSelectedSensorPosition());
  }

  public void setShooter(boolean enabled) {
    if(!enabled) {
      m_shooterController.set(ControlMode.PercentOutput, 0.0);
      //ledMode.setNumber(1);
      on = false;
    } else {
      m_shooterController.set(ControlMode.Velocity, m_shooterSpeed);
      ledMode.setNumber(3);
      on = true;
    }
  }

  public void setTurretSpeed(double horizontalSpeed) {
    if (Constants.turretSafetyDisabled) {
      m_turretController.set(ControlMode.PercentOutput, horizontalSpeed * Constants.turretCoefficient);
    }
  }

  public void disableTurret() {
    m_turretController.set(ControlMode.PercentOutput, 0);
    System.out.println("Disabled.");
  }

  public void setTurretAngle(double angle) {
    m_turretController.set(ControlMode.Position, angle);

  }

  public boolean getEnabled() {
    return on;
  }

  public void setManualTurretAngle(double difference) {
    double newTarget = m_turretController.getClosedLoopTarget(0) - difference;
    if(newTarget < 0) {
      newTarget = 0.0;
    }
    if(newTarget > Constants.turretLimit) {
      newTarget = Constants.turretLimit;
    }
    m_turretController.set(ControlMode.Position, newTarget);
  }

  public void resetTurretEncoder() {
    m_turretController.setSelectedSensorPosition(0);
  }

  public boolean isRevLimit() {
    return m_turretController.getSensorCollection().isRevLimitSwitchClosed();
  }

  public void setShooterSpeed(double speed) {
    m_shooterSpeed = speed;
    setShooter(on);
  }

  public double getTx() {
    return limelightTable.getEntry("tx").getDouble(0.0);
  }

  public double getTv() {
    return limelightTable.getEntry("tv").getDouble(0.0);
  }

  public double getTy() {
    return limelightTable.getEntry("ty").getDouble(0.0);
  }
} 
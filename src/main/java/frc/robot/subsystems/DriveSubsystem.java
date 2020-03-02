package frc.robot.subsystems;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.SerialPort.Port;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


public class DriveSubsystem extends SubsystemBase {

  public ADXRS450_Gyro gyro;
  public AHRS navX;
  public Encoder leftEnc;
  public Encoder rightEnc;

  public TalonFX frontLeftController;
  public TalonFX frontRightController;
  public TalonFX backLeftController;
  public TalonFX backRightController;

  public DriveSubsystem() {
    
    frontLeftController = new TalonFX(10);
    frontRightController = new TalonFX(11);
    backLeftController = new TalonFX(12);
    backRightController = new TalonFX(13);

    frontLeftController.configFactoryDefault();
    frontRightController.configFactoryDefault();
    backLeftController.configFactoryDefault();
    backRightController.configFactoryDefault();

    frontLeftController.setSelectedSensorPosition(0, 0, Constants.shooterTimeout);
    frontRightController.setSelectedSensorPosition(0, 0, Constants.shooterTimeout);
    backLeftController.setSelectedSensorPosition(0, 0, Constants.shooterTimeout);
    backRightController.setSelectedSensorPosition(0, 0, Constants.shooterTimeout);

    frontLeftController.setNeutralMode(NeutralMode.Coast);
    frontRightController.setNeutralMode(NeutralMode.Coast);
    backLeftController.setNeutralMode(NeutralMode.Coast);
    backRightController.setNeutralMode(NeutralMode.Coast);

    backLeftController.follow(frontLeftController);
    backRightController.follow(frontRightController);
    backLeftController.setInverted(InvertType.FollowMaster);
    backRightController.setInverted(InvertType.FollowMaster);

    frontLeftController.setInverted(false);
    frontRightController.setInverted(true);

    frontLeftController.config_kF(0, Constants.drivetrainF);
    frontLeftController.config_kP(0, Constants.drivetrainP);
    frontLeftController.config_kI(0, Constants.drivetrainI);
    frontRightController.config_kF(0, Constants.drivetrainF);
    frontRightController.config_kP(0, Constants.drivetrainP);
    frontRightController.config_kI(0, Constants.drivetrainI);
    frontLeftController.configMotionCruiseVelocity(Constants.drivetrainMaxV);
    frontRightController.configMotionCruiseVelocity(Constants.drivetrainMaxV);
    frontLeftController.configMotionAcceleration(Constants.drivetrainMaxA, Constants.shooterTimeout);
    frontRightController.configMotionAcceleration(Constants.drivetrainMaxA, Constants.shooterTimeout);

    frontLeftController.configOpenloopRamp(1.0);
    frontRightController.configOpenloopRamp(1.0);

    SmartDashboard.putNumber("Drive Distance", 1.0);

    gyro = new ADXRS450_Gyro();
   // navX = new AHRS(Port.kUSB);
    //leftEnc = new Encoder(23, 24, false, CounterBase.EncodingType.k4X);
    //rightEnc = new Encoder(21, 22, true, CounterBase.EncodingType.k4X);

    gyro.calibrate();
    gyro.reset();
    //navX.zeroYaw();
    //leftEnc.reset();
    //rightEnc.reset();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Gyro", gyro.getAngle());
    //SmartDashboard.putNumber("NavX", navX.getYaw());
    SmartDashboard.putNumber("Left Encoder", frontLeftController.getSelectedSensorPosition(0));
    //SmartDashboard.putNumber("Right Encoder", rightEnc.get());
  }

  public void drive(double left, double right) {
    frontLeftController.set(ControlMode.PercentOutput, left);
    frontRightController.set(ControlMode.PercentOutput, right);
  }
}

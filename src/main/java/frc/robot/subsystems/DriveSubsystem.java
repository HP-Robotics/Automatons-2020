package frc.robot.subsystems;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.SerialPort.Port;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


public class DriveSubsystem extends SubsystemBase {

  public ADXRS450_Gyro gyro;
  public AHRS navX;
  public Encoder leftEnc;
  public Encoder rightEnc;
  
  SpeedControllerGroup left;
  SpeedControllerGroup right;

  DifferentialDrive tankDrive;

  public DriveSubsystem() {
    
    left = new SpeedControllerGroup(new WPI_TalonSRX(Constants.frontLeftMotorID), new WPI_TalonSRX(Constants.rearLeftMotorID));
    right = new SpeedControllerGroup(new WPI_TalonSRX(Constants.frontRightMotorID), new WPI_TalonSRX(Constants.rearRightMotorID));

    tankDrive = new DifferentialDrive(left, right);

    gyro = new ADXRS450_Gyro();
    navX = new AHRS(Port.kUSB);
    //leftEnc = new Encoder(23, 24, false, CounterBase.EncodingType.k4X);
    //rightEnc = new Encoder(21, 22, true, CounterBase.EncodingType.k4X);

    gyro.calibrate();
    gyro.reset();
    navX.zeroYaw();
    //leftEnc.reset();
    //rightEnc.reset();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Gyro", gyro.getAngle());
    SmartDashboard.putNumber("NavX", navX.getYaw());
    //SmartDashboard.putNumber("Left Encoder", leftEnc.get());
    //SmartDashboard.putNumber("Right Encoder", rightEnc.get());
  }

  public void drive(double left, double right) {
    tankDrive.tankDrive(left, right);
  }
}

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DriveSubsystem extends SubsystemBase {

  public ADXRS450_Gyro gyro;
  public Encoder leftEnc;
  public Encoder rightEnc;
  
  SpeedControllerGroup left;
  SpeedControllerGroup right;

  DifferentialDrive tankDrive;

  public DriveSubsystem() {
    
    left = new SpeedControllerGroup(new WPI_TalonSRX(10), new WPI_TalonSRX(12));
    right = new SpeedControllerGroup(new WPI_TalonSRX(11), new WPI_TalonSRX(13));
    
    tankDrive = new DifferentialDrive(left, right);

    gyro = new ADXRS450_Gyro();
    leftEnc = new Encoder(23, 24, false, CounterBase.EncodingType.k4X);
    rightEnc = new Encoder(21, 22, true, CounterBase.EncodingType.k4X);

    gyro.calibrate();
    gyro.reset();
    leftEnc.reset();
    rightEnc.reset();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Gyro", gyro.getAngle());
    SmartDashboard.putNumber("Left Encoder", leftEnc.get());
    SmartDashboard.putNumber("Right Encoder", rightEnc.get());
  }

  public void drive(double left, double right) {
    tankDrive.tankDrive(left, right);
    System.out.printf("Left Encoder: %06d Right Encoder: %06d \n", leftEnc.get(), rightEnc.get());
  }
}

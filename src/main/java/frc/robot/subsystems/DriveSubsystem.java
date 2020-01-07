package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DriveSubsystem extends SubsystemBase {

  SpeedControllerGroup left;
  SpeedControllerGroup right;

  DifferentialDrive tankDrive;

  public DriveSubsystem() {
    
    left = new SpeedControllerGroup(new WPI_TalonSRX(10), new WPI_VictorSPX(11));
    right = new SpeedControllerGroup(new WPI_TalonSRX(12), new WPI_TalonSRX(13));
    
    tankDrive = new DifferentialDrive(left, right);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void drive(double left, double right) {
    tankDrive.tankDrive(left, right);
  }
}

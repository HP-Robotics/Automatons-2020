package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Encoder;

public class TurretSubsystem extends SubsystemBase {

  TalonSRX turretDriver;
  Encoder angleEncoder;

  public TurretSubsystem() {
    turretDriver = new TalonSRX(20);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void driveSpeed(double speed) {
    turretDriver.set(ControlMode.PercentOutput, speed * 0.5);
  }

  public void driveAngle() {

  }
}

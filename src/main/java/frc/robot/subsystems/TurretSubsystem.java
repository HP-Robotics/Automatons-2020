package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Encoder;

public class TurretSubsystem extends SubsystemBase {

  TalonSRX turretDriver;
  Encoder angleEncoder;

  public TurretSubsystem() {
    turretDriver = new TalonSRX(Constants.turretRingMotorId);
    turretDriver.configFactoryDefault();
    turretDriver.configSelectedFeedbackSensor(FeedbackDevice.PulseWidthEncodedPosition, Constants.turretClosedLoopSensor , Constants.turretSensorTimeout);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    // System.out.println("Turret Encoder raw values: " + turretDriver.getSelectedSensorPosition());
  }

  public void driveSpeed(double speed) {
    turretDriver.set(ControlMode.PercentOutput, speed * Constants.turretCoefficient);
    
  }

  public void driveAngle() {

  }
}

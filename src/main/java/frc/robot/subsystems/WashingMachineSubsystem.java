package frc.robot.subsystems;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import frc.robot.Constants;

public class WashingMachineSubsystem extends SubsystemBase {

  private TalonSRX spinnerMotor;
  private PowerDistributionPanel pdp;

  public WashingMachineSubsystem() {
    spinnerMotor = new TalonSRX(Constants.washingMachineMotorId); //TODO - Make sure to correct device ID
    pdp = new PowerDistributionPanel();
    
    spinnerMotor.configFactoryDefault();

	/* Config sensor used for Primary PID [Velocity] */
    spinnerMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, Constants.washingMachineTimeout);

    /**
	 * Phase sensor accordingly. 
     * Positive Sensor Reading should match Green (blinking) Leds on Talon
     */
	spinnerMotor.setSensorPhase(false);

	/* Config the peak and nominal outputs */
	spinnerMotor.configNominalOutputForward(0, Constants.washingMachineTimeout);
	spinnerMotor.configNominalOutputReverse(0, Constants.washingMachineTimeout);
	spinnerMotor.configPeakOutputForward(1, Constants.washingMachineTimeout);
    spinnerMotor.configPeakOutputReverse(-1, Constants.washingMachineTimeout);


	/* Config the Velocity closed loop gains in slot0 */
	spinnerMotor.config_kF(0, Constants.washingMachineF, Constants.washingMachineTimeout);
	spinnerMotor.config_kP(0, Constants.washingMachineP, Constants.washingMachineTimeout);
	spinnerMotor.config_kI(0, Constants.washingMachineI, Constants.washingMachineTimeout);
	spinnerMotor.config_kD(0, Constants.washingMachineD, Constants.washingMachineTimeout);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Current", pdp.getCurrent(Constants.washingMachinePDPChannel));
  }

  public void setVelocity(double velocity) {
    spinnerMotor.set(ControlMode.Velocity, velocity);
  }
}

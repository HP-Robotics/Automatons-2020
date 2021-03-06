package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.VelocityMeasPeriod;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import frc.robot.Constants;

public class WashingMachineSubsystem extends SubsystemBase {

  private final TalonSRX spinnerMotor;
  private final TalonSRX uptakeMotor;

    public WashingMachineSubsystem() {
        spinnerMotor = new TalonSRX(Constants.washingMachineMotorId);
        uptakeMotor = new TalonSRX(Constants.uptakeMotorId);

        spinnerMotor.configFactoryDefault();
        spinnerMotor.configSupplyCurrentLimit(
                new SupplyCurrentLimitConfiguration(true, Constants.washingMachineLimitedCurrent,
                        Constants.washingMachineMaxCurrent, Constants.washingMachineThresholdTime));

        /* Config sensor used for Primary PID [Velocity] */
        spinnerMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0,
                Constants.washingMachineTimeout);

        /**
         * Phase sensor accordingly. Positive Sensor Reading should match Green
         * (blinking) Leds on Talon
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

        spinnerMotor.configVelocityMeasurementWindow(1);
        spinnerMotor.configVelocityMeasurementPeriod(VelocityMeasPeriod.Period_10Ms);
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        /*
         * SmartDashboard.putNumber("Current",
         * pdp.getCurrent(Constants.washingMachinePDPChannel)); if
         * (pdp.getCurrent(Constants.washingMachinePDPChannel) > 0.0) {
         * System.out.println("Current draw: " +
         * pdp.getCurrent(Constants.washingMachinePDPChannel)); }
         */
        SmartDashboard.putNumber("Wash Velocity", spinnerMotor.getSelectedSensorVelocity());
        SmartDashboard.putNumber("Wash Position", spinnerMotor.getSelectedSensorPosition());
        SmartDashboard.putNumber("Wash Error", spinnerMotor.getClosedLoopError());
    }

    public void setVelocity(final double velocity, final double uptakeOutput) {
      if (velocity == 0.0) {
        spinnerMotor.set(ControlMode.PercentOutput, 0.0);
        uptakeMotor.set(ControlMode.PercentOutput, 0.0);
      }
      else {
        spinnerMotor.set(ControlMode.Velocity, velocity);
        uptakeMotor.set(ControlMode.PercentOutput, uptakeOutput);
      }
    }

    public double getVelocity() {
      return spinnerMotor.getSelectedSensorVelocity(0);
    }
}


package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class IntakeSubsystem extends SubsystemBase {
  /**
   * Creates a new ExampleSubsystem.
   */

  TalonSRX intakeMotor;
  boolean m_state;

  public IntakeSubsystem() {
      intakeMotor = new TalonSRX(Constants.intakeMotorId); //TODO - Discuss Intake CAN ID range
      m_state = false;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void toggle() {
    setEnabled(!m_state);
  }

  public void setEnabled(boolean state) {
    //TODO - Make motor spin
    if (state) {
      m_state = true;
      intakeMotor.set(ControlMode.PercentOutput, Constants.intakeSpeed); //TODO - Discuss speed
    } else {
      m_state = false;
      intakeMotor.set(ControlMode.PercentOutput, 0.0);
    }
  }

}

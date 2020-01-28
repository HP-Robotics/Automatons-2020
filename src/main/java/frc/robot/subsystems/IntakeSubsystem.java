
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

  public IntakeSubsystem() {
      intakeMotor = new TalonSRX(40); //TODO - Discuss Intake CAN ID range
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setEnabled(boolean state) {
      //TODO - Make motor spin
      if (state) {
          intakeMotor.set(ControlMode.PercentOutput, Constants.intakeSpeed); //TODO - Discuss speed
      } else {
          intakeMotor.set(ControlMode.PercentOutput, 0.0);
      }
  }

}

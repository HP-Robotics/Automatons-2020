/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class LIDARSubsystem extends SubsystemBase {
  /**
   * Creates a new LIDARSubsystem.
   */

  private static final int CALIBRATION_OFFSET = -18;

  private Counter counter;
  private int printedWarningCount = 5;
  
  public LIDARSubsystem() {
    counter = new Counter(Constants.lidarId);
    counter.setMaxPeriod(1.0);
    counter.setSemiPeriodMode(true);
    counter.reset();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler 
    SmartDashboard.putNumber("LIDAR Distance", getDistance());
  }

  /**
 * Take a measurement and return the distance in cm
 * 
 * @return Distance in cm
 */
  public double getDistance() {
	  double cm;
	  /* If we haven't seen the first rising to falling pulse, then we have no measurement.
	   * This happens when there is no LIDAR-Lite plugged in, btw.
	   */
	  if (counter.get() < 1) {
	  	if (printedWarningCount-- > 0) {
	  		System.out.println("LidarLitePWM: waiting for distance measurement");
	  	}
	  	return 0;
	  }
	  /* getPeriod returns time in seconds. The hardware resolution is microseconds.
	   * The LIDAR-Lite unit sends a high signal for 10 microseconds per cm of distance.
	   */
	  cm = (counter.getPeriod() * 1000000.0 / 10.0) + CALIBRATION_OFFSET;
	  return cm;
  }
}

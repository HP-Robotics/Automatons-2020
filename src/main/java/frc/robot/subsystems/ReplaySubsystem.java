/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ReplaySubsystem extends SubsystemBase {

  private final DriveSubsystem driveSubsystem;

  private String m_filename;
  private File m_file;
  private FileWriter m_fw;
  private BufferedWriter m_bw;
  private boolean m_open;

  /**
   * Creates a new ReplaySubsystem.
   */
  public ReplaySubsystem(final DriveSubsystem driveSubsystem, final String filename) {
    this.driveSubsystem = driveSubsystem;
    if (!openCSV(filename)) {
      System.out.println("REPLAY FILE FAILED TO OPEN");
    }
  }

  // Literally just record drive outputs
  // Other subsystems should just have commands recorded
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public boolean openCSV(final String filename) {
    if (m_filename == null) {
			return false;
		}
		if(m_open) {
			return true;
		}
		m_filename = filename;
		try {
			if(Files.exists(Paths.get("/home/lvuser"))) {
				m_file = new File(String.format("/home/lvuser/%s", filename));
			} else {
				m_file = new File(String.format("/tmp/%s",filename));
			}
			if(!m_file.exists()) {
				m_file.createNewFile();
			}
			m_fw = new FileWriter(m_file);

		} catch(final IOException e) {
			e.printStackTrace();
			return false;
		}
		m_bw = new BufferedWriter(m_fw);
		m_open = true;

    return writeCSV("left,right");
  }

  public void closeCSV() {
		if (m_open) {
			try {
				m_bw.close();
				m_fw.close();

			} catch(IOException e) {

			}
			m_open = false;
		}
  }
  
  public boolean writeCSV(String s) {
		if(!m_open) {
			return false;
		}
		try {
			m_bw.write(s);
			m_bw.flush();

		} catch(IOException e) {
			System.out.println("Unexpected exception in " + m_file);
			e.printStackTrace();
			return false;
		}
		return true;
	}
}

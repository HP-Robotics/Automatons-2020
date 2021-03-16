/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ReplaySubsystem extends SubsystemBase {

  private File m_file;
  private FileWriter m_fw;
  private BufferedWriter m_bw;
  private FileReader m_fr;
  private BufferedReader m_br;
  private boolean m_open;
  private boolean m_writing;

  /**
   * Creates a new ReplaySubsystem.
   */
  public ReplaySubsystem() {
	  
  }

  // Literally just record drive outputs
  // Other subsystems should just have commands recorded
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public boolean openWritingCSV(final String filename) {
    	if (filename == null) {
			return false;
		}
		if(m_open) {
			return m_writing;
		}
		try {
			if(Files.exists(Paths.get("/home/lvuser"))) {
				m_file = new File(String.format("/home/lvuser/replays/%s", filename));
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
		m_writing = true;
		return writeCSV("left,right,timestamp");
  }

	public void closeCSV() {
		if (m_open) {
			if (m_writing) {
				try {
					m_bw.close();
					m_fw.close();
				} catch(IOException e) {
				}
			} else {
				try {
					m_br.close();
					m_fr.close();
				} catch(IOException e) {
				}
			}
			m_open = false;
		}
	}
  
	public boolean writeCSV(String s) {
		if(!m_open || !m_writing) {
			return false;
		}
		try {
			m_bw.write(s);
			m_bw.write("\n");
			m_bw.flush();

		} catch(IOException e) {
			System.out.println("Unexpected exception in " + m_file);
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean openReadingCSV(String filename) {
		if (filename == null) {
			return false;
		}
		if(m_open) {
			return !m_writing;
		}
		System.out.println("TRYING TO OPEN");
		try {
			if(Files.exists(Paths.get("/home/lvuser"))) {
				m_file = new File(String.format("/home/lvuser/replays/%s", filename));
			} else {
				m_file = new File(String.format("/tmp/%s",filename));
			}
			m_fr = new FileReader(m_file);

		} catch(final IOException e) {
			e.printStackTrace();
			return false;
		}
		m_br = new BufferedReader(m_fr);
		m_open = true;
		m_writing = false;
		try {
			m_br.readLine();
		} catch (IOException e) {
		}
		return true;
	}

	public double[] readLine() {
		if(!m_open || m_writing) {
			return null;
		}
		String line;
		try {
			line = m_br.readLine();
			if(line == null) {
				return null;
			}
		} catch (IOException e) {
			return null;
		}
		String[] values = line.split(",");
		if(values.length < 2) {
			return null;
		}
		double left = Double.parseDouble(values[0]);
		double right = Double.parseDouble(values[1]);
		return new double[]{left, right};
	}
}

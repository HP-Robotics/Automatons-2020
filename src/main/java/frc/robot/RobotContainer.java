/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
 
package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
// import frc.robot.commands.CalibrateDrive;
import frc.robot.commands.DriveCommand;
import frc.robot.commands.DriveLifterCommand;
import frc.robot.commands.DriveSetDistanceCommand;
import frc.robot.commands.DriveWinchCommand;
import frc.robot.commands.GyroDriveCommand;
import frc.robot.commands.CalibrateHood;
import frc.robot.commands.CalibrateTurret;
import frc.robot.commands.HoodCommandManual;
import frc.robot.commands.HoodOffCommand;
import frc.robot.commands.HoodSetCommand;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.LimeLightCommand;
import frc.robot.commands.ReplayDrivingCommand;
import frc.robot.commands.ReverseWasherCommand;
import frc.robot.commands.SaveDrivingCommand;
import frc.robot.commands.ShooterSpeedCommand;
import frc.robot.commands.SmartWasherCommand;
import frc.robot.commands.TurretCommandManual;
import frc.robot.commands.TurretOffCommand;
import frc.robot.commands.TurretSetCommand;
import frc.robot.commands.SpinWasherCommand;
import frc.robot.commands.ToggleIntakeCommand;
import frc.robot.commands.ToggleShooterCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.HoodSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.LIDARSubsystem;
import frc.robot.subsystems.LifterSubsystem;
import frc.robot.subsystems.ReplaySubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.WashingMachineSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final DriveSubsystem m_driveSubsystem = new DriveSubsystem();

  private final Joystick m_driverStickLeft = new Joystick(0); // TODO - split into two controllers, use constants.
  private final Joystick m_driverStickRight = new Joystick(1);
  private final Joystick m_operatorStick = new Joystick(2);

  private  CameraServer m_camera = null;

  private final DriveCommand m_tankDrive = new DriveCommand(m_driveSubsystem, () -> { return Math.pow(-m_driverStickLeft.getRawAxis(1), 3); },
      () -> { return Math.pow(-m_driverStickRight.getRawAxis(1), 3); });

  private final ShooterSubsystem m_shooterSubsystem = new ShooterSubsystem();

  private final IntakeSubsystem m_intakeSubsystem = new IntakeSubsystem();

  private final WashingMachineSubsystem m_washingMachineSubsystem = new WashingMachineSubsystem();

  private final HoodSubsystem m_hoodSubsystem = new HoodSubsystem();

  private final LifterSubsystem m_lifterSubsystem = new LifterSubsystem();

  private final LIDARSubsystem m_lidarSubsystem = new LIDARSubsystem();

  private final ReplaySubsystem m_replaySubsystem = new ReplaySubsystem();

  private final SendableChooser<Command> m_autonomousChooser;
  //private final SendableChooser<String> m_fileChooser;

  // private final SpinWasherCommand m_spinWasherCommand = new
  // SpinWasherCommand(m_washingMachineSubsystem);

  private final Command m_autoDriveForwardCommand = new ParallelCommandGroup(new CalibrateHood(m_hoodSubsystem),
      new CalibrateTurret(m_shooterSubsystem), new DriveSetDistanceCommand(m_driveSubsystem, () -> inchesToTicks(24)));

  private final boolean m_programmerMode = false;
  private final boolean m_bungaMode = false;
  private final boolean m_recordMode = false;
  private final boolean m_replayMode = true;

  // If we shoot first, then move: Hood 750, Shooter, 12000, Turret 2200
  private final Command m_fiveCell = new SequentialCommandGroup(new ToggleShooterCommand(m_shooterSubsystem),
      new ShooterSpeedCommand(m_shooterSubsystem, () -> 14000.0))
          .andThen(new ToggleIntakeCommand(m_intakeSubsystem))
          .andThen(
              new ParallelCommandGroup(new CalibrateHood(m_hoodSubsystem), new CalibrateTurret(m_shooterSubsystem)))
          .andThen(
              new ParallelCommandGroup(new DriveSetDistanceCommand(m_driveSubsystem, () -> inchesToTicks(11.5 * 12)),
                  new HoodSetCommand(m_hoodSubsystem, m_shooterSubsystem, () -> 850.0),
                  new TurretSetCommand(m_shooterSubsystem, () -> 2275.0 + 15))) // TODO Right direction?
          .andThen(new SpinWasherCommand(m_washingMachineSubsystem, () -> Constants.washingMachineSpeed * 0.5)
              .withTimeout(7.5))
          .andThen(new ToggleIntakeCommand(m_intakeSubsystem)).andThen(new ToggleShooterCommand(m_shooterSubsystem));

  private final Command m_fiveCellLimeLight = new SequentialCommandGroup(new ToggleShooterCommand(m_shooterSubsystem),
      new ShooterSpeedCommand(m_shooterSubsystem, () -> 14000.0))
          .andThen(new ToggleIntakeCommand(m_intakeSubsystem))
          .andThen(
              new ParallelCommandGroup(new CalibrateHood(m_hoodSubsystem), new CalibrateTurret(m_shooterSubsystem)))
          .andThen(
              new ParallelCommandGroup(new DriveSetDistanceCommand(m_driveSubsystem, () -> inchesToTicks(11.5 * 12)),
                  new HoodSetCommand(m_hoodSubsystem, m_shooterSubsystem, () -> 850.0),
                  new TurretSetCommand(m_shooterSubsystem, () -> 2275.0 + 15))) // TODO Right direction?
          .andThen(new ParallelCommandGroup(
              new SpinWasherCommand(m_washingMachineSubsystem, () -> Constants.washingMachineSpeed * 0.5)
                  .withTimeout(7.5),
              new LimeLightCommand(m_shooterSubsystem, m_hoodSubsystem).withTimeout(7.5)))
          .andThen(new ToggleIntakeCommand(m_intakeSubsystem)).andThen(new ToggleShooterCommand(m_shooterSubsystem));
  
  private final Command m_fiveCellLimeLightForward = new SequentialCommandGroup(new ToggleShooterCommand(m_shooterSubsystem),
  new ShooterSpeedCommand(m_shooterSubsystem, () -> 14000.0))
      .andThen(new ToggleIntakeCommand(m_intakeSubsystem))
      .andThen(
          new ParallelCommandGroup(new CalibrateHood(m_hoodSubsystem), new CalibrateTurret(m_shooterSubsystem)))
      .andThen(
          new ParallelCommandGroup(new DriveSetDistanceCommand(m_driveSubsystem, () -> inchesToTicks(11.5 * 12)),
              new HoodSetCommand(m_hoodSubsystem, m_shooterSubsystem, () -> 850.0),
              new TurretSetCommand(m_shooterSubsystem, () -> 2275.0 + 15))) // TODO Right direction?
      .andThen(new ParallelCommandGroup(
          //new SpinWasherCommand(m_washingMachineSubsystem, () -> Constants.washingMachineSpeed * 0.5).withTimeout(7.5),
          new SmartWasherCommand(m_washingMachineSubsystem, m_shooterSubsystem).withTimeout(7.5),
          new LimeLightCommand(m_shooterSubsystem, m_hoodSubsystem).withTimeout(7.5),
          new DriveSetDistanceCommand(m_driveSubsystem, () -> inchesToTicks(-3 * 12))))
      .andThen(new ToggleIntakeCommand(m_intakeSubsystem)).andThen(new ToggleShooterCommand(m_shooterSubsystem));

  private final Command m_threeCell = new ToggleShooterCommand(m_shooterSubsystem)
      .andThen(new ParallelCommandGroup(new CalibrateHood(m_hoodSubsystem), new CalibrateTurret(m_shooterSubsystem)))
      .andThen(new ParallelCommandGroup(new HoodSetCommand(m_hoodSubsystem, m_shooterSubsystem, () -> 690.0),
          new TurretSetCommand(m_shooterSubsystem, () -> 2442.0)))
      .andThen(new ShooterSpeedCommand(m_shooterSubsystem, () -> 10000.0)).andThen(new WaitCommand(2))
      .andThen(
          new SpinWasherCommand(m_washingMachineSubsystem, () -> Constants.washingMachineSpeed * 0.75).withTimeout(6))
      .andThen(new ToggleShooterCommand(m_shooterSubsystem))
      .andThen(new DriveSetDistanceCommand(m_driveSubsystem, () -> inchesToTicks(24.0)));

  private final Command m_threeCellLimeLight = new ToggleShooterCommand(m_shooterSubsystem)
      .andThen(new ParallelCommandGroup(new CalibrateHood(m_hoodSubsystem), new CalibrateTurret(m_shooterSubsystem)))
      .andThen(new ParallelCommandGroup(new HoodSetCommand(m_hoodSubsystem, m_shooterSubsystem, () -> 690.0),
          new TurretSetCommand(m_shooterSubsystem, () -> 2442.0)))
      .andThen(new ShooterSpeedCommand(m_shooterSubsystem, () -> 10000.0)).andThen(new WaitCommand(2))
      .andThen(new ParallelCommandGroup(
          //new SpinWasherCommand(m_washingMachineSubsystem, () -> Constants.washingMachineSpeed * 0.75).withTimeout(6),
          new SmartWasherCommand(m_washingMachineSubsystem, m_shooterSubsystem).withTimeout(6),
          new LimeLightCommand(m_shooterSubsystem, m_hoodSubsystem).withTimeout(6)))
      .andThen(new ToggleShooterCommand(m_shooterSubsystem))
      .andThen(new DriveSetDistanceCommand(m_driveSubsystem, () -> inchesToTicks(24.0)));

  private final Command m_autoNav = new ReplayDrivingCommand(m_replaySubsystem, m_driveSubsystem, "auto.csv");

  private Command m_galacticSearch;

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    /*
    m_fileChooser = new SendableChooser<String>();

		if(Files.exists(Paths.get("/home/lvuser/replays"))) {
			File dir = new File("/home/lvuser/replays");
			for(String name : dir.list()) {
				m_fileChooser.setDefaultOption(name, name);
      }
    }

    
    m_fileChooser.setDefaultOption("No file selected", "");
    SmartDashboard.putData("Choose an auto file", m_fileChooser);
    */

    NetworkTable limelightTable = NetworkTableInstance.getDefault().getTable("limelight-yellow");
    if(limelightTable.getEntry("ta").getDouble(0.0) > 0.09) {
      if(limelightTable.getEntry("tx1").getDouble(0.0) < 0) {
        SmartDashboard.putString("Auto Selected", "RED2");
        m_galacticSearch = new ParallelCommandGroup(new ReplayDrivingCommand(m_replaySubsystem, m_driveSubsystem, "gsr2-1001.csv"), new ToggleIntakeCommand(m_intakeSubsystem))
            .andThen(new ToggleIntakeCommand(m_intakeSubsystem));
      } else {
        SmartDashboard.putString("Auto Selected", "RED1");
        m_galacticSearch = new ParallelCommandGroup(new ReplayDrivingCommand(m_replaySubsystem, m_driveSubsystem, "gsr1-948.csv"), new ToggleIntakeCommand(m_intakeSubsystem))
            .andThen(new ToggleIntakeCommand(m_intakeSubsystem));
      }
    } else {
      if(limelightTable.getEntry("tx0").getDouble(0.0) > 0.50) {
        SmartDashboard.putString("Auto Selected", "BLUE1");
        m_galacticSearch = new ParallelCommandGroup(new ReplayDrivingCommand(m_replaySubsystem, m_driveSubsystem, "gsb1988.csv"), new ToggleIntakeCommand(m_intakeSubsystem))
            .andThen(new ToggleIntakeCommand(m_intakeSubsystem));
      } else {
        SmartDashboard.putString("Auto Selected", "BLUE2");
        m_galacticSearch = new ParallelCommandGroup(new ReplayDrivingCommand(m_replaySubsystem, m_driveSubsystem, "blue2.csv"), new ToggleIntakeCommand(m_intakeSubsystem))
            .andThen(new ToggleIntakeCommand(m_intakeSubsystem));
      }
    }

    m_autonomousChooser = new SendableChooser<Command>();
    m_autonomousChooser.addOption("Five Cell Auto", m_fiveCell);
    m_autonomousChooser.addOption("Five Cell Auto (with Limelight)", m_fiveCellLimeLight);
    m_autonomousChooser.addOption("Five Cell Auto (with Limelight and Forward drive", m_fiveCellLimeLightForward);
    m_autonomousChooser.addOption("Three Cell Auto", m_threeCell);
    m_autonomousChooser.addOption("Three Cell Auto (with Limelight)", m_threeCellLimeLight);
    m_autonomousChooser.addOption("Init Line Auto", m_autoDriveForwardCommand);
    m_autonomousChooser.addOption("InstantCommand", new InstantCommand());
    m_autonomousChooser.addOption("GalacticSearch", m_galacticSearch);
    m_autonomousChooser.setDefaultOption("AutoNav", m_autoNav);

    SmartDashboard.putData("Autonomous Mode", m_autonomousChooser);

    SmartDashboard.putNumber("Hood Position", 0.0);
    SmartDashboard.putNumber("Turret Position", 0.0);
    SmartDashboard.putNumber("A shooter speed named desire", 0.0);

    m_camera = CameraServer.getInstance();
    if (m_camera != null) {
      UsbCamera usbCamera = m_camera.startAutomaticCapture();
      if (usbCamera != null) {
        System.out.println("Yay, we have a camera!");
        usbCamera.setResolution(160, 120);
        usbCamera.setFPS(10);
      } else {
        System.out.println("startAutomaticCapture() failed, no USB Camera");
      }
      
    } else {
      System.out.println("CAMERA WAS NOT CONNECTED");
    }

    m_driveSubsystem.setDefaultCommand(m_tankDrive);
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    new JoystickButton(m_driverStickLeft, 2).whileHeld(new GyroDriveCommand(m_driveSubsystem, () -> -m_driverStickLeft.getRawAxis(1))); // Middle Button
    new JoystickButton(m_driverStickRight, 1).whileHeld(new IntakeCommand(m_intakeSubsystem)); // Trigger
  
    new JoystickButton(m_operatorStick, 1).whenHeld(new SpinWasherCommand(m_washingMachineSubsystem, () -> Constants.washingMachineSpeed)); // X
    //new JoystickButton(m_operatorStick, 4).whenHeld(new SpinWasherCommand(m_washingMachineSubsystem, () -> Constants.washingMachineSpeed * 0.5)); //Y
    new JoystickButton(m_operatorStick, 4).whenHeld(new SmartWasherCommand(m_washingMachineSubsystem, m_shooterSubsystem));
    new JoystickButton(m_operatorStick, 2).whenHeld(new ReverseWasherCommand(m_washingMachineSubsystem)); // 
    
    new JoystickButton(m_operatorStick, 8).toggleWhenPressed(new ToggleShooterCommand(m_shooterSubsystem)); // Right Trigger

    new Trigger(this::getRight).whileActiveContinuous(new LimeLightCommand(m_shooterSubsystem, m_hoodSubsystem));
    if(!m_bungaMode) {
      new Trigger(this::getDown).whenActive(new ParallelCommandGroup(new TurretSetCommand(m_shooterSubsystem, () -> 371.0), new HoodSetCommand(m_hoodSubsystem, m_shooterSubsystem, () -> 10.0)));
      new Trigger(this::getLeft).whenActive(new ParallelCommandGroup(new TurretSetCommand(m_shooterSubsystem, () -> 371.0), new HoodSetCommand(m_hoodSubsystem, m_shooterSubsystem, () -> 690)).andThen(new ShooterSpeedCommand(m_shooterSubsystem, () -> 10000.0)));
      new Trigger(this::getUp).whenActive(new ParallelCommandGroup(new TurretSetCommand(m_shooterSubsystem, () -> 2350), new HoodSetCommand(m_hoodSubsystem, m_shooterSubsystem, () -> 875.0)));

      new JoystickButton(m_operatorStick, 5).whileHeld(new DriveLifterCommand(m_lifterSubsystem, false)); // Left Bumper
      new JoystickButton(m_operatorStick, 7).whileHeld(new DriveLifterCommand(m_lifterSubsystem, true)); // Left Trigger

      new JoystickButton(m_operatorStick, 6).whileHeld(new DriveWinchCommand(m_lifterSubsystem)); // Right Bumper

      new JoystickButton(m_driverStickLeft, 8).whenPressed(new ParallelCommandGroup(new HoodSetCommand(m_hoodSubsystem, m_shooterSubsystem, () -> 10.0), new TurretSetCommand(m_shooterSubsystem, () -> 371.0), 
      new DriveSetDistanceCommand(m_driveSubsystem, () -> inchesToTicks(-24.0)))); // Button 8
    } else {
      new JoystickButton(m_driverStickRight, 7).whenPressed(new DriveSetDistanceCommand(m_driveSubsystem, () -> inchesToTicks(Constants.drivingDistance)));
      new JoystickButton(m_driverStickRight, 8).whenPressed(new DriveSetDistanceCommand(m_driveSubsystem, () -> inchesToTicks(-Constants.drivingDistance)));
      new JoystickButton(m_operatorStick, 5).whileHeld(new ReplayDrivingCommand(m_replaySubsystem, m_driveSubsystem, "replay.csv")); // Left Bumper
      new JoystickButton(m_operatorStick, 7).whileHeld(new SaveDrivingCommand(m_replaySubsystem, m_driveSubsystem, "replay.csv")); // Left Trigger

      new JoystickButton(m_driverStickLeft, 11).whenPressed(new DriveSetDistanceCommand(m_driveSubsystem, () -> inchesToTicks(-125)));
      new JoystickButton(m_driverStickLeft, 16).whenPressed(new DriveSetDistanceCommand(m_driveSubsystem, () -> inchesToTicks(125)));
    }

    new Trigger(this::getTurretCoarseTrigger).whileActiveContinuous(new TurretCommandManual(m_shooterSubsystem, () -> m_operatorStick.getRawAxis(2))); // Right joystick horizontal
    new Trigger(this::getHoodCoarseTrigger).whileActiveContinuous(new HoodCommandManual(m_hoodSubsystem, m_shooterSubsystem, () -> m_operatorStick.getRawAxis(3))); // Right joystick vertical
    new Trigger(this::getTurretFineTrigger).whileActiveContinuous(new TurretCommandManual(m_shooterSubsystem, () -> m_operatorStick.getRawAxis(0)*0.1)); // Left joystick horizontal
    new Trigger(this::getHoodFineTrigger).whileActiveContinuous(new HoodCommandManual(m_hoodSubsystem, m_shooterSubsystem, () -> m_operatorStick.getRawAxis(1)*0.1)); // Left joystick vertical

    new JoystickButton(m_driverStickRight, 7).whenPressed(new ParallelCommandGroup(new CalibrateHood(m_hoodSubsystem), new CalibrateTurret(m_shooterSubsystem)));
    new JoystickButton(m_operatorStick, 10).whenPressed(new ParallelCommandGroup(new CalibrateHood(m_hoodSubsystem), new CalibrateTurret(m_shooterSubsystem)));

    //programmer secret buttons
    if(m_programmerMode) {
      new JoystickButton(m_driverStickRight, 5).whenPressed(new TurretSetCommand(m_shooterSubsystem, () -> SmartDashboard.getNumber("Turret Position", -1000.0)));
      new JoystickButton(m_driverStickRight, 10).whenPressed(new TurretOffCommand(m_shooterSubsystem));

      new JoystickButton(m_driverStickRight, 6).whenPressed(new HoodSetCommand(m_hoodSubsystem, m_shooterSubsystem,() -> SmartDashboard.getNumber("Hood Position", 400.0)));
      new JoystickButton(m_driverStickRight, 9).whenPressed(new HoodOffCommand(m_hoodSubsystem));

      new JoystickButton(m_driverStickRight, 14).whenPressed(new ShooterSpeedCommand(m_shooterSubsystem, () -> SmartDashboard.getNumber("A shooter speed named desire", 0.0)));
      // new JoystickButton(m_driverStickRight, 13).whenPressed(new CalibrateDrive(m_driveSubsystem));

      new JoystickButton(m_driverStickRight, 15).whenPressed(new DriveSetDistanceCommand(m_driveSubsystem, () -> inchesToTicks(SmartDashboard.getNumber("Drive Distance", 0))));
    } 
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    if(m_autonomousChooser.getSelected() == null) {
      return new InstantCommand();
    } else {
      return m_autonomousChooser.getSelected();
    }
  }

  public boolean getUp() {
    return m_operatorStick.getPOV() == 0;   
  }

  public boolean getDown() {
    return m_operatorStick.getPOV() == 180;
  }
  
  public boolean getLeft() {
    return m_operatorStick.getPOV() == 270;
  }

  public boolean getRight() {
    return m_operatorStick.getPOV() == 90;    
  }

  public boolean getTurretCoarseTrigger() {
    return Math.abs(m_operatorStick.getRawAxis(2)) > 0.2;
  }

  public boolean getHoodCoarseTrigger() {
    return Math.abs(m_operatorStick.getRawAxis(3)) > 0.2;
  }

  public boolean getTurretFineTrigger() {
    return Math.abs(m_operatorStick.getRawAxis(0)) > 0.2;
  }

  public boolean getHoodFineTrigger() {
    return Math.abs(m_operatorStick.getRawAxis(1)) > 0.2;
  }

  public int inchesToTicks(double input) {
    return (int)((2048*10.71*input)/(6*Math.PI));
  }

  public void startRecording() {
    /*
    if(m_recordMode) {
      new SaveDrivingCommand(m_replaySubsystem, m_driveSubsystem, "seriousbruhmoment420.csv").schedule();
    } else if(m_replayMode) {
      new ReplayDrivingCommand(m_replaySubsystem, m_driveSubsystem, "seriousbruhmoment420.csv").schedule();
    }
    */
  }
}

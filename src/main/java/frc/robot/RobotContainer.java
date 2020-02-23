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
import frc.robot.commands.AutoDriveForwardCommand;
import frc.robot.commands.DriveCommand;
import frc.robot.commands.DriveWinchCommand;
import frc.robot.commands.GyroDriveCommand;
import frc.robot.commands.CalibrateHood;
import frc.robot.commands.CalibrateTurret;
import frc.robot.commands.HoodCommandManual;
import frc.robot.commands.HoodOffCommand;
import frc.robot.commands.HoodSetCommand;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.ReverseWasherCommand;
import frc.robot.commands.TurretCommandManual;
import frc.robot.commands.SpinUpCommand;
import frc.robot.commands.SpinWasherCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.HoodSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.LifterSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.WashingMachineSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final DriveSubsystem m_driveSubsystem = new DriveSubsystem();

  private final Joystick m_driverStickLeft = new Joystick(0); //TODO - split into two controllers, use constants.
  private final Joystick m_driverStickRight = new Joystick(1);
  private final Joystick m_operatorStick = new Joystick(2);

  private final DriveCommand m_tankDrive = new DriveCommand(m_driveSubsystem, () -> -m_driverStickLeft.getRawAxis(1), () -> -m_driverStickRight.getRawAxis(1));

  private final ShooterSubsystem m_shooterSubsystem = new ShooterSubsystem();

  private final IntakeSubsystem m_intakeSubsystem = new IntakeSubsystem();

  private final WashingMachineSubsystem m_washingMachineSubsystem = new WashingMachineSubsystem();

  private final HoodSubsystem m_hoodSubsystem = new HoodSubsystem();

  private final LifterSubsystem m_lifterSubsystem = new LifterSubsystem();

  private final SendableChooser<Command> m_autonomousChooser;
  private final SendableChooser<String> m_stringChooser;

  // private final SpinWasherCommand m_spinWasherCommand = new SpinWasherCommand(m_washingMachineSubsystem);

  private final Command m_autoDriveForwardCommand = new AutoDriveForwardCommand(m_driveSubsystem).withTimeout(2);




  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    m_autonomousChooser = new SendableChooser<Command>();
    m_autonomousChooser.setDefaultOption("Init line forward", m_autoDriveForwardCommand);
    m_autonomousChooser.addOption("InstantCommand", new InstantCommand());
    SmartDashboard.putData("Autonomous Mode", m_autonomousChooser);
    
    SmartDashboard.putNumber("Hood Position", 0.0);


    m_stringChooser = new SendableChooser<String>();
    m_stringChooser.setDefaultOption("Default String", "Hi I am a string. 0");
    m_stringChooser.addOption("String 1", "Hi I am a string. 1");
    m_stringChooser.addOption("String 2", "Hi I am a string. 2");
    m_stringChooser.addOption("String 3", "Hi I am a string. 3");
    m_stringChooser.addOption("String 4", "Hi I am a string. 4");
    SmartDashboard.putData("Choose a string", m_stringChooser);

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
    new JoystickButton(m_operatorStick, 8).toggleWhenPressed(new SpinUpCommand(m_shooterSubsystem));
    new JoystickButton(m_driverStickLeft, 2).whileHeld(new GyroDriveCommand(m_driveSubsystem, () -> -m_driverStickLeft.getRawAxis(1)));
    new JoystickButton(m_driverStickRight, 1).toggleWhenPressed(new IntakeCommand(m_intakeSubsystem));
    new JoystickButton(m_operatorStick, 1).whenHeld(new SpinWasherCommand(m_washingMachineSubsystem)); 
    new JoystickButton(m_operatorStick, 2).whenHeld(new ReverseWasherCommand(m_washingMachineSubsystem));
    new Trigger(this::getUp).whileActiveContinuous(new HoodCommandManual(m_hoodSubsystem, false));
    new Trigger(this::getDown).whileActiveContinuous(new HoodCommandManual(m_hoodSubsystem, true));
    //new Trigger(this::getLeft).whileActiveContinuous(new TurretCommandManual(m_shooterSubsystem, false));
    //new Trigger(this::getRight).whileActiveContinuous(new TurretCommandManual(m_shooterSubsystem, true));

    new JoystickButton(m_operatorStick, 5).whenPressed(new HoodSetCommand(m_hoodSubsystem, () -> SmartDashboard.getNumber("Hood Position", 400.0)));
    new JoystickButton(m_operatorStick, 7).whenPressed(new HoodOffCommand(m_hoodSubsystem));

    new JoystickButton(m_operatorStick, 6).whileHeld(new DriveWinchCommand(m_lifterSubsystem));

    new JoystickButton(m_operatorStick, 4).whenPressed(new ParallelCommandGroup(new CalibrateHood(m_hoodSubsystem), new CalibrateTurret(m_shooterSubsystem)));
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
    return Math.abs(m_operatorStick.getPOV()) <= 45 && m_operatorStick.getPOV() != -1;   
  }

  public boolean getDown() {
    return Math.abs(m_operatorStick.getPOV() - 180) <= 45;    
  }
  
  public boolean getLeft() {
    // if(Math.abs(m_operatorStick.getPOV() - 270) <= 45)
    //   System.out.println("LEFT");
    return Math.abs(m_operatorStick.getPOV() - 270) <= 45;
  }

  public boolean getRight() {
    // if(Math.abs(m_operatorStick.getPOV() - 90) <= 45)
    //   System.out.println("Right");
    return Math.abs(m_operatorStick.getPOV() - 90) <= 45;    
  }
}

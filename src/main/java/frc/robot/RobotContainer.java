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
import frc.robot.commands.DriveCommand;
import frc.robot.commands.GyroDriveCommand;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.TurretCommand;
import frc.robot.commands.SpinUpCommand;
import frc.robot.commands.SpinWasherCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.WashingMachineSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final DriveSubsystem m_driveSubsystem = new DriveSubsystem();

  private final Joystick m_driverStick = new Joystick(0); //TODO - split into two controllers, use constants.
  private final Joystick m_operatorStick = new Joystick(1);

  private final DriveCommand m_tankDrive = new DriveCommand(m_driveSubsystem, () -> m_driverStick.getRawAxis(1), () -> m_driverStick.getRawAxis(3));

  private final ShooterSubsystem m_shooterSubsystem = new ShooterSubsystem();

  private final TurretCommand m_turretCommand = new TurretCommand(m_shooterSubsystem, () -> m_operatorStick.getRawAxis(0), () -> m_operatorStick.getRawAxis(1));

  private final IntakeSubsystem m_intakeSubsystem = new IntakeSubsystem();

  private final WashingMachineSubsystem m_washingMachineSubsystem = new WashingMachineSubsystem();

  private final SendableChooser<Command> m_autonomousChooser;
  private final SendableChooser<String> m_stringChooser;

  // private final SpinWasherCommand m_spinWasherCommand = new SpinWasherCommand(m_washingMachineSubsystem);

  private final Command m_initLineForwardAuto = new InstantCommand();

  

  


  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    m_autonomousChooser = new SendableChooser<Command>();
    m_autonomousChooser.setDefaultOption("Test", new InstantCommand());
    m_autonomousChooser.addOption("Init line forward", m_initLineForwardAuto);
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
    m_shooterSubsystem.setDefaultCommand(m_turretCommand);
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    new JoystickButton(m_driverStick, 4).toggleWhenPressed(new SpinUpCommand(m_shooterSubsystem));
    new JoystickButton(m_driverStick, 3).whileHeld(new GyroDriveCommand(m_driveSubsystem, () -> m_driverStick.getRawAxis(1)));
    new JoystickButton(m_driverStick, 2).toggleWhenPressed(new IntakeCommand(m_intakeSubsystem));
    new JoystickButton(m_driverStick, 1).toggleWhenPressed(new SpinWasherCommand(m_washingMachineSubsystem)); 
    new JoystickButton(m_driverStick, 5).whenPressed(() -> m_shooterSubsystem.setHoodPosition(SmartDashboard.getNumber("Hood Position", 0.0)));
    new JoystickButton(m_driverStick, 7).whenPressed(new InstantCommand(() -> m_shooterSubsystem.setHoodOff()));
  }

  /**S
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    if(m_autonomousChooser.getSelected() == null) {
      return new InstantCommand();
    } else {
      return new InstantCommand();
    }
  }
}

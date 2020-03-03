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
import frc.robot.commands.ReverseWasherCommand;
import frc.robot.commands.ShooterSpeedCommand;
import frc.robot.commands.TurretCommandManual;
import frc.robot.commands.TurretOffCommand;
import frc.robot.commands.TurretSetCommand;
import frc.robot.commands.SpinWasherCommand;
import frc.robot.commands.ToggleIntakeCommand;
import frc.robot.commands.ToggleShooterCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.HoodSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.LifterSubsystem;
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

  private final Command m_autoDriveForwardCommand = new ParallelCommandGroup(new CalibrateHood(m_hoodSubsystem), new CalibrateTurret(m_shooterSubsystem), new DriveSetDistanceCommand(m_driveSubsystem, () -> inchesToTicks(24)));

  private final boolean m_programmerMode = true;

  // If we shoot first, then move: Hood 750, Shooter, 12000, Turret 2200
  private final Command m_fiveCell = new SequentialCommandGroup(new ToggleShooterCommand(m_shooterSubsystem), new ShooterSpeedCommand(m_shooterSubsystem, () -> 14000.0))
    .andThen(new ToggleIntakeCommand(m_intakeSubsystem))
    .andThen(new ParallelCommandGroup(new CalibrateHood(m_hoodSubsystem), new CalibrateTurret(m_shooterSubsystem)))
    .andThen(new ParallelCommandGroup(new DriveSetDistanceCommand(m_driveSubsystem, () -> inchesToTicks(10*12)), new HoodSetCommand(m_hoodSubsystem, m_shooterSubsystem, () -> 850.0), new TurretSetCommand(m_shooterSubsystem, () -> 2245.0)))
    .andThen(new SpinWasherCommand(m_washingMachineSubsystem, () -> Constants.washingMachineSpeed * 0.75).withTimeout(7))
    .andThen(new ToggleIntakeCommand(m_intakeSubsystem))
    .andThen(new ToggleShooterCommand(m_shooterSubsystem));

  private final Command m_threeCell = new ToggleShooterCommand(m_shooterSubsystem)
  .andThen(new ParallelCommandGroup(new CalibrateHood(m_hoodSubsystem), new CalibrateTurret(m_shooterSubsystem)))
  .andThen(new ParallelCommandGroup(new HoodSetCommand(m_hoodSubsystem, m_shooterSubsystem, () -> 690.0), new TurretSetCommand(m_shooterSubsystem, () -> 2442.0)))
  .andThen(new ShooterSpeedCommand(m_shooterSubsystem, () -> 10000.0))
  .andThen(new WaitCommand(2))
  .andThen(new SpinWasherCommand(m_washingMachineSubsystem, () -> Constants.washingMachineSpeed * 0.75).withTimeout(6))
  .andThen(new ToggleShooterCommand(m_shooterSubsystem))
  .andThen(new DriveSetDistanceCommand(m_driveSubsystem, () -> inchesToTicks(24.0)));




  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    m_autonomousChooser = new SendableChooser<Command>();
    m_autonomousChooser.setDefaultOption("Init line forward", m_autoDriveForwardCommand);
    m_autonomousChooser.addOption("InstantCommand", new InstantCommand());
    m_autonomousChooser.addOption("Five Cell Auto", m_fiveCell);
    m_autonomousChooser.addOption("Three Cell Auto", m_threeCell);
    SmartDashboard.putData("Autonomous Mode", m_autonomousChooser);
    
    
    SmartDashboard.putNumber("Hood Position", 0.0);
    SmartDashboard.putNumber("Turret Position", 0.0);
    SmartDashboard.putNumber("A shooter speed named desire", 0.0);


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
    new JoystickButton(m_driverStickLeft, 2).whileHeld(new GyroDriveCommand(m_driveSubsystem, () -> -m_driverStickLeft.getRawAxis(1))); // Middle Button
    new JoystickButton(m_driverStickRight, 1).whileHeld(new IntakeCommand(m_intakeSubsystem)); // Trigger
    
    new JoystickButton(m_driverStickLeft, 8).whenPressed(new ParallelCommandGroup(new HoodSetCommand(m_hoodSubsystem, m_shooterSubsystem, () -> 10.0), new TurretSetCommand(m_shooterSubsystem, () -> 371.0), 
      new DriveSetDistanceCommand(m_driveSubsystem, () -> inchesToTicks(-24.0)))); // Button 8
  
    new JoystickButton(m_operatorStick, 1).whenHeld(new SpinWasherCommand(m_washingMachineSubsystem, () -> Constants.washingMachineSpeed)); // X
    new JoystickButton(m_operatorStick, 2).whenHeld(new ReverseWasherCommand(m_washingMachineSubsystem)); // A

    new Trigger(this::getUp).whenActive(new ParallelCommandGroup(new TurretSetCommand(m_shooterSubsystem, () -> 2350), new HoodSetCommand(m_hoodSubsystem, m_shooterSubsystem, () -> 875.0)));
    new Trigger(this::getRight).whileActiveContinuous(new LimeLightCommand(m_shooterSubsystem, m_hoodSubsystem));
    new Trigger(this::getDown).whenActive(new ParallelCommandGroup(new TurretSetCommand(m_shooterSubsystem, () -> 371.0), new HoodSetCommand(m_hoodSubsystem, m_shooterSubsystem, () -> 10.0)));
    //new Trigger(this::getLeft);

    new Trigger(this::getTurretCoarseTrigger).whileActiveContinuous(new TurretCommandManual(m_shooterSubsystem, () -> m_operatorStick.getRawAxis(2))); // Right joystick horizontal
    new Trigger(this::getHoodCoarseTrigger).whileActiveContinuous(new HoodCommandManual(m_hoodSubsystem, m_shooterSubsystem, () -> m_operatorStick.getRawAxis(3))); // Right joystick vertical
    new Trigger(this::getTurretFineTrigger).whileActiveContinuous(new TurretCommandManual(m_shooterSubsystem, () -> m_operatorStick.getRawAxis(0)*0.1)); // Left joystick horizontal
    new Trigger(this::getHoodFineTrigger).whileActiveContinuous(new HoodCommandManual(m_hoodSubsystem, m_shooterSubsystem, () -> m_operatorStick.getRawAxis(1)*0.1)); // Left joystick vertical


    new JoystickButton(m_operatorStick, 5).whileHeld(new DriveLifterCommand(m_lifterSubsystem, false)); // Left Bumper
    new JoystickButton(m_operatorStick, 7).whileHeld(new DriveLifterCommand(m_lifterSubsystem, true)); // Left Trigger

    new JoystickButton(m_operatorStick, 6).whileHeld(new DriveWinchCommand(m_lifterSubsystem)); // Right Bumper
    new JoystickButton(m_operatorStick, 8).toggleWhenPressed(new ToggleShooterCommand(m_shooterSubsystem)); // Right Trigger

    
    //programmer secret buttons

    if(m_programmerMode) {
      new JoystickButton(m_driverStickRight, 5).whenPressed(new TurretSetCommand(m_shooterSubsystem, () -> SmartDashboard.getNumber("Turret Position", -1000.0)));
      new JoystickButton(m_driverStickRight, 10).whenPressed(new TurretOffCommand(m_shooterSubsystem));

      new JoystickButton(m_driverStickRight, 6).whenPressed(new HoodSetCommand(m_hoodSubsystem, m_shooterSubsystem,() -> SmartDashboard.getNumber("Hood Position", 400.0)));
      new JoystickButton(m_driverStickRight, 9).whenPressed(new HoodOffCommand(m_hoodSubsystem));

      new JoystickButton(m_driverStickRight, 7).whenPressed(new ParallelCommandGroup(new CalibrateHood(m_hoodSubsystem), new CalibrateTurret(m_shooterSubsystem)));
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
}

/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.ctre.phoenix.motion.BufferedTrajectoryPointStream;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.*;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;


public class TrajectoryExperimentCommand extends CommandBase {
  /**
   * Creates a new TrajectoryExperimentCommand.
   */
  DriveSubsystem m_subsystem;

  public TrajectoryExperimentCommand(final DriveSubsystem subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_subsystem = subsystem;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_subsystem.frontLeftController.setSelectedSensorPosition(0, 0, Constants.shooterTimeout);
    m_subsystem.frontRightController.setSelectedSensorPosition(0, 0, Constants.shooterTimeout);
    m_subsystem.frontLeftController.setNeutralMode(NeutralMode.Brake);
    m_subsystem.frontRightController.setNeutralMode(NeutralMode.Brake);

    final DifferentialDriveKinematics kinematics = new DifferentialDriveKinematics(Constants.kTrackwidthMeters);

    // Create a voltage constraint to ensure we don't accelerate too fast
    final var autoVoltageConstraint = new DifferentialDriveVoltageConstraint(new SimpleMotorFeedforward(
        Constants.ksVolts, Constants.kvVoltSecondsPerMeter, Constants.kaVoltSecondsSquaredPerMeter), kinematics, 10);

    // Create config for trajectory
    final TrajectoryConfig config = new TrajectoryConfig(Constants.kMaxSpeedMetersPerSecond,
        Constants.kMaxAccelerationMetersPerSecondSquared)
            // Add kinematics to ensure max speed is actually obeyed
            .setKinematics(kinematics)
            // Apply the voltage constraint
            .addConstraint(autoVoltageConstraint);

    // An example trajectory to follow. All units in meters.
    final Trajectory exampleTrajectory = TrajectoryGenerator.generateTrajectory(
        // Start at the origin facing the +X direction
        new Pose2d(0, 0, new Rotation2d(0)),
        // Pass through these two interior waypoints, making an 's' curve path
        List.of(new Translation2d(1, 1), new Translation2d(2, -1)),
        // End 3 meters straight ahead of where we started, facing forward
        new Pose2d(3, 0, new Rotation2d(0)),
        // Pass config
        config);

    final List<Trajectory.State> states = exampleTrajectory.getStates();

    try {
      final File file = new File("/home/lvuser/t.csv");
      if (!file.exists()) {
        file.createNewFile();
      }
      final FileWriter fw = new FileWriter(file);
      double lpos = 0.0;
      double rpos = 0.0;
      double tick = 0.0;

      final BufferedTrajectoryPointStream lstream = new BufferedTrajectoryPointStream();
      final BufferedTrajectoryPointStream rstream = new BufferedTrajectoryPointStream();

      //m_subsystem.frontLeftController.startMotionProfile(lstream, 10, ControlMode.MotionProfile);
      //m_subsystem.frontRightController.startMotionProfile(rstream, 10, ControlMode.MotionProfile);


      fw.write("time, X, Y, vel, curv, velLeft, velRight, lPos, rPos, lpvel, rpvel, lpoint, rpoint\n");
      for (int i = 0; i < states.size(); i++) {
        final Trajectory.State state = states.get(i);
        final Pose2d pose = state.poseMeters;
        final Translation2d tpose = pose.getTranslation();
        final double radpersec = state.curvatureRadPerMeter * state.velocityMetersPerSecond;
        final ChassisSpeeds cspeed = new ChassisSpeeds(state.velocityMetersPerSecond, 0, radpersec);
        final DifferentialDriveWheelSpeeds wspeeds = kinematics.toWheelSpeeds(cspeed);
        final double delta = state.timeSeconds - tick;
        tick = state.timeSeconds;
        lpos += wspeeds.leftMetersPerSecond * delta;
        rpos += wspeeds.rightMetersPerSecond * delta;

        final TrajectoryPoint lpoint = new TrajectoryPoint();
        final TrajectoryPoint rpoint = new TrajectoryPoint();

        rpoint.timeDur = lpoint.timeDur = (int) Math.round(delta * 1000.0);
        if (i == states.size() - 1) {
          rpoint.isLastPoint = lpoint.isLastPoint = true;
        }
        if (i == 0) {
          rpoint.isLastPoint = lpoint.zeroPos = true;
        }
        lpoint.velocity = metersToTicks(wspeeds.leftMetersPerSecond);
        lpoint.position = metersToTicks(lpos);

        rpoint.velocity = metersToTicks(wspeeds.rightMetersPerSecond);
        rpoint.position = metersToTicks(rpos);

        lstream.Write(lpoint);
        rstream.Write(rpoint);

        fw.write(String.format("%f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f\n", state.timeSeconds, tpose.getX(), tpose.getY(),
          state.velocityMetersPerSecond, state.curvatureRadPerMeter, wspeeds.leftMetersPerSecond, wspeeds.rightMetersPerSecond,
          lpos, rpos, lpoint.velocity, rpoint.velocity, lpoint.position, rpoint.position));
      }
      fw.close();
    } catch (final IOException e) {
      e.printStackTrace();
    }
    System.out.println("Starting Trajectory!");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(final boolean interrupted) {
    m_subsystem.frontLeftController.set(ControlMode.PercentOutput, 0.0);
    m_subsystem.frontRightController.set(ControlMode.PercentOutput, 0.0);
    m_subsystem.frontLeftController.setNeutralMode(NeutralMode.Coast);
    m_subsystem.frontRightController.setNeutralMode(NeutralMode.Coast);
    System.out.println("Ending Trajectory!");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }

  public int metersToTicks(final double input) {
    final double inches = (input * 100) / 2.54;
    return (int)((2048*10.71*inches)/(6*Math.PI));
  }
  
}



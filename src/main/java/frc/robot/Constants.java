/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static final int driverLeft = 0;
    public static final int driverRight = 1;
    public static final int operator = 2;

    public static final int frontLeftMotorID = 10;
    public static final int frontRightMotorID = 11;
    public static final int rearLeftMotorID = 12;
    public static final int rearRightMotorID = 13;

    public static final double washingMachineP = 0.02;
    // public static final double washingMachineI = 0.0002;
    public static final double washingMachineI = 0.0;
    public static final double washingMachineD = 0.0;
    //public static final double washingMachineF = 0.15;
    public static final double washingMachineF = 0.0;
    public static final int washingMachineTimeout = 30;
    public static final double washingMachineSpeed = -28000.0;
    public static final double washingMachineOff = 0.0;
    public static final int washingMachinePDPChannel = 13;
    public static final double washingMachineMaxCurrent = 18.0;
    public static final double washingMachineLimitedCurrent = 5.0;
    public static final double washingMachineThresholdTime = 2.0;

    public static final double shooterP = 0.1; 
    public static final double shooterI = 0.001;
    public static final double shooterD = 0.01;
    public static final double shooterF = 0.053;
    public static final int shooterTimeout = 30;
    public static final double shooterFullspeed = 16000.0; //7000.0;
    public static final double shooterOff = 0.0;
    public static final double shooterSpeedSlope = 14.3184421535;
    public static final double shooterSpeedAtTwo = 5356.81557847;

    public static final boolean turretSafetyDisabled = true; // TODO FIX THIS
    public static final double turretLimit = 2800;
    public static final double hoodCoefficient = 1;
    public static final double turretOff = 0.0;
    public static final double hoodOff = 0.0;
    // public static final double hoodP = 150;
    // public static final double hoodI = 0.01;
    // public static final double hoodD = 300;
    public static final double hoodP = 2.5;
    public static final double hoodI = 0.005;
    public static final double hoodD = 100;
    public static final int hoodIntegralZone = 100;
    public static final double hoodRamp = 0.25;
    public static final double hoodBottom = 1100;
    public static final double hoodTop = 400;
    public static final double hoodSlope = -11.173;
    public static final double hoodIntercept = 627.73;
    public static final int hoodAbsoluteEncoder = 2;
    public static final int turretClosedLoopSensor = 0;
    public static final int turretSensorTimeout = 100;
    public static final int turretAxis = 0;
    public static final int hoodMovement = -25;
    public static final int turretMovement = -20;
    public static final double johnsonTicks = -177.6;
    public static final double absZeroOffset = 116.8;

    public static final int turretRingMotorId = 20;
    public static final int hoodMotorId = 21;
    public static final int washingMachineMotorId = 30;
    public static final int uptakeMotorId = 31; // 31
    public static final int shooterMotor1Id = 40;
    public static final int shooterMotor2Id = 41;
    public static final int intakeMotorId = 50;
    public static final int winchMotorId = 60;
    public static final int lifterMotorId = 61;

    public static final double intakeSpeed = -0.75;
    public static final double uptakeReverse = 0.25;

    public static final double winchSpeed = 1.0;
    public static final double winchStop = 0.0;
    public static final double lifterSpeed = 0.5;

    public static final double drivetrainF = 0.06;
    public static final double drivetrainP = 0.2;
    public static final double drivetrainI = 0.001;
    public static final int drivetrainMaxV = 8000;
    public static final int drivetrainMaxA = 10000;


    /* ########################################################### *
     * #              ENTER THE REALM OF AUTONOMOUS              # *
     * ########################################################### */

    public static final double initAutoForwardTime = 1.5;
}

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

    public static final double washingMachineP = 0.01;
    public static final double washingMachineI = 0.0002;
    public static final double washingMachineD = 0.0;
    //public static final double washingMachineF = 0.15;
    public static final double washingMachineF = 0.0;
    public static final int washingMachineTimeout = 30;
    public static final double washingMachineSpeed = 20000.0;
    public static final double washingMachineOff = 0.0;
    public static final int washingMachinePDPChannel = 13;
    public static final double washingMachineMaxCurrent = 18.0;
    public static final double washingMachineLimitedCurrent = 5.0;
    public static final double washingMachineThresholdTime = 2.0;

    public static final double turretCoefficient = 0.5;
    public static final int turretAxis = 0;

    public static final double shooterFullspeed = -1.0;
    public static final double shooterOff = 0.0;

    public static final int turretRingMotorId = 20;
    public static final int washingMachineMotorId = 30;
}

package org.igutech.utils;

import com.acmerobotics.dashboard.config.Config;

@Config
public class MagicValues {
    public static double deliverServoUp = 0.93;
    public static double deliverServoDown = 0.55;

    public static double holderServoPush=0.68;
    public static double holderServoDown=0.5;
    public static double holderServoUp=0.15;
    public static double holderServoPushShared=0.54;

    public static double colorSensorGain = 2.0;

    public static double autoMotorPowerForward=0.25;
    public static double autoMotorPowerBackward=-0.2;

    public static double collectDriveTimeFirst =1500;
    public static double collectDriveTimeSecond =1750;
    public static double collectTime = 2000;
    public static double collectStopTime = 2750;

    public static double intakePower = 0.4;
}

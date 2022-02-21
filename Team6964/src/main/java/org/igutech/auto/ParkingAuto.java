package org.igutech.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.igutech.auto.roadrunner.SampleMecanumDrive;
import org.igutech.config.Hardware;
import org.igutech.teleop.modules.TimerService;

@Autonomous
public class ParkingAuto extends LinearOpMode {
    private Hardware hardware;
    SampleMecanumDrive drive;
    @Override
    public void runOpMode() throws InterruptedException {
        drive = new SampleMecanumDrive(hardwareMap);
        telemetry.addData("ready","true");
        waitForStart();
        drive.setMotorPowers(0.3,0.3,0.3,0.3);
        sleep(2000);
        drive.setMotorPowers(0.0,0.0,0.0,0.0);

    }
}

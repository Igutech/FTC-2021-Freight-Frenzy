package org.igutech.auto.roadrunner;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.igutech.auto.roadrunner.trajectorysequence.TrajectorySequence;
import org.igutech.config.Hardware;

@Autonomous
public class RedAutoPath extends LinearOpMode {
    private Hardware hardware;

    @Override
    public void runOpMode() throws InterruptedException {
        hardware = new Hardware(hardwareMap);
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        TrajectorySequence trajSeq = drive.trajectorySequenceBuilder(new Pose2d(-40, -60, Math.toRadians(90)))
                        .strafeLeft(15)
                        .lineToSplineHeading(new Pose2d(-15, -40, Math.toRadians(250)))

                        .lineToLinearHeading(new Pose2d(20, -60))

                        .lineToLinearHeading(new Pose2d(55, -60))
                        .lineToLinearHeading(new Pose2d(10, -60, Math.toRadians(0)))
                        .lineToLinearHeading(new Pose2d(-5, -40, Math.toRadians(-50)))
                        .build();


        while (!opModeIsActive() && !isStopRequested()) {
            telemetry.addData("Status: ", "Ready");
            telemetry.update();

        }
        if (isStopRequested()) return;
        drive.followTrajectorySequenceAsync(trajSeq);
        while (!isStopRequested() && opModeIsActive()) {
            drive.update();
            telemetry.addData("Pose", drive.getPoseEstimate());
            telemetry.update();
        }
    }
}

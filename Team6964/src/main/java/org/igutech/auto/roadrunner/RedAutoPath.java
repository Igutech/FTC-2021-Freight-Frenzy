package org.igutech.auto.roadrunner;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.igutech.auto.roadrunner.trajectorysequence.TrajectorySequence;
import org.igutech.config.Hardware;
import org.igutech.teleop.Teleop;
import org.igutech.teleop.modules.Delivery;
import org.igutech.teleop.modules.TimerService;

@Autonomous
@Config
public class RedAutoPath extends LinearOpMode {
    private Hardware hardware;
    private TimerService timerService;
    private Delivery delivery;
    public static double x=-68;
    public static double y=-45;
    public static double theta=-90;

    @Override
    public void runOpMode() throws InterruptedException {
        hardware = new Hardware(hardwareMap,false);
        timerService = new TimerService();
        delivery = new Delivery(hardware,false);
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Pose2d startPose = new Pose2d(-40, -60, Math.toRadians(0));
        hardware.getServos().get("deliveryServo").setPosition(0.73);
        hardware.getServos().get("holderServo").setPosition(0.36);

        timerService.init();
        delivery.init();
        waitForStart();
        timerService.start();
        delivery.start();

        if (isStopRequested()) return;
        drive.setPoseEstimate(startPose);


        TrajectorySequence trajectorySequence = drive.trajectorySequenceBuilder(startPose)
                .lineToSplineHeading(new Pose2d(-25, -22, Math.toRadians(220)))
                .addDisplacementMarker(()->delivery.setCurrentDeliveryState(Delivery.DeliveryState.HIGH))
                .back(0.5)
                .waitSeconds(2)
                .addDisplacementMarker(()->{
                    hardware.getServos().get("deliveryServo").setPosition(0.93);
                    timerService.registerUniqueTimerEvent(500, "holderServoUp", () -> {
                        hardware.getServos().get("holderServo").setPosition(0.36);
                    });
                })
                .back(0.5)
                .waitSeconds(1)
                .addDisplacementMarker(()->delivery.setCurrentDeliveryState(Delivery.DeliveryState.WAITING))
                .lineToLinearHeading(new Pose2d(x,y,Math.toRadians(theta)))
                .addDisplacementMarker(()->hardware.getMotors().get("spinner").setPower(0.65))
                .forward(0.5)
                .waitSeconds(2)
                .addDisplacementMarker(()->hardware.getMotors().get("spinner").setPower(0))
                .lineToLinearHeading(new Pose2d(-10,-50,Math.toRadians(0)))
                .lineToLinearHeading(new Pose2d(30,-58,Math.toRadians(0)))
                .build();


//        drive.followTrajectory(first);
//        drive.followTrajectory(second);
//       hardware.getMotors().get("spinner").setPower(0.65);
//        sleep(2000);

//        Pose2d poseEstimate = drive.getPoseEstimate();
//        telemetry.addData("finalX", poseEstimate.getX());
//        telemetry.addData("finalY", poseEstimate.getY());
//        telemetry.addData("finalHeading", poseEstimate.getHeading());
//        telemetry.update();
        drive.followTrajectorySequenceAsync(trajectorySequence);
        while (!isStopRequested() && opModeIsActive()){
            drive.update();
            timerService.loop();
            delivery.loop();
        }
    }
}

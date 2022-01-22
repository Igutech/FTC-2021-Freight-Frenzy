package org.igutech.auto.roadrunner;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.igutech.auto.roadrunner.trajectorysequence.TrajectorySequence;
import org.igutech.auto.vision.BarcodePipeline;
import org.igutech.config.Hardware;
import org.igutech.teleop.Teleop;
import org.igutech.teleop.modules.Delivery;
import org.igutech.teleop.modules.TimerService;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@Autonomous
@Config
public class RedAutoPath extends LinearOpMode {
    private Hardware hardware;
    private TimerService timerService;
    private Delivery delivery;
    public static double x=-72;
    public static double y=-53;
    public static double theta=-90;
    public static double forwardTemp=20;
    public static double backTemp=10;
    public static int pattern=2;
    public static Delivery.DeliveryState deliveryState = Delivery.DeliveryState.HIGH;
    OpenCvCamera phoneCam;
    @Override
    public void runOpMode() throws InterruptedException {
        hardware = new Hardware(hardwareMap,false);
        timerService = new TimerService();
        delivery = new Delivery(hardware,timerService,false);
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Pose2d startPose = new Pose2d(-40, -60, Math.toRadians(0));
        hardware.getServos().get("deliveryServo").setPosition(0.73);
        hardware.getServos().get("holderServo").setPosition(0.65);

        timerService.init();
        delivery.init();


        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        BarcodePipeline pipeline = new BarcodePipeline();
        phoneCam.setPipeline(pipeline);

        phoneCam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                /*
                 * Tell the camera to start streaming images to us! Note that you must make sure
                 * the resolution you specify is supported by the camera. If it is not, an exception
                 * will be thrown.
                 *
                 * Also, we specify the rotation that the camera is used in. This is so that the image
                 * from the camera sensor can be rotated such that it is always displayed with the image upright.
                 * For a front facing camera, rotation is defined assuming the user is looking at the screen.
                 * For a rear facing camera or a webcam, rotation is defined assuming the camera is facing
                 * away from the user.
                 */
                phoneCam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {
                System.out.println("Error has occuered: "+errorCode);
            }
        });

        while(!opModeIsActive() && !isStopRequested()){
            telemetry.addData("ready","");
            pattern = pipeline.pattern;
            telemetry.addData("pattern",pattern);
            telemetry.update();
        }



        timerService.start();
        delivery.start();

        if (isStopRequested()) return;
        drive.setPoseEstimate(startPose);
        if(pattern==1){
            deliveryState= Delivery.DeliveryState.LOW;
        }else if(pattern==2){
            deliveryState= Delivery.DeliveryState.MIDDLE;
        }else{
            forwardTemp=1;
            backTemp=2;
            deliveryState= Delivery.DeliveryState.HIGH;
            }
        TrajectorySequence trajectorySequence;

        if(pattern==3){
            trajectorySequence = drive.trajectorySequenceBuilder(startPose)
                    .lineToSplineHeading(new Pose2d(-10, -55, Math.toRadians(270)))
                    .lineToLinearHeading(new Pose2d(-10, -35, Math.toRadians(270)))
                    .forward(1)
                    //.lineToSplineHeading(new Pose2d(x, y, Math.toRadians(theta)))
                    .addDisplacementMarker(()->delivery.setCurrentDeliveryState(deliveryState))
                    .forward(1)
                    .waitSeconds(2)
                    .back(2)
                    .waitSeconds(2)
                    .addDisplacementMarker(()->{
                        hardware.getServos().get("deliveryServo").setPosition(0.93);
                    })
                    .back(0.5)
                    .waitSeconds(1)
                    .addDisplacementMarker(()->delivery.setCurrentDeliveryState(Delivery.DeliveryState.WAITING))
                    .lineToLinearHeading(new Pose2d(-72,-53,Math.toRadians(-90)))
                    .addDisplacementMarker(()->hardware.getMotors().get("spinner").setPower(0.4))
                    .forward(0.5)
                    .waitSeconds(3)
                    .addDisplacementMarker(()->hardware.getMotors().get("spinner").setPower(0))
                    .lineToLinearHeading(new Pose2d(-20,-55,Math.toRadians(0)))
                    .lineToLinearHeading(new Pose2d(40,-62,Math.toRadians(0)))
                    .build();
        }else{
            trajectorySequence = drive.trajectorySequenceBuilder(startPose)
                    .lineToSplineHeading(new Pose2d(-25, -22, Math.toRadians(220)))
                    .forward(20)
                    //.lineToSplineHeading(new Pose2d(x, y, Math.toRadians(theta)))
                    .addDisplacementMarker(()->delivery.setCurrentDeliveryState(deliveryState))
                    .forward(1)
                    .waitSeconds(2)
                    .back(10)
                    .waitSeconds(2)
                    .addDisplacementMarker(()->{
                        hardware.getServos().get("deliveryServo").setPosition(0.93);
                    })
                    .back(0.5)
                    .waitSeconds(1)
                    .addDisplacementMarker(()->delivery.setCurrentDeliveryState(Delivery.DeliveryState.WAITING))
                    .lineToLinearHeading(new Pose2d(-68,-44,Math.toRadians(-90)))
                    .addDisplacementMarker(()->hardware.getMotors().get("spinner").setPower(0.4))
                    .forward(0.5)
                    .waitSeconds(3)
                    .addDisplacementMarker(()->hardware.getMotors().get("spinner").setPower(0))
                    .lineToLinearHeading(new Pose2d(-20,-50,Math.toRadians(0)))
                    .lineToLinearHeading(new Pose2d(40,-58,Math.toRadians(0)))
                    .build();
        }



        /*left to right
           1 to bottom
           2 to middle
           3 to high
         */
        drive.followTrajectorySequenceAsync(trajectorySequence);
        while (!isStopRequested() && opModeIsActive()){
            drive.update();
            timerService.loop();
            delivery.loop();
        }
    }
}

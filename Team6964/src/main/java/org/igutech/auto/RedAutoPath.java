package org.igutech.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.igutech.auto.redstates.GoToHub;
import org.igutech.auto.roadrunner.SampleMecanumDrive;
import org.igutech.auto.vision.BarcodePipeline;
import org.igutech.config.Hardware;
import org.igutech.teleop.modules.ColorDetection;
import org.igutech.teleop.modules.Delivery;
import org.igutech.teleop.modules.Intake;
import org.igutech.teleop.modules.TimerService;
import org.igutech.utils.MagicValues;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

import dev.raneri.statelib.StateLibrary;

@Autonomous
@Config
public class RedAutoPath extends LinearOpMode {
    private Hardware hardware;
    private TimerService timerService;
    private Delivery delivery;
    private Intake intake;
    private ColorDetection colorDetection;
    private SampleMecanumDrive drive;
    private RedTrajectory redTrajectories;
    public static int pattern = 3;
    public static Delivery.DeliveryState deliveryState = Delivery.DeliveryState.HIGH;
    private OpenCvCamera phoneCam;
    private Pose2d startPose = new Pose2d(10, -60, Math.toRadians(-90));

    @Override
    public void runOpMode() throws InterruptedException {
        hardware = new Hardware(hardwareMap, false);
        timerService = new TimerService();
        delivery = new Delivery(hardware, timerService, false);
        intake = new Intake(hardware, false);
        colorDetection = new ColorDetection(hardware);
        drive = new SampleMecanumDrive(hardwareMap);
        redTrajectories = new RedTrajectory(drive,startPose);
        redTrajectories.init();
        hardware.getServos().get("deliveryServo").setPosition(0.73);
        hardware.getServos().get("holderServo").setPosition(MagicValues.holderServoDown);

        timerService.init();
        delivery.init();
        colorDetection.init();
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        BarcodePipeline pipeline = new BarcodePipeline();
        phoneCam.setPipeline(pipeline);

        phoneCam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                phoneCam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
                System.out.println("Error has occurred: " + errorCode);
            }
        });
        StateLibrary transitioner = new StateLibrary();
        transitioner.addStateTransitionHandler(event -> {
            if (event.getFinalState() == null) {
                return;
            }
            System.out.println("Exiting " + event.getInitialState().getClass().getName() + " and going into " + event.getFinalState().getClass().getName());
        });
        transitioner.addLoopStartHandler(event -> System.out.println("Current state: " + event.getState().getClass().getName() + " HSV VALUE: " + colorDetection.getHsvValues()[2]));

        while (!opModeIsActive() && !isStopRequested()) {
            telemetry.addData("ready", "");
            //pattern = pipeline.pattern;
            telemetry.addData("pattern", pattern);
            telemetry.update();
        }


        timerService.start();
        delivery.start();

        if (isStopRequested()) return;
        transitioner.init(new GoToHub(this, startPose));
        drive.setPoseEstimate(startPose);




        /*left to right
           1 to bottom
           2 to middle
           3 to high
         */
        while (!isStopRequested() && opModeIsActive()) {
            delivery.loop();
            transitioner.loop();
            drive.update();
            timerService.loop();
            colorDetection.loop();

            telemetry.addData("pose estimate", drive.getPoseEstimate());
            telemetry.update();
        }
    }

    public Hardware getHardware() {
        return hardware;
    }

    public TimerService getTimerService() {
        return timerService;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public static int getPattern() {
        return pattern;
    }

    public SampleMecanumDrive getDrive() {
        return drive;
    }

    public ColorDetection getColorDetection() {
        return colorDetection;
    }

    public Intake getIntake() {
        return intake;
    }

    public RedTrajectory getRedTrajectories() {
        return redTrajectories;
    }
}

package org.igutech.auto.roadrunner.tuning;
//
//import com.acmerobotics.dashboard.FtcDashboard;
//import com.acmerobotics.dashboard.config.Config;
//import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.DcMotor;
//
//import org.firstinspires.ftc.robotcore.external.Telemetry;
//import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
//import org.igutech.auto.roadrunner.SampleMecanumDrive;
//
///**
// * This is a simple teleop routine for debugging your motor configuration.
// * Pressing each of the buttons will power its respective motor.
// * <p>
// * Button Mappings:
// * <p>
// * Xbox/PS4 Button - Motor
// * X / ▢         - Front Left
// * Y / Δ         - Front Right
// * B / O         - Rear  Right
// * A / X         - Rear  Left
// * The buttons are mapped to match the wheels spatially if you
// * were to rotate the gamepad 45deg°. x/square is the front left
// * ________        and each button corresponds to the wheel as you go clockwise
// * / ______ \
// * ------------.-'   _  '-..+              Front of Bot
// * /   _  ( Y )  _  \                  ^
// * |  ( X )  _  ( B ) |     Front Left   \    Front Right
// * ___  '.      ( A )     /|       Wheel       \      Wheel
// * .'    '.    '-._____.-'  .'       (x/▢)        \     (Y/Δ)
// * |       |                 |                      \
// * '.___.' '.               |          Rear Left    \   Rear Right
// * '.             /             Wheel       \    Wheel
// * \.          .'              (A/X)        \   (B/O)
// * \________/
// * <p>
// * Uncomment the @Disabled tag below to use this opmode.
// */
//@Config
//@TeleOp(group = "drive")
//public class MotorDirectionDebugger extends LinearOpMode {
//    public static double MOTOR_POWER = 0.7;
//    DcMotor frontLeft;
//    DcMotor frontRight;
//    DcMotor backLeft;
//    DcMotor backRight;
//
//    @Override
//    public void runOpMode() throws InterruptedException {
//        telemetry = new MultipleTelemetry(telemetry, telemetry);
//        frontLeft = hardwareMap.dcMotor.get("frontleft");
//        frontRight = hardwareMap.dcMotor.get("frontright");
//        backLeft = hardwareMap.dcMotor.get("backleft");
//        backRight = hardwareMap.dcMotor.get("backright");
//        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
//
//        telemetry.addLine("Press play to begin the debugging opmode");
//        telemetry.update();
//
//        waitForStart();
//
//        if (isStopRequested()) return;
//
//        telemetry.clearAll();
//        telemetry.setDisplayFormat(Telemetry.DisplayFormat.HTML);
//
//        while (!isStopRequested()) {
//            telemetry.addLine("Press each button to turn on its respective motor");
//            telemetry.addLine();
//            telemetry.addLine("<font face=\"monospace\">Xbox/PS4 Button - Motor</font>");
//            telemetry.addLine("<font face=\"monospace\">&nbsp;&nbsp;X / ▢&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Front Left</font>");
//            telemetry.addLine("<font face=\"monospace\">&nbsp;&nbsp;Y / Δ&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Front Right</font>");
//            telemetry.addLine("<font face=\"monospace\">&nbsp;&nbsp;B / O&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Rear&nbsp;&nbsp;Right</font>");
//            telemetry.addLine("<font face=\"monospace\">&nbsp;&nbsp;A / X&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Rear&nbsp;&nbsp;Left</font>");
//            telemetry.addLine();
//
//            if (gamepad1.x) {
//                frontLeft.setPower(MOTOR_POWER);
//                backLeft.setPower(0);
//                frontRight.setPower(0);
//                backRight.setPower(0);
//                // drive.setMotorPowers(MOTOR_POWER, 0, 0, 0);
//                telemetry.addLine("Running Motor: Front Left");
//                telemetry.addData("Running Motor Front Left Velo: ", drive.getLeftFront().getVelocity());
//                telemetry.addData("Voltage: ", drive.getLeftRear().getCurrent(CurrentUnit.MILLIAMPS));
//            } else if (gamepad1.y) {
//                frontLeft.setPower(0);
//                backLeft.setPower(0);
//                frontRight.setPower(MOTOR_POWER);
//                backRight.setPower(0);
//                telemetry.addLine("Running Motor: Front Right");
//                telemetry.addData("Running Motor: Front Right Velo: ", drive.getRightFront().getVelocity());
//                telemetry.addData("Voltage: ", drive.getLeftRear().getCurrent(CurrentUnit.MILLIAMPS));
//
//            } else if (gamepad1.b) {
//                frontLeft.setPower(0);
//                backLeft.setPower(0);
//                frontRight.setPower(0);
//                backRight.setPower(MOTOR_POWER);
//                telemetry.addLine("Running Motor: Rear Right");
//                telemetry.addData("Running Motor: Rear Right Velo: ", drive.getRightRear().getVelocity());
//                telemetry.addData("Voltage: ", drive.getLeftRear().getCurrent(CurrentUnit.MILLIAMPS));
//
//            } else if (gamepad1.a) {
//                frontLeft.setPower(0);
//                backLeft.setPower(MOTOR_POWER);
//                frontRight.setPower(0);
//                backRight.setPower(0);
//                telemetry.addLine("Running Motor: Rear Left");
//                telemetry.addData("Running Motor: Rear Left Velo: ", drive.getLeftRear().getVelocity());
//                telemetry.addData("Voltage: ", drive.getLeftRear().getCurrent(CurrentUnit.MILLIAMPS));
//            } else {
//                //drive.setMotorPowers(0, 0, 0, 0);
//                frontLeft.setPower(0);
//                backLeft.setPower(0);
//                frontRight.setPower(0);
//                backRight.setPower(0);
//                telemetry.addLine("Running Motor: None");
//            }
//            telemetry.update();
//        }
//    }
//}
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.igutech.auto.roadrunner.SampleMecanumDrive;

/**
 * This is a simple teleop routine for debugging your motor configuration.
 * Pressing each of the buttons will power its respective motor.
 *
 * Button Mappings:
 *
 * Xbox/PS4 Button - Motor
 *   X / ▢         - Front Left
 *   Y / Δ         - Front Right
 *   B / O         - Rear  Right
 *   A / X         - Rear  Left
 *                                    The buttons are mapped to match the wheels spatially if you
 *                                    were to rotate the gamepad 45deg°. x/square is the front left
 *                    ________        and each button corresponds to the wheel as you go clockwise
 *                   / ______ \
 *     ------------.-'   _  '-..+              Front of Bot
 *              /   _  ( Y )  _  \                  ^
 *             |  ( X )  _  ( B ) |     Front Left   \    Front Right
 *        ___  '.      ( A )     /|       Wheel       \      Wheel
 *      .'    '.    '-._____.-'  .'       (x/▢)        \     (Y/Δ)
 *     |       |                 |                      \
 *      '.___.' '.               |          Rear Left    \   Rear Right
 *               '.             /             Wheel       \    Wheel
 *                \.          .'              (A/X)        \   (B/O)
 *                  \________/
 *
 * Uncomment the @Disabled tag below to use this opmode.
 */
@Disabled
@Config
@TeleOp(group = "drive")
public class MotorDirectionDebugger extends LinearOpMode {
    public static double MOTOR_POWER = 0.7;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, telemetry);

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        telemetry.addLine("Press play to begin the debugging opmode");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        telemetry.clearAll();
        telemetry.setDisplayFormat(Telemetry.DisplayFormat.HTML);

        while (!isStopRequested()) {
            telemetry.addLine("Press each button to turn on its respective motor");
            telemetry.addLine();
            telemetry.addLine("<font face=\"monospace\">Xbox/PS4 Button - Motor</font>");
            telemetry.addLine("<font face=\"monospace\">&nbsp;&nbsp;X / ▢&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Front Left</font>");
            telemetry.addLine("<font face=\"monospace\">&nbsp;&nbsp;Y / Δ&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Front Right</font>");
            telemetry.addLine("<font face=\"monospace\">&nbsp;&nbsp;B / O&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Rear&nbsp;&nbsp;Right</font>");
            telemetry.addLine("<font face=\"monospace\">&nbsp;&nbsp;A / X&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Rear&nbsp;&nbsp;Left</font>");
            telemetry.addLine();

            if(gamepad1.x) {
                drive.setMotorPowers(MOTOR_POWER, 0, 0, 0);
                telemetry.addLine("Running Motor: Front Left");

            } else if(gamepad1.y) {
                drive.setMotorPowers(0, 0, 0, MOTOR_POWER);
                telemetry.addLine("Running Motor: Front Right");

            } else if(gamepad1.b) {
                drive.setMotorPowers(0, 0, MOTOR_POWER, 0);
                telemetry.addLine("Running Motor: Rear Right");

            } else if(gamepad1.a) {
                drive.setMotorPowers(0, MOTOR_POWER, 0, 0);
                telemetry.addLine("Running Motor: Rear Left");

            } else {
                drive.setMotorPowers(0, 0, 0, 0);
                telemetry.addLine("Running Motor: None");
            }
            telemetry.addData("Front Left Position ",drive.getLeftFront().getCurrentPosition());
            telemetry.addData("Front Left Velo ",drive.getLeftFront().getVelocity());
            telemetry.addData("Front Right Position ",drive.getRightFront().getCurrentPosition());
            telemetry.addData("Front right Velo ",drive.getRightFront().getVelocity());
            telemetry.addData("Rear right Position ",drive.getRightRear().getCurrentPosition());
            telemetry.addData("Rear right Velo ",drive.getRightRear().getVelocity());
            telemetry.addData("Rear Left Position ",drive.getLeftRear().getCurrentPosition());
            telemetry.addData("Rear Left Velo ",drive.getLeftRear().getVelocity());
            //telemetry.update();
            telemetry.update();
        }
    }
}
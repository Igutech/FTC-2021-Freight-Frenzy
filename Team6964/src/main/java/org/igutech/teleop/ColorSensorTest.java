package org.igutech.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class ColorSensorTest extends LinearOpMode {
    // Define a variable for our color sensor
    ColorSensor color;

    @Override
    public void runOpMode() {
        // Get the color sensor from hardwareMap
        color = hardwareMap.get(ColorSensor.class, "Color");

        // Wait for the Play button to be pressed
        waitForStart();

        // While the Op Mode is running, update the telemetry values.
        while (opModeIsActive()) {

            telemetry.addData("Red", color.red());
            FtcDashboard.getInstance().getTelemetry().addData("Red", color.red());
            telemetry.addData("Green", color.green());
            FtcDashboard.getInstance().getTelemetry().addData("Green", color.green());
            telemetry.addData("Blue", color.blue());
            FtcDashboard.getInstance().getTelemetry().addData("Blue", color.blue());
            telemetry.update();
            FtcDashboard.getInstance().getTelemetry().update();
        }
    }
}
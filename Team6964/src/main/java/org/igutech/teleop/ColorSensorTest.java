package org.igutech.teleop;

import android.graphics.Color;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp
@Config
public class ColorSensorTest extends LinearOpMode {
    // Define a variable for our color sensor
    NormalizedColorSensor colorSensor;
    public static double gain = 2;
    final float[] hsvValues = new float[3];

    @Override
    public void runOpMode() {
        // Get the color sensor from hardwareMap
        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "Color");
        if (colorSensor instanceof SwitchableLight) {
            ((SwitchableLight) colorSensor).enableLight(true);
        }
        // Wait for the Play button to be pressed
        waitForStart();

        // While the Op Mode is running, update the telemetry values.
        while (opModeIsActive()) {
            //TODO set this in init
            colorSensor.setGain((float) gain);
            NormalizedRGBA colors = colorSensor.getNormalizedColors();
            Color.colorToHSV(colors.toColor(), hsvValues);
            telemetry.addLine()
                    .addData("Red", "%.3f", colors.red)
                    .addData("Green", "%.3f", colors.green)
                    .addData("Blue", "%.3f", colors.blue);

            telemetry.addLine()
                    .addData("Hue", "%.3f", hsvValues[0])
                    .addData("Saturation", "%.3f", hsvValues[1])
                    .addData("Value", "%.3f", hsvValues[2]);
            telemetry.addData("Alpha", "%.3f", colors.alpha);

            FtcDashboard.getInstance().getTelemetry().addLine()
                    .addData("Red", "%.3f", colors.red)
                    .addData("Green", "%.3f", colors.green)
                    .addData("Blue", "%.3f", colors.blue);
            FtcDashboard.getInstance().getTelemetry().addLine()
                    .addData("Hue", "%.3f", hsvValues[0])
                    .addData("Saturation", "%.3f", hsvValues[1])
                    .addData("Value", "%.3f", hsvValues[2]);
            telemetry.addData("Alpha", "%.3f", colors.alpha);
            /* If this color sensor also has a distance sensor, display the measured distance.
             * Note that the reported distance is only useful at very close range, and is impacted by
             * ambient light and surface reflectivity. */
            if (colorSensor instanceof DistanceSensor) {
                telemetry.addData("Distance (cm)", "%.3f", ((DistanceSensor) colorSensor).getDistance(DistanceUnit.CM));
                FtcDashboard.getInstance().getTelemetry().addData("Distance (cm)", "%.3f", ((DistanceSensor) colorSensor).getDistance(DistanceUnit.CM));
            }

            telemetry.update();
            FtcDashboard.getInstance().getTelemetry().update();
        }
    }
}
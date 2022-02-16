package org.igutech.teleop.modules;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;

import org.igutech.config.Hardware;
import org.igutech.teleop.Module;
import org.igutech.utils.MagicValues;

public class ColorDetection extends Module {
    private Hardware hardware;
    private final float[] hsvValues = new float[3];

    public ColorDetection(Hardware hardware) {

        super(10, "ColorDetection");
        this.hardware = hardware;
    }

    @Override
    public void init() {
        hardware.getColorSensors().get("colorSensor").setGain((float) MagicValues.colorSensorGain);
        if (hardware.getColorSensors().get("colorSensor") instanceof SwitchableLight) {
            ((SwitchableLight) hardware.getColorSensors().get("colorSensor")).enableLight(true);
        }
    }

    public NormalizedRGBA getRGBA() {
        return hardware.getColorSensors().get("colorSensor").getNormalizedColors();

    }

    @Override
    public void loop() {
        NormalizedRGBA colors = hardware.getColorSensors().get("colorSensor").getNormalizedColors();
        Color.colorToHSV(colors.toColor(), hsvValues);
    }

    public float[] getHsvValues() {
        return hsvValues;
    }
}

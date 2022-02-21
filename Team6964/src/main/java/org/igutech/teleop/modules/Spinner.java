package org.igutech.teleop.modules;

import com.acmerobotics.dashboard.config.Config;

import org.igutech.config.Hardware;
import org.igutech.teleop.Module;
import org.igutech.teleop.Teleop;
import org.igutech.utils.ButtonToggle;

@Config
public class Spinner extends Module {
    private GamepadService gamepadService;
    public double pow = 0.4;
    private Hardware hardware;
    private ButtonToggle duckToggleRed;
    private ButtonToggle duckToggleBlue;

    public Spinner(Hardware hardware) {
        super(200, "Spinner");
        this.hardware = hardware;
        duckToggleRed = new ButtonToggle(2, "left_bumper", () -> {
            hardware.getMotors().get("spinner").setPower(0.6);
        }, () -> {
            hardware.getMotors().get("spinner").setPower(0);
        });
        duckToggleBlue = new ButtonToggle(2,"right_bumper",()->{
            hardware.getMotors().get("spinner").setPower(-0.6);
        }, () -> {
            hardware.getMotors().get("spinner").setPower(0);
        });

    }


    @Override
    public void init() {
        duckToggleRed.init();
        duckToggleBlue.init();
        gamepadService = (GamepadService) Teleop.getInstance().getService("GamepadService");
    }

    @Override
    public void loop() {
        duckToggleRed.loop();
        duckToggleBlue.loop();

    }
}



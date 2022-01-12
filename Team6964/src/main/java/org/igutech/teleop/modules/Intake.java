package org.igutech.teleop.modules;

import org.igutech.teleop.Module;
import org.igutech.teleop.Teleop;

public class Intake extends Module {
    private GamepadService gamepadService;

    public Intake() {
        super(700, "Intake");
    }

    @Override
    public void init() {
        gamepadService = (GamepadService) Teleop.getInstance().getService("GamepadService");

    }

    @Override
    public void loop() {
        double power = gamepadService.getAnalog(1, "left_trigger");
        if (power > 0.05) {
            power = Math.max(power,0.5);
        }
        power = power * -1;
        Teleop.getInstance().getHardware().getMotors().get("intake").setPower(power);
    }
}

package org.igutech.teleop.modules;

import org.igutech.teleop.Module;
import org.igutech.teleop.Teleop;
import org.igutech.utils.ButtonToggle;

public class Delivery extends Module {
    private GamepadService gamepadService;
    private ButtonToggle safety;
    private boolean enable = false;

    public Delivery() {
        super(500, "Delivery");
    }

    @Override
    public void init() {
        gamepadService = (GamepadService) Teleop.getInstance().getService("GamepadService");
        safety = new ButtonToggle(1, "a", () -> enable = !enable, () -> enable = !enable);
        safety.init();
    }


    @Override
    public void loop() {
        safety.init();
        double power = gamepadService.getAnalog(1, "right_stick_y");
        power = Math.min(power, 0.3);
        Teleop.getInstance().telemetry.addData("Is Enabled ",enable);
        Teleop.getInstance().telemetry.addData("Power ",power);
        if(enable){
            Teleop.getInstance().getHardware().getMotors().get("delivery").setPower(power);
        }

    }
}

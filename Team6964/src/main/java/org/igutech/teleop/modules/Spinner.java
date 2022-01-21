package org.igutech.teleop.modules;

import com.acmerobotics.dashboard.config.Config;

import org.igutech.teleop.Module;
import org.igutech.teleop.Teleop;
@Config
public class Spinner extends Module {
    private GamepadService gamepadService;
    public static double maxPow = 0.65;
    public Spinner() {
        super(200, "Spinner");
    }

    @Override
    public void init() {
        gamepadService = (GamepadService) Teleop.getInstance().getService("GamepadService");
    }

    @Override
    public void loop() {
        //double power = gamepadService.getAnalog(2,"left_trigger");
        if(gamepadService.getDigital(2,"left_bumper")){
            Teleop.getInstance().getHardware().getMotors().get("spinner").setPower(maxPow);
        }else if(gamepadService.getDigital(2,"right_bumper")){
            Teleop.getInstance().getHardware().getMotors().get("spinner").setPower(1);
        }else{
            Teleop.getInstance().getHardware().getMotors().get("spinner").setPower(0);
        }
    }
}

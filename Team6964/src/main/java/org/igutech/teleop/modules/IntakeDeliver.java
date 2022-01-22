package org.igutech.teleop.modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.igutech.teleop.Module;
import org.igutech.teleop.Teleop;
import org.igutech.utils.ButtonToggle;

@Config
public class IntakeDeliver extends Module {
    private GamepadService gamepadService;
    private TimerService timerService;
    private Delivery deliveryInstance;
    private Intake intakeInstance;
    private ButtonToggle intakeLiftToggle;
    private ButtonToggle deliveryToggle;
    private ButtonToggle deliveryServoToggle;

    public IntakeDeliver() {
        super(600, "IntakeDeliver");
    }

    @Override
    public void init() {
        gamepadService = (GamepadService) Teleop.getInstance().getService("GamepadService");
        timerService = (TimerService) Teleop.getInstance().getService("TimerService");
        deliveryInstance = (Delivery) Teleop.getInstance().getModuleByName("Delivery");
        intakeInstance = (Intake) Teleop.getInstance().getModuleByName("Intake");

        intakeLiftToggle = new ButtonToggle(1, "left_bumper", () -> {
            intakeInstance.setIntakeLiftState(true);

             intakeInstance.setIntakeState(Intake.IntakeState.AUTO);
        }, () -> {
            intakeInstance.setIntakeLiftState(false);
            intakeInstance.setIntakeState(Intake.IntakeState.MANUAL);
            timerService.registerUniqueTimerEvent(250, "activateHolderServo", () -> Teleop.getInstance().getHardware().getServos().get("holderServo").setPosition(0.65));
        });

        deliveryToggle = new ButtonToggle(1, "right_bumper", () -> deliveryInstance.setDeliveryStatus(true), () -> {
            Teleop.getInstance().getHardware().getServos().get("deliveryServo").setPosition(0.73);
            deliveryInstance.setDeliveryStatus(false);
        });

        deliveryServoToggle = new ButtonToggle(1, "dpad_right", () -> {
            Teleop.getInstance().getHardware().getServos().get("deliveryServo").setPosition(0.93);
            timerService.registerUniqueTimerEvent(500, "holderServoUp", () -> {
                Teleop.getInstance().getHardware().getServos().get("holderServo").setPosition(0.36);
            });
        });

        intakeLiftToggle.init();
        deliveryToggle.init();
        deliveryServoToggle.init();
    }

    @Override
    public void start() {
        Teleop.getInstance().getHardware().getMotors().get("intakeLift").setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Teleop.getInstance().getHardware().getMotors().get("intakeLift").setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void loop() {

        intakeLiftToggle.loop();
        deliveryToggle.loop();
        deliveryServoToggle.loop();


    }


}

package org.igutech.teleop.modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.igutech.teleop.Module;
import org.igutech.teleop.Teleop;
import org.igutech.utils.ButtonToggle;
import org.igutech.utils.MagicValues;
import org.igutech.utils.events.CycleableAction;

@Config
public class IntakeDeliver extends Module {
    private GamepadService gamepadService;
    private TimerService timerService;
    private Delivery deliveryInstance;
    private Intake intakeInstance;
    private ButtonToggle intakeLiftToggle;
    private ButtonToggle deliveryToggle;
    private ButtonToggle deliveryServoToggle;
    private ButtonToggle sharedShippingHubToggle;
    private CycleableAction sharedHubActions;

    public IntakeDeliver() {
        super(600, "IntakeDeliver");
    }

    @Override
    public void init() {
        gamepadService = (GamepadService) Teleop.getInstance().getService("GamepadService");
        timerService = (TimerService) Teleop.getInstance().getService("TimerService");
        deliveryInstance = (Delivery) Teleop.getInstance().getModuleByName("Delivery");
        intakeInstance = (Intake) Teleop.getInstance().getModuleByName("Intake");
        sharedHubActions = new CycleableAction(
                () -> {
                    intakeInstance.setShareShippingHubActive(true);
                    intakeInstance.setIntakeState(Intake.IntakeState.SHARED);
                    Teleop.getInstance().getHardware().getServos().get("holderServo").setPosition(MagicValues.holderServoPush);
                    timerService.registerUniqueTimerEvent(750, "intakeLiftUp", () -> intakeInstance.setIntakeLiftState(Intake.IntakeLiftState.UP));
                },
                () -> {
                    Teleop.getInstance().getHardware().getServos().get("holderServo").setPosition(MagicValues.holderServoPushShared);
                    timerService.registerSingleTimerEvent(300, () -> {
                        intakeInstance.setIntakeState(Intake.IntakeState.OFF);
                        Teleop.getInstance().getHardware().getMotors().get("intake").setPower(MagicValues.intakePower);
                    });
                },
                () -> {
                    intakeInstance.setShareShippingHubActive(false);
                    intakeInstance.setIntakeLiftState(Intake.IntakeLiftState.DOWN);
                    intakeInstance.setIntakeState(Intake.IntakeState.MANUAL);
                    timerService.registerSingleTimerEvent(250, () -> {
                        Teleop.getInstance().getHardware().getServos().get("holderServo").setPosition(MagicValues.holderServoUp);
                    });

                }
        );
        intakeLiftToggle = new ButtonToggle(1, "left_bumper", () -> {
            intakeInstance.setIntakeLiftState(Intake.IntakeLiftState.UP);
            intakeInstance.setIntakeState(Intake.IntakeState.AUTO);
        }, () -> {
            intakeInstance.setIntakeLiftState(Intake.IntakeLiftState.DOWN);
            intakeInstance.setIntakeState(Intake.IntakeState.MANUAL);
            timerService.registerUniqueTimerEvent(250, "activateHolderServo", () -> Teleop.getInstance().getHardware().getServos().get("holderServo").setPosition(MagicValues.holderServoDown));
            Teleop.getInstance().getHardware().getMotors().get("intake").setPower(0);

        });

        deliveryToggle = new ButtonToggle(1, "right_bumper", () -> {
            Teleop.getInstance().getHardware().getServos().get("holderServo").setPosition(MagicValues.holderServoDown);
            deliveryInstance.setDeliveryStatus(true);
        }, () -> {
            Teleop.getInstance().getHardware().getServos().get("holderServo").setPosition(MagicValues.holderServoUp);
            Teleop.getInstance().getHardware().getServos().get("deliveryServo").setPosition(MagicValues.deliverServoDown);
            deliveryInstance.setDeliveryStatus(false);
        });

        deliveryServoToggle = new ButtonToggle(1, "dpad_right", () -> {
            Teleop.getInstance().getHardware().getServos().get("deliveryServo").setPosition(0.93);
            Teleop.getInstance().getHardware().getServos().get("holderServo").setPosition(MagicValues.holderServoPush);

        });

        sharedShippingHubToggle = new ButtonToggle(1, "a", () -> sharedHubActions.call());

        intakeLiftToggle.init();
        deliveryToggle.init();
        deliveryServoToggle.init();
        sharedShippingHubToggle.init();
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
        sharedShippingHubToggle.loop();


    }


}

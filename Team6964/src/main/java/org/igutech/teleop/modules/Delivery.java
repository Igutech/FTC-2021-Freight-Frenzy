package org.igutech.teleop.modules;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.igutech.config.Hardware;
import org.igutech.teleop.Module;
import org.igutech.teleop.Teleop;
import org.igutech.utils.ButtonToggle;
import org.igutech.utils.control.PIDFController;

import java.util.HashMap;

@Config
public class Delivery extends Module {
    private GamepadService gamepadService;
    private TimerService timerService;
    private ButtonToggle highToggle;
    private ButtonToggle middleToggle;
    private ButtonToggle lowToggle;
    private PIDFController controller;
    private Hardware hardware;
    public static double p = 0.005;
    public static double i = 0;
    public static double d = 0;
    public static double f = 0;
    public static double maxPower = 0.8;
    private boolean teleop;

    public Delivery(Hardware hardware, TimerService timerService, boolean teleop) {

        super(500, "Delivery");
        this.timerService = timerService;
        this.hardware = hardware;
        this.teleop = teleop;
    }

    HashMap<Integer, Integer> position;
    private DeliveryState extendDeliveryState = DeliveryState.HIGH;
    private DeliveryState currentDeliveryState = DeliveryState.OFF;
    private ButtonToggle deliveryToggle;
    private ButtonToggle holderToggle;
    private ButtonToggle safety;
    private ButtonToggle encoderReset;
    public static int targetPosition;
    public static int marginError = 300;

    @Override
    public void init() {
        controller = new PIDFController(p, i, d, f);
        if (teleop) {
            gamepadService = (GamepadService) Teleop.getInstance().getService("GamepadService");
            highToggle = new ButtonToggle(1, "dpad_up", () -> extendDeliveryState = DeliveryState.HIGH, () -> extendDeliveryState = DeliveryState.HIGH);
            middleToggle = new ButtonToggle(1, "dpad_left", () -> extendDeliveryState = DeliveryState.MIDDLE, () -> extendDeliveryState = DeliveryState.MIDDLE);
            lowToggle = new ButtonToggle(1, "dpad_down", () -> extendDeliveryState = DeliveryState.LOW, () -> extendDeliveryState = DeliveryState.LOW);

            highToggle.init();
            middleToggle.init();
            lowToggle.init();
//            hardware.getServos().get("deliveryServo").setPosition(0.73);
//            hardware.getServos().get("holderServo").setPosition(0.36);
            hardware.getServos().get("deliveryServo").setPosition(0.73);
            hardware.getServos().get("holderServo").setPosition(0.65);

            deliveryToggle = new ButtonToggle(2,"y",
                    ()->hardware.getServos().get("deliveryServo").setPosition(0.93),
                    ()->hardware.getServos().get("deliveryServo").setPosition(0.73));
            deliveryToggle.init();

            holderToggle = new ButtonToggle(2,"a",
                    ()->hardware.getServos().get("holderServo").setPosition(0.36),
                    ()->hardware.getServos().get("holderServo").setPosition(0.65));
            holderToggle.init();

            safety = new ButtonToggle(2,"dpad_up",()->{},()->{});
            safety.init();
            encoderReset = new ButtonToggle(2,"x",()->{
                hardware.getMotors().get("delivery").setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                hardware.getMotors().get("delivery").setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                safety.setState(false);
            });
            encoderReset.init();

        }

        position = new HashMap<>();
        position.put(-2, -100);
        position.put(-1, 0);
        position.put(0, 0);
        position.put(1, -580);
        position.put(2, -520);
        position.put(3, -360);
        controller.init();


    }

    @Override
    public void start() {
        hardware.getMotors().get("delivery").setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hardware.getMotors().get("delivery").setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void loop() {
        controller.setPIDFValues(p, i, d, f);
        targetPosition = position.get(currentDeliveryState.value);

        double currentPos = hardware.getMotors().get("delivery").getCurrentPosition();
        switch (currentDeliveryState) {
            case LOW:
                if (Math.abs(currentPos - targetPosition) <= marginError) {
                    hardware.getMotors().get("delivery").setPower(0.0);
                } else {
                    updatePID(targetPosition, currentPos);
                }
                break;
            case MIDDLE:
                updatePID(targetPosition,currentPos);
                break;
            case HIGH:
                updatePID(targetPosition, currentPos);
                break;
            case WAITING:
                if (Math.abs(currentPos - targetPosition) <= 340) {
                    hardware.getMotors().get("delivery").setPower(0.0);
                } else {
                    updatePID(targetPosition, currentPos);
                }
                if (Math.abs(currentPos - targetPosition) <= 10) {
                    currentDeliveryState = DeliveryState.SAFETY;
                }
                break;
            case SAFETY:
                updatePID(targetPosition, currentPos);
                timerService.registerUniqueTimerEvent(500, "Safety", () -> currentDeliveryState = DeliveryState.OFF);
                break;
            case OFF:
                hardware.getMotors().get("delivery").setPower(0.0);
                break;


        }





        FtcDashboard.getInstance().getTelemetry().addData("Delivery Position ", currentPos);
        FtcDashboard.getInstance().getTelemetry().addData("Delivery Target ", targetPosition);
        FtcDashboard.getInstance().getTelemetry().addData("Delivery Current State ", currentDeliveryState);
        FtcDashboard.getInstance().getTelemetry().addData("Delivery Extend State ", extendDeliveryState);

        if (teleop) {
            if(safety.getState()){
                hardware.getMotors().get("delivery").setPower(gamepadService.getAnalog(2,"left_stick_y"));
            }
            highToggle.loop();
            middleToggle.loop();
            lowToggle.loop();
            deliveryToggle.loop();
            holderToggle.loop();
            encoderReset.loop();
            safety.loop();
            Teleop.getInstance().telemetry.addData("Current Delivery State ", currentDeliveryState);
            Teleop.getInstance().telemetry.addData("Extend Delivery State ", extendDeliveryState);
            Teleop.getInstance().telemetry.addData("Delivery Position ", currentPos);
            Teleop.getInstance().telemetry.addData("Delivery Target ", targetPosition);
        }

    }

    public void setCurrentDeliveryState(DeliveryState currentDeliveryState) {
        this.currentDeliveryState = currentDeliveryState;
    }

    public void setDeliveryStatus(boolean status) {
        if (status) {
            currentDeliveryState = extendDeliveryState;
        } else {
            currentDeliveryState = DeliveryState.WAITING;
        }
    }

    private void updatePID(double targetPosition, double currentPosition) {
        controller.updateSetpoint(targetPosition);
        double pow = controller.update(currentPosition);
        pow = Math.min(pow, maxPower);
        pow = Math.max(pow, -1 * maxPower);

        hardware.getMotors().get("delivery").setPower(pow);

        //Teleop.getInstance().telemetry.addData("Delivery Power ", pow);
        FtcDashboard.getInstance().getTelemetry().addData("Delivery Power ", pow);

    }

    public enum DeliveryState {
        HIGH(3),
        MIDDLE(2),
        LOW(1),
        WAITING(0),
        OFF(-1),
        SAFETY(-2);
        public int value;

        DeliveryState(int value) {
            this.value = value;
        }
    }

    public DeliveryState getCurrentDeliveryState() {
        return currentDeliveryState;
    }
}

package org.igutech.teleop.modules;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;

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
    private ButtonToggle mainToggle;
    private ButtonToggle servoToggle;
    private PIDFController controller;
    public static double p = 0.005;
    public static double i = 0;
    public static double d = 0;
    public static double f = 0;
    public static double maxPower = 0.5;

    public Delivery() {
        super(500, "Delivery");
    }

    HashMap<Integer, Integer> position;
    private State extendState = State.HIGH;
    private State currentState = State.OFF;
    public static int targetPosition;
    public static int marginError = 280;

    @Override
    public void init() {
        controller = new PIDFController(p, i, d, f);
        gamepadService = (GamepadService) Teleop.getInstance().getService("GamepadService");
        timerService = (TimerService) Teleop.getInstance().getService("TimerService");
        highToggle = new ButtonToggle(1, "dpad_up", () -> extendState = State.HIGH, () -> extendState = State.HIGH);
        middleToggle = new ButtonToggle(1, "dpad_left", () -> extendState = State.MIDDLE, () -> extendState = State.MIDDLE);
        lowToggle = new ButtonToggle(1, "dpad_down", () -> extendState = State.LOW, () -> extendState = State.LOW);
        mainToggle = new ButtonToggle(1, "right_bumper", () -> {
            currentState = extendState;
        }, () -> currentState = State.WAITING);
        servoToggle = new ButtonToggle(1, "dpad_right", () -> {
            timerService.registerUniqueTimerEvent(500, "servoUp", () -> Teleop.getInstance().getHardware().getServos().get("deliveryServo").setPosition(1.0));
            timerService.registerUniqueTimerEvent(500, "servoDown", () -> Teleop.getInstance().getHardware().getServos().get("deliveryServo").setPosition(0.73));
        });
        servoToggle.init();
        highToggle.init();
        middleToggle.init();
        lowToggle.init();
        mainToggle.init();
        position = new HashMap<>();
        position.put(-1, 0);
        position.put(0, 0);
        position.put(1, -550);
        position.put(2, -500);
        position.put(3, -310);
        controller.init();
        Teleop.getInstance().getHardware().getServos().get("deliveryServo").setPosition(0.73);

    }

    @Override
    public void start() {
        Teleop.getInstance().getHardware().getMotors().get("delivery").setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Teleop.getInstance().getHardware().getMotors().get("delivery").setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void loop() {
        controller.setPIDFValues(p, i, d, f);
        targetPosition = position.get(currentState.value);
        double currentPos = Teleop.getInstance().getHardware().getMotors().get("delivery").getCurrentPosition();
        switch (currentState) {
            case LOW:
            case MIDDLE:
            case HIGH:
                updatePID(targetPosition, currentPos);
                break;
            case WAITING:
                if (Math.abs(currentPos - targetPosition) <= marginError) {
                    currentState = State.OFF;
                }
                updatePID(targetPosition, currentPos);
                break;
            case OFF:
                Teleop.getInstance().getHardware().getMotors().get("delivery").setPower(0.0);
                break;

        }
//        double po = gamepadService.getAnalog(2,"right_stick_y");
//        Teleop.getInstance().getHardware().getMotors().get("delivery").setPower(po);

        // Teleop.getInstance().telemetry.addData("SETTING POWER ", po);

//        Teleop.getInstance().telemetry.addData("Position ", currentPos);
//        Teleop.getInstance().telemetry.addData("Target ", targetPosition);
        Teleop.getInstance().telemetry.addData("Current Delivery State ", currentState);
        Teleop.getInstance().telemetry.addData("Extend Delivery State ", extendState);

        FtcDashboard.getInstance().getTelemetry().addData("Position ", currentPos);
        FtcDashboard.getInstance().getTelemetry().addData("Target ", targetPosition);
        FtcDashboard.getInstance().getTelemetry().addData("Current State ", currentState);
        FtcDashboard.getInstance().getTelemetry().addData("Extend State ", extendState);


        highToggle.loop();
        middleToggle.loop();
        lowToggle.loop();
        mainToggle.loop();
        servoToggle.loop();
    }

    private void updatePID(double targetPosition, double currentPosition) {
        controller.updateSetpoint(targetPosition);
        double pow = controller.update(currentPosition);
        pow = Math.min(pow, maxPower);
        pow = Math.max(pow, -1 * maxPower);

        Teleop.getInstance().getHardware().getMotors().get("delivery").setPower(pow);

        // Teleop.getInstance().telemetry.addData("Power ", pow);
        FtcDashboard.getInstance().getTelemetry().addData("Delivery Power ", pow);

    }

    private enum State {
        HIGH(3),
        MIDDLE(2),
        LOW(1),
        WAITING(0),
        OFF(-1);
        public int value;

        State(int value) {
            this.value = value;
        }
    }
}

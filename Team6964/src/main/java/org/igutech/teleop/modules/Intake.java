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
public class Intake extends Module {
    public static double p = 0.001;
    public static double i = 0;
    public static double d = 0;
    public static double f = 0;
    public static double maxPower = 0.5;
    private GamepadService gamepadService;
    private ButtonToggle buttonToggle;
    private ButtonToggle safety;
    private IntakeState state = IntakeState.OFF;
    private HashMap<Integer, Integer> positions;
    private PIDFController controller;

    public Intake() {
        super(700, "Intake");
    }

    @Override
    public void init() {
        gamepadService = (GamepadService) Teleop.getInstance().getService("GamepadService");
        buttonToggle = new ButtonToggle(1, "left_bumper", () -> state=IntakeState.UP, () -> {
            state=IntakeState.DOWN;
        });
        buttonToggle.init();
        positions = new HashMap<>();
        positions.put(0, 0);
        positions.put(1, 0);
        positions.put(2, 130);
        controller = new PIDFController(p, i, d, f);
        controller.init();
        safety = new ButtonToggle(1, "a", () -> {
        }, () -> {
        });
        safety.init();

    }

    @Override
    public void start() {
        Teleop.getInstance().getHardware().getMotors().get("intakeLift").setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Teleop.getInstance().getHardware().getMotors().get("intakeLift").setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void loop() {

        double po = gamepadService.getAnalog(2,"right_stick_y");
        Teleop.getInstance().getHardware().getMotors().get("intakeLift").setPower(po);

         Teleop.getInstance().telemetry.addData("SETTING intake lift POWER ", po);

        controller.setPIDFValues(p, i, d, f);
        controller.updateSetpoint(positions.get(state.value));
        double currentPos = Teleop.getInstance().getHardware().getMotors().get("intakeLift").getCurrentPosition();
        double targetPosition = positions.get(state.value);
        double power = gamepadService.getAnalog(1, "left_trigger");
        if (power > 0.05) {
            power = Math.max(power, 0.5);
        }
        power = power * -1;
        Teleop.getInstance().getHardware().getMotors().get("intake").setPower(power);


        Teleop.getInstance().telemetry.addData("Is Enabled ", safety.getState());
        Teleop.getInstance().telemetry.addData("Position ", currentPos);
        Teleop.getInstance().telemetry.addData("Target ", targetPosition);
        Teleop.getInstance().telemetry.addData("Current State ", state);
        switch (state) {
            case UP:
            case DOWN:
                updatePID(targetPosition, currentPos);
                if (Math.abs(currentPos - targetPosition) <= 15) {
                    state = IntakeState.OFF;
                }
                break;
            case OFF:
                Teleop.getInstance().getHardware().getMotors().get("intakeLift").setPower(0);
                break;

        }
        buttonToggle.loop();
        safety.loop();
    }

    private void updatePID(double targetPosition, double currentPosition) {
        controller.updateSetpoint(targetPosition);
        double pow = controller.update(currentPosition);
        pow = Math.min(pow, maxPower);
        pow = Math.max(pow, -1 * maxPower);

        if (safety.getState()) {
            Teleop.getInstance().getHardware().getMotors().get("intakeLift").setPower(pow);
        } else {
            Teleop.getInstance().getHardware().getMotors().get("intakeLift").setPower(0.0);
        }
        Teleop.getInstance().telemetry.addData("Power ", pow);
        FtcDashboard.getInstance().getTelemetry().addData("Power ", pow);

    }

    private enum IntakeState {
        UP(2),
        DOWN(1),
        OFF(0);
        public int value;

        IntakeState(int value) {
            this.value = value;
        }
    }
}

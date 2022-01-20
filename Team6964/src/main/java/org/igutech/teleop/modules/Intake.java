package org.igutech.teleop.modules;

import com.acmerobotics.dashboard.FtcDashboard;

import org.igutech.teleop.Module;
import org.igutech.teleop.Teleop;
import org.igutech.utils.control.PIDFController;

import java.util.HashMap;

public class Intake extends Module {

    private GamepadService gamepadService;
    private IntakeState intakeState = IntakeState.MANUAL;
    public static double p = 0.008;
    public static double i = 0;
    public static double d = 0.00013;
    public static double f = 0.0007;
    private PIDFController controller;
    private HashMap<Integer, Integer> positions;
    private IntakeLiftState intakeLiftState = IntakeLiftState.OFF;

    public Intake() {
        super(700, "Intake");
    }

    @Override
    public void init() {
        gamepadService = (GamepadService) Teleop.getInstance().getService("GamepadService");
        controller = new PIDFController(p, i, d, f);
        controller.init();

        positions = new HashMap<>();
        positions.put(0, 0);
        positions.put(1, 0);
        positions.put(2, 160);
    }

    @Override
    public void loop() {

        switch (intakeState) {
            case MANUAL:
                double power = gamepadService.getAnalog(1, "left_trigger");
                if (power > 0.05) {
                    power = Math.max(power, 0.5);
                }
                power = power * -1;
                Teleop.getInstance().getHardware().getMotors().get("intake").setPower(power);
                break;
            case AUTO:
                Teleop.getInstance().getHardware().getMotors().get("intake").setPower(-1);
                break;
            case WAITING:
                Teleop.getInstance().getHardware().getMotors().get("intake").setPower(0);
                intakeState = IntakeState.OFF;
                break;
            case OFF:
                break;

        }

        double currentPos = Teleop.getInstance().getHardware().getMotors().get("intakeLift").getCurrentPosition();
        double targetPosition = positions.get(intakeLiftState.value);
        controller.setPIDFValues(p, i, d, f);
        controller.updateSetpoint(positions.get(intakeLiftState.value));

        switch (intakeLiftState) {
            case UP:
                updatePID(targetPosition, currentPos);
                break;
            case DOWN:
                Teleop.getInstance().getHardware().getMotors().get("intakeLift").setPower(0);
                intakeLiftState = IntakeLiftState.OFF;
                break;
            case OFF:
                break;

        }


        //Teleop.getInstance().telemetry.addData("Intake Position ", currentPos);
        //Teleop.getInstance().telemetry.addData("Intake Target ", targetPosition);
        Teleop.getInstance().telemetry.addData("Current Intake State ", intakeState);
        Teleop.getInstance().telemetry.addData("Current Intake Lift State ", intakeLiftState);

        FtcDashboard.getInstance().getTelemetry().addData("Intake Position", currentPos);
        FtcDashboard.getInstance().getTelemetry().addData("Intake Target  ", targetPosition);
        FtcDashboard.getInstance().getTelemetry().addData("Current Intake State ", intakeLiftState);
        FtcDashboard.getInstance().getTelemetry().addData("Current Intake Lift State ", intakeLiftState);
    }

    private void updatePID(double targetPosition, double currentPosition) {
        controller.updateSetpoint(targetPosition);
        double pow = controller.update(currentPosition);
        pow = Math.min(pow, 0.9);
        pow = Math.max(pow, -1 * 0.9);

        Teleop.getInstance().getHardware().getMotors().get("intakeLift").setPower(pow);

        //Teleop.getInstance().telemetry.addData("Intake Power ", pow);
        FtcDashboard.getInstance().getTelemetry().addData("Intake Power ", pow);

    }


    public enum IntakeState {
        AUTO,
        MANUAL,
        WAITING,
        OFF
    }

    private enum IntakeLiftState {
        UP(2),
        DOWN(1),
        OFF(0);
        public int value;

        IntakeLiftState(int value) {
            this.value = value;
        }
    }

    public void setIntakeState(IntakeState intakeState) {
        this.intakeState = intakeState;
    }

    public void setIntakeLiftState(boolean on) {
        if (on) {
            intakeLiftState = IntakeLiftState.UP;
        } else {
            intakeLiftState = IntakeLiftState.DOWN;
        }
    }
}

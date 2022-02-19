package org.igutech.teleop.modules;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;

import org.igutech.config.Hardware;
import org.igutech.teleop.Module;
import org.igutech.teleop.Teleop;
import org.igutech.utils.ButtonToggle;
import org.igutech.utils.MagicValues;
import org.igutech.utils.control.PIDFController;

import dev.scratch.simplestatemachine.State;
import dev.scratch.simplestatemachine.StateBuilder;
import dev.scratch.simplestatemachine.StateMachine;
import dev.scratch.simplestatemachine.StateMachineBuilder;
import dev.scratch.simplestatemachine.Transition;
import dev.scratch.simplestatemachine.TransitionBuilder;
@Config
public class Intake extends Module {

    private GamepadService gamepadService;
    private ButtonToggle intakeToggle;
    private IntakeState intakeState = IntakeState.MANUAL;
    public static double p = 0.009;
    public static double i = 0;
    public static double d = 0.00013;
    public static double f = 0.0009;
    //public static double targetPosition = 180;
    private PIDFController controller;
    private int[] positions;
    private IntakeLiftState intakeLiftState = IntakeLiftState.OFF;

    private boolean shareShippingHubActive = false;

    private Hardware hardware;
    private boolean isTeleop;

    public Intake(Hardware hardware, boolean isTeleop) {
        super(700, "Intake");
        this.hardware = hardware;
        this.isTeleop = isTeleop;
    }

    @Override
    public void init() {
        if (isTeleop) {
            gamepadService = (GamepadService) Teleop.getInstance().getService("GamepadService");
            intakeToggle = new ButtonToggle(1, "x", () -> {
            });
            intakeToggle.init();
        }
        controller = new PIDFController(p, i, d, f);
        controller.init();

        positions = new int[]{0, 0, 180};


    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        switch (intakeState) {
            case MANUAL:
                if(isTeleop){
                    double power = gamepadService.getAnalog(1, "left_trigger");
                    if (power > 0.1) {
                        if (!shareShippingHubActive) {
                            hardware.getServos().get("deliveryServo").setPosition(0.73);
                            hardware.getServos().get("holderServo").setPosition(MagicValues.holderServoUp);
                        }

                        power = Math.max(power, 0.5);
                    }
                    if (!intakeToggle.getState()) {
                        power = power * -1;
                    }
                    hardware.getMotors().get("intake").setPower(power);
                }

                break;
            case AUTO:
                hardware.getMotors().get("intake").setPower(-1);
                break;
            case MANUAL_REVERSE:
                hardware.getMotors().get("intake").setPower(0.5);
                break;
            case WAITING:
                hardware.getMotors().get("intake").setPower(0);
                intakeState = IntakeState.OFF;
                break;
            case OFF:
                break;

        }

        double currentPos = hardware.getMotors().get("intakeLift").getCurrentPosition();
        double targetPosition = positions[intakeLiftState.value];
        controller.setPIDFValues(p, i, d, f);
        controller.updateSetpoint(targetPosition);

        switch (intakeLiftState) {
            case UP:
                updatePID(targetPosition, currentPos);
                break;
            case DOWN:
                hardware.getMotors().get("intakeLift").setPower(0);
                intakeLiftState = IntakeLiftState.OFF;
                break;
            case OFF:
                break;

        }
        if (isTeleop) {
            intakeToggle.loop();

            //Teleop.getInstance().telemetry.addData("Intake Position ", currentPos);
            //Teleop.getInstance().telemetry.addData("Intake Target ", targetPosition);
            Teleop.getInstance().telemetry.addData("Current Intake State ", intakeState);
            Teleop.getInstance().telemetry.addData("Current Intake Lift State ", intakeLiftState);
        }


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

        hardware.getMotors().get("intakeLift").setPower(pow);

        //Teleop.getInstance().telemetry.addData("Intake Power ", pow);
        FtcDashboard.getInstance().getTelemetry().addData("Intake Power ", pow);

    }


    public enum IntakeState {
        MANUAL_REVERSE,
        AUTO,
        MANUAL,
        WAITING,
        OFF
    }

    public enum IntakeLiftState {
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

    public IntakeState getIntakeState() {
        return intakeState;
    }


    public void setIntakeLiftState(IntakeLiftState intakeLiftState) {
        this.intakeLiftState = intakeLiftState;
    }

    public boolean isShareShippingHubActive() {
        return shareShippingHubActive;
    }

    public void setShareShippingHubActive(boolean shareShippingHubActive) {
        this.shareShippingHubActive = shareShippingHubActive;
    }
}

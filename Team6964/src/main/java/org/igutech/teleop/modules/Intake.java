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
    public static double p = 0.008;
    public static double i = 0;
    public static double d = 0.00013;
    public static double f = 0.0007;
    public static double maxPower = 0.9;
    public static double highPosition = 160;
    private GamepadService gamepadService;
    private ButtonToggle buttonToggle;
    private IntakeState state = IntakeState.OFF;
    private HashMap<Integer, Integer> positions;
    private PIDFController controller;
    private TimerService timerService;

    public Intake() {
        super(700, "Intake");
    }

    @Override
    public void init() {
        gamepadService = (GamepadService) Teleop.getInstance().getService("GamepadService");
        timerService = (TimerService) Teleop.getInstance().getService("TimerService");

        buttonToggle = new ButtonToggle(1, "left_bumper", () -> {
            state = IntakeState.UP;
            timerService.registerUniqueTimerEvent(500, "activateIntake", () -> Teleop.getInstance().getHardware().getMotors().get("intake").setPower(-1));
        }, () -> {
            state = IntakeState.DOWN;
        });
        buttonToggle.init();
        positions = new HashMap<>();
        positions.put(0, 0);
        positions.put(1, 0);
        positions.put(2, 160);
        controller = new PIDFController(p, i, d, f);
        controller.init();


    }

    @Override
    public void start() {
        Teleop.getInstance().getHardware().getMotors().get("intakeLift").setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Teleop.getInstance().getHardware().getMotors().get("intakeLift").setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void loop() {

        controller.setPIDFValues(p, i, d, f);
        controller.updateSetpoint(positions.get(state.value));
        double currentPos = Teleop.getInstance().getHardware().getMotors().get("intakeLift").getCurrentPosition();
        double targetPosition = positions.get(state.value);
        double power = gamepadService.getAnalog(1, "left_trigger");
        if (power > 0.05) {
            power = Math.max(power, 0.5);
        }
        power = power * -1;
        if(state==IntakeState.OFF){
            //Teleop.getInstance().getHardware().getServos().get("holderServo").setPosition(0.36);
            //Teleop.getInstance().getHardware().getServos().get("deliveryServo").setPosition(0.73);
            Teleop.getInstance().getHardware().getMotors().get("intake").setPower(power);
        }



        switch (state) {
            case UP:
                targetPosition = highPosition;
                updatePID(targetPosition, currentPos);
                break;
            case DOWN:
                Teleop.getInstance().getHardware().getMotors().get("intakeLift").setPower(0);
                Teleop.getInstance().getHardware().getMotors().get("intake").setPower(0);
                timerService.registerUniqueTimerEvent(500,"activateHolderServo",()->Teleop.getInstance().getHardware().getServos().get("holderServo").setPosition(0.65));
                state = IntakeState.OFF;
                break;
            case OFF:
                break;

        }
        buttonToggle.loop();

        Teleop.getInstance().telemetry.addData("Intake Position ", currentPos);
        Teleop.getInstance().telemetry.addData("Intake Target ", targetPosition);
        Teleop.getInstance().telemetry.addData("Current State ", state);

        FtcDashboard.getInstance().getTelemetry().addData("Intake Position", currentPos);
        FtcDashboard.getInstance().getTelemetry().addData("Intake Target  ", targetPosition);
        FtcDashboard.getInstance().getTelemetry().addData("Current Intake State ", state);

    }

    private void updatePID(double targetPosition, double currentPosition) {
        controller.updateSetpoint(targetPosition);
        double pow = controller.update(currentPosition);
        pow = Math.min(pow, maxPower);
        pow = Math.max(pow, -1 * maxPower);

        Teleop.getInstance().getHardware().getMotors().get("intakeLift").setPower(pow);

        Teleop.getInstance().telemetry.addData("Intake Power ", pow);
        FtcDashboard.getInstance().getTelemetry().addData("Intake Power ", pow);

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

package org.igutech.auto.bluestates;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.igutech.auto.BlueAutoPath;
import org.igutech.auto.redstates.ExitWareHouse;
import org.igutech.teleop.modules.Intake;
import org.igutech.utils.MagicValues;
import org.jetbrains.annotations.Nullable;

import dev.raneri.statelib.State;

public class BlueRelocalizePosition extends State {
    private BlueAutoPath blueAutoPath;
    private Pose2d startPose;
    private boolean done = false;

    public BlueRelocalizePosition(BlueAutoPath blueAutoPath, Pose2d startPose) {
        this.blueAutoPath = blueAutoPath;
        this.startPose = startPose;
    }


    @Override
    public void onEntry(@Nullable State previousState) {
        double pow = MagicValues.autoMotorPowerBackward;
        double strafePow = MagicValues.autoMotorPowerForward;
        blueAutoPath.getDrive().setMotorPowers(-strafePow, strafePow, -strafePow, strafePow);
        blueAutoPath.getTimerService().registerSingleTimerEvent(500,()->{
            blueAutoPath.getDrive().setMotorPowers(pow*2,pow,pow*2,pow);

            blueAutoPath.getIntake().setIntakeState(Intake.IntakeState.MANUAL_REVERSE);
            blueAutoPath.getIntake().setIntakeLiftState(Intake.IntakeLiftState.DOWN);
        });
        blueAutoPath.getTimerService().registerSingleTimerEvent(750, () -> blueAutoPath.getHardware().getServos().get("holderServo").setPosition(MagicValues.holderServoDown));
    }

    @Override
    public void loop() {
        if (blueAutoPath.getColorDetection().getHsvValues()[2] > 0.5) {
            blueAutoPath.getDrive().setMotorPowers(0, 0, 0, 0);
            blueAutoPath.getDrive().setPoseEstimate(new Pose2d(23, 63.5, 0));
            done = true;
        }
    }

    @Override
    public void onExit(@Nullable State nextState) {
        blueAutoPath.getIntake().setIntakeState(Intake.IntakeState.WAITING);

    }

    @Nullable
    @Override
    public State getNextState() {
        if (done) {
            blueAutoPath.getDrive().setPoseEstimate(new Pose2d(23, 63.5, 0));
            return new BlueExitWareHouse(blueAutoPath, new Pose2d(23, 63.5, 0));
        }
        return null;
    }
}

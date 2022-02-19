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
        blueAutoPath.getDrive().setMotorPowers(pow, pow * 2, pow, pow * 2);
    }

    @Override
    public void loop() {
        if (blueAutoPath.getColorDetection().getHsvValues()[2] > 0.5) {
            blueAutoPath.getDrive().setMotorPowers(0, 0, 0, 0);
            blueAutoPath.getDrive().setPoseEstimate(new Pose2d(24, 63.5, 0));
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
            return new BlueExitWareHouse(blueAutoPath, new Pose2d(24, 63.5, 0));
        }
        return null;
    }
}

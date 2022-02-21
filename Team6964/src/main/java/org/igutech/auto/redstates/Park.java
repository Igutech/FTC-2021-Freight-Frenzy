package org.igutech.auto.redstates;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.igutech.auto.RedDuckAuto;
import org.igutech.auto.roadrunner.trajectorysequence.TrajectorySequence;
import org.igutech.utils.MagicValues;
import org.jetbrains.annotations.Nullable;

import dev.raneri.statelib.State;

public class Park extends State {
    private RedDuckAuto redDuckAuto;
    private Pose2d startPose;
    private TrajectorySequence park;
    public Park(RedDuckAuto redDuckAuto, Pose2d startPose) {
        this.redDuckAuto = redDuckAuto;
        this.startPose = startPose;

    }

    @Override
    public void onEntry(@Nullable State previousState) {
        double pow = MagicValues.autoMotorPowerForward;
        redDuckAuto.getDrive().setMotorPowers(-pow,-pow,-pow,-pow);
        redDuckAuto.getTimerService().registerSingleTimerEvent(1500,()->redDuckAuto.getDrive().setMotorPowers(0,0,0,0));
    }

    @Nullable
    @Override
    public State getNextState() {
        return null;
    }
}

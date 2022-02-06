package org.igutech.auto.redstates;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.igutech.auto.RedAutoPath;
import org.jetbrains.annotations.Nullable;

import dev.raneri.statelib.State;

public class PutDownDelivery extends State {
    private RedAutoPath redAutoBase;
    private Pose2d startPose;

    public PutDownDelivery(RedAutoPath autoBase, Pose2d startPose) {
        this.redAutoBase = autoBase;
        this.startPose = startPose;
    }

    @Override
    public void onEntry(@Nullable State previousState) {
        redAutoBase.getDelivery().setDeliveryStateBaseOnPattern(redAutoBase.getPattern());
    }

    @Nullable
    @Override
    public State getNextState() {
        if (redAutoBase.getDelivery().getError() < 30) {
            return new GoToHub(redAutoBase, startPose);
        }
        return null;
    }
}

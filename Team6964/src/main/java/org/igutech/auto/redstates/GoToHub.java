package org.igutech.auto.redstates;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.igutech.auto.RedAutoPath;
import org.igutech.auto.roadrunner.trajectorysequence.TrajectorySequence;
import org.jetbrains.annotations.Nullable;

import dev.raneri.statelib.State;

public class GoToHub extends State {
    private RedAutoPath redAutoBase;
    private Pose2d startPose;
    private TrajectorySequence goToHub;

    public GoToHub(RedAutoPath redAutoBase, Pose2d startPose) {
        this.redAutoBase = redAutoBase;
        this.startPose = startPose;
        goToHub = redAutoBase.getDrive().trajectorySequenceBuilder(startPose)
                .lineToConstantHeading(new Vector2d(-12, -40))
                .build();
    }

    @Override
    public void onEntry(@Nullable State previousState) {
        redAutoBase.getDrive().followTrajectorySequenceAsync(goToHub);

    }

    @Nullable
    @Override
    public State getNextState() {
        if (!redAutoBase.getDrive().isBusy()) {
            return new Deliver(redAutoBase, goToHub.end());
        }
        return null;
    }
}

package org.igutech.auto.redstates;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;

import org.igutech.auto.RedAutoPath;
import org.igutech.auto.roadrunner.trajectorysequence.TrajectorySequence;
import org.jetbrains.annotations.Nullable;

import dev.raneri.statelib.State;

public class PrepareToGoToHub extends State {
    private RedAutoPath autoPath;
    private Pose2d startPose;
    private TrajectorySequence prepareToGoToHub;

    public PrepareToGoToHub(RedAutoPath autoPath, Pose2d startPose) {
        this.autoPath = autoPath;
        this.startPose = startPose;
        prepareToGoToHub = autoPath.getDrive().trajectorySequenceBuilder(startPose)
                .setReversed(true)
                .splineTo(new Vector2d(-12, -45.0), Math.toRadians(90))
                .build();

    }

    @Override
    public void onEntry(@Nullable State previousState) {
        autoPath.getDrive().followTrajectorySequenceAsync(prepareToGoToHub);
    }

    @Nullable
    @Override
    public State getNextState() {
        if (!autoPath.getDrive().isBusy()) {
            return new PutDownDelivery(autoPath, prepareToGoToHub.end());
        }
        return null;
    }
}

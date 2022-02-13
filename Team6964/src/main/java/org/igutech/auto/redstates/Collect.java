package org.igutech.auto.redstates;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.igutech.auto.RedAutoPath;
import org.igutech.auto.roadrunner.trajectorysequence.TrajectorySequence;
import org.jetbrains.annotations.Nullable;

import dev.raneri.statelib.State;

public class Collect extends State {
    private RedAutoPath redAutoBase;
    private Pose2d startPose;
    private boolean done = false;
    private TrajectorySequence collectTrajectory;

    public Collect(RedAutoPath redAutoBase, Pose2d startPose) {
        this.redAutoBase = redAutoBase;
        this.startPose = startPose;
        collectTrajectory = redAutoBase.getDrive().trajectorySequenceBuilder(startPose)
                .splineTo(new Vector2d(35, -63.5), Math.toRadians(0.0))
                .build();
    }

    @Override
    public void onEntry(@Nullable State previousState) {
        redAutoBase.getHardware().getMotors().get("intake").setPower(-1);
        redAutoBase.getDrive().followTrajectorySequenceAsync(collectTrajectory);
    }

    @Nullable
    @Override
    public State getNextState() {
        return null;
    }
}

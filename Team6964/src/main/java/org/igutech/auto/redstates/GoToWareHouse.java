package org.igutech.auto.redstates;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.igutech.auto.RedAutoPath;
import org.igutech.auto.roadrunner.trajectorysequence.TrajectorySequence;
import org.igutech.utils.MagicValues;
import org.jetbrains.annotations.Nullable;

import dev.raneri.statelib.State;
@Config
public class GoToWareHouse extends State {
    private RedAutoPath redAutoBase;
    private Pose2d startPose;
    private TrajectorySequence goToWareHouse;
    public static double x=12;
    public GoToWareHouse(RedAutoPath redAutoBase, Pose2d startPose) {
        this.redAutoBase = redAutoBase;
        this.startPose = startPose;
        goToWareHouse =   redAutoBase.getDrive().trajectorySequenceBuilder(startPose)
                .setReversed(false)
                .splineToSplineHeading(new Pose2d(5, -62.0, Math.toRadians(-10.0)), Math.toRadians(-21.0))
                .splineToSplineHeading(new Pose2d(x, -63.5, Math.toRadians(0.0)), Math.toRadians(0.0))
                .build();

    }

    @Override
    public void onEntry(@Nullable State previousState) {
        redAutoBase.getDrive().followTrajectorySequenceAsync(goToWareHouse);
    }

    @Nullable
    @Override
    public State getNextState() {
        if (!redAutoBase.getDrive().isBusy()) {
            return new Collect(redAutoBase, goToWareHouse.end());
        }
        return null;
    }
}

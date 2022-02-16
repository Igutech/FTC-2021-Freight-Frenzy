package org.igutech.auto.redstates;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.igutech.auto.RedAutoPath;
import org.igutech.auto.roadrunner.trajectorysequence.TrajectorySequence;
import org.jetbrains.annotations.Nullable;

import dev.raneri.statelib.State;

public class ExitWareHouse extends State {
    private RedAutoPath redAutoPath;
    private Pose2d startPose;
    private TrajectorySequence exitWareHouse;
    public ExitWareHouse(RedAutoPath redAutoPath, Pose2d startPose) {
        this.redAutoPath = redAutoPath;
        this.startPose = startPose;
        exitWareHouse = redAutoPath.getDrive().trajectorySequenceBuilder(startPose)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(19.0, -63.5, Math.toRadians(0.0)), Math.toRadians(180.0))
                .splineToSplineHeading(new Pose2d(9.0, -62.0, Math.toRadians(-10.0)), Math.toRadians(170.0))
                .build();
    }
    @Override
    public void onEntry(@Nullable State previousState) {
        redAutoPath.getDrive().followTrajectorySequenceAsync(exitWareHouse);
    }

    @Nullable
    @Override
    public State getNextState() {
        if(!redAutoPath.getDrive().isBusy()){
            return new PrepareToGoToHub(redAutoPath,exitWareHouse.end());
        }
        return null;
    }
}

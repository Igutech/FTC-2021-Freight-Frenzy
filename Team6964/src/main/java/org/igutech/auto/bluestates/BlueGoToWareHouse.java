package org.igutech.auto.bluestates;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.igutech.auto.BlueAutoPath;
import org.igutech.auto.roadrunner.trajectorysequence.TrajectorySequence;
import org.jetbrains.annotations.Nullable;

import dev.raneri.statelib.State;

public class BlueGoToWareHouse extends State {
    private BlueAutoPath blueAutoPath;
    private Pose2d startPose;
    private TrajectorySequence goToWareHouse;
    public static double x = 10;
    public static double y = 60;

    public BlueGoToWareHouse(BlueAutoPath blueAutoPath, Pose2d startPose) {
        this.blueAutoPath = blueAutoPath;
        this.startPose = startPose;
        goToWareHouse = blueAutoPath.getDrive().trajectorySequenceBuilder(startPose)
                .lineToLinearHeading(new Pose2d(x, y, Math.toRadians(0.0)))
                .build();

    }

    @Override
    public void onEntry(@Nullable State previousState) {
        blueAutoPath.getDrive().followTrajectorySequenceAsync(goToWareHouse);
    }

    @Nullable
    @Override
    public State getNextState() {
        if (!blueAutoPath.getDrive().isBusy()) {
            return new BlueCollect(blueAutoPath, goToWareHouse.end());
        }
        return null;
    }
}

package org.igutech.auto.bluestates;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.igutech.auto.BlueAutoPath;
import org.igutech.auto.roadrunner.trajectorysequence.TrajectorySequence;
import org.jetbrains.annotations.Nullable;

import dev.raneri.statelib.State;

public class BlueExitWareHouse extends State {
    private BlueAutoPath blueAutoPath;
    private Pose2d startPose;
    private TrajectorySequence exitWareHouse;

    public BlueExitWareHouse(BlueAutoPath blueAutoPath, Pose2d startPose) {
        this.blueAutoPath = blueAutoPath;
        this.startPose = startPose;
        exitWareHouse = blueAutoPath.getDrive().trajectorySequenceBuilder(startPose)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(3.0, 62.5, Math.toRadians(0.0)), Math.toRadians(170.0))
                .build();
    }

    @Override
    public void onEntry(@Nullable State previousState) {
        blueAutoPath.getHardware().getMotors().get("delivery").setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        blueAutoPath.getHardware().getMotors().get("delivery").setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        blueAutoPath.getDrive().followTrajectorySequenceAsync(exitWareHouse);
    }

    @Nullable
    @Override
    public State getNextState() {
        if (!blueAutoPath.getDrive().isBusy()) {
            return new BlueGoToHub(blueAutoPath, exitWareHouse.end());
        }
        return null;
    }
}

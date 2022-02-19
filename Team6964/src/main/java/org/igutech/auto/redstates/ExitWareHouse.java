package org.igutech.auto.redstates;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.DcMotor;

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
        exitWareHouse = redAutoPath.getDrive().trajectorySequenceBuilder( new Pose2d(23, -63.5, 0))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(3.0, -62.5, Math.toRadians(-10.0)), Math.toRadians(170.0))
                .build();
    }

    @Override
    public void onEntry(@Nullable State previousState) {
        redAutoPath.getHardware().getMotors().get("delivery").setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        redAutoPath.getHardware().getMotors().get("delivery").setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        redAutoPath.getDrive().followTrajectorySequenceAsync(exitWareHouse);
    }

    @Nullable
    @Override
    public State getNextState() {
        if (!redAutoPath.getDrive().isBusy()) {
            return new GoToHubHigh(redAutoPath, exitWareHouse.end());
        }
        return null;
    }
}

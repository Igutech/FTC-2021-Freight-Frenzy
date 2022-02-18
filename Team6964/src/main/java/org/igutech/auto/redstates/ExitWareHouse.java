package org.igutech.auto.redstates;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.igutech.auto.RedAutoPath;
import org.igutech.auto.roadrunner.trajectorysequence.TrajectorySequence;
import org.igutech.teleop.modules.Intake;
import org.jetbrains.annotations.Nullable;

import dev.raneri.statelib.State;

public class ExitWareHouse extends State {
    private RedAutoPath redAutoPath;
    private Pose2d startPose;
    private TrajectorySequence exitWareHouse;

    public ExitWareHouse(RedAutoPath redAutoPath, Pose2d startPose) {
        this.redAutoPath = redAutoPath;
        this.startPose = startPose;
        exitWareHouse = redAutoPath.getRedTrajectories().exitWareHouse;
    }

    @Override
    public void onEntry(@Nullable State previousState) {
        redAutoPath.getIntake().setIntakeLiftState(Intake.IntakeLiftState.UP);
        redAutoPath.getIntake().setIntakeState(Intake.IntakeState.AUTO);

        redAutoPath.getDrive().followTrajectorySequenceAsync(exitWareHouse);
    }

    @Nullable
    @Override
    public State getNextState() {
        if (!redAutoPath.getDrive().isBusy()) {
            return new GoToHub(redAutoPath, exitWareHouse.end());
        }
        return null;
    }
}

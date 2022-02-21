package org.igutech.auto.redstates;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.igutech.auto.RedDuckAuto;
import org.igutech.auto.roadrunner.trajectorysequence.TrajectorySequence;
import org.jetbrains.annotations.Nullable;

import dev.raneri.statelib.State;

public class DuckGoToSpinner extends State {
    private RedDuckAuto redAutoPath;
    private Pose2d pose2d;
    private TrajectorySequence goToSpinner;
    private boolean done = false;
    private boolean finishedDriving = false;
    public DuckGoToSpinner(RedDuckAuto redAutoPath, Pose2d pose2d) {
        this.redAutoPath = redAutoPath;
        this.pose2d = pose2d;
        goToSpinner = redAutoPath.getDrive().trajectorySequenceBuilder(pose2d)
                .lineToLinearHeading(new Pose2d(-65, -30, Math.toRadians(-90)))
                .build();
    }

    @Override
    public void onEntry(@Nullable State previousState) {
        redAutoPath.getDrive().followTrajectorySequenceAsync(goToSpinner);
    }

    @Nullable
    @Override
    public State getNextState() {
        if(!redAutoPath.getDrive().isBusy()){
            return new DuckGoToSpinnerCont(redAutoPath,goToSpinner.end());
        }

        return null;
    }
}

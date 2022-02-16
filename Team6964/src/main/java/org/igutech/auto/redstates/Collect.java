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
                .lineToLinearHeading(new Pose2d(45, -63.5,0))
                .build();
    }

    @Override
    public void onEntry(@Nullable State previousState) {
        redAutoBase.getDrive().followTrajectorySequenceAsync(collectTrajectory);
    }

    @Override
    public void onExit(@Nullable State nextState) {
        redAutoBase.getHardware().getMotors().get("intake").setPower(0);

    }

    @Nullable
    @Override
    public State getNextState()
    {
        if(!redAutoBase.getDrive().isBusy()){
            redAutoBase.getTimerService().registerUniqueTimerEvent(750,"finishCollection",()->done=true );
        }
        if(done){
            return new ExitWareHouse(redAutoBase,startPose);
        }
        return null;
    }
}

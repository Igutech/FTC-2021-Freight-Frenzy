package org.igutech.auto.redstates;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.igutech.auto.RedAutoPath;
import org.igutech.auto.roadrunner.trajectorysequence.TrajectorySequence;
import org.igutech.utils.MagicValues;
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
//        collectTrajectory = redAutoBase.getDrive().trajectorySequenceBuilder(startPose)
//                .lineToLinearHeading(new Pose2d(45, -63.5,0))
//                .build();
    }

    @Override
    public void onEntry(@Nullable State previousState) {
        redAutoBase.getHardware().getMotors().get("intake").setPower(-1);
        double pow = MagicValues.autoMotorPowerForward;
        redAutoBase.getDrive().setMotorPowers(pow,pow*2,pow,pow*2);
        redAutoBase.getTimerService().registerSingleTimerEvent((int) MagicValues.collectTime,()->done=true);
    }

    @Override
    public void onExit(@Nullable State nextState) {
        redAutoBase.getHardware().getMotors().get("intake").setPower(0);
    }

    @Nullable
    @Override
    public State getNextState()
    {
        if(done){
            return new RelocalizePosition(redAutoBase,startPose);
        }
        return null;
    }
}

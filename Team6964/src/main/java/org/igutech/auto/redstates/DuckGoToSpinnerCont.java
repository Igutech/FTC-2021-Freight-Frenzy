package org.igutech.auto.redstates;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.igutech.auto.RedDuckAuto;
import org.igutech.auto.roadrunner.trajectorysequence.TrajectorySequence;
import org.igutech.utils.MagicValues;
import org.jetbrains.annotations.Nullable;

import dev.raneri.statelib.State;

public class DuckGoToSpinnerCont extends State {
    private RedDuckAuto redAutoPath;
    private Pose2d pose2d;
    private TrajectorySequence goToSpinner;
    private boolean done = false;
    private boolean spinDuck = false;
    public DuckGoToSpinnerCont(RedDuckAuto redAutoPath, Pose2d pose2d) {
        this.redAutoPath = redAutoPath;
        this.pose2d = pose2d;

    }

    @Override
    public void onEntry(@Nullable State previousState) {
        double pow = MagicValues.autoMotorPowerForward;
        redAutoPath.getDrive().setMotorPowers(pow, -pow, pow, -pow);
        redAutoPath.getTimerService().registerSingleTimerEvent(4000, () -> redAutoPath.getDrive().setMotorPowers(pow, pow, pow , pow));
        redAutoPath.getTimerService().registerSingleTimerEvent(5000, () -> redAutoPath.getDrive().setMotorPowers(pow*2, pow, pow*2 , pow));
        redAutoPath.getTimerService().registerSingleTimerEvent(6000,()->{
            spinDuck=true;
            redAutoPath.getDrive().setMotorPowers(0,0,0,0);
        });
    }

    @Nullable
    @Override
    public State getNextState() {
        if(spinDuck){
            redAutoPath.getHardware().getMotors().get("spinner").setPower(0.5);
            redAutoPath.getTimerService().registerSingleTimerEvent(7000,()->done=true);
            spinDuck=false;
        }
        if(done){
            redAutoPath.getHardware().getMotors().get("spinner").setPower(0);
            return new Park(redAutoPath,goToSpinner.end());
        }


        return null;
    }
}

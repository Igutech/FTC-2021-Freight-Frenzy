package org.igutech.auto.bluestates;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.igutech.auto.BlueAutoPath;
import org.igutech.auto.RedAutoPath;
import org.igutech.auto.redstates.RelocalizePosition;
import org.igutech.auto.roadrunner.trajectorysequence.TrajectorySequence;
import org.igutech.teleop.modules.Intake;
import org.igutech.utils.MagicValues;
import org.jetbrains.annotations.Nullable;

import dev.raneri.statelib.State;

public class BlueCollect extends State {
    private BlueAutoPath blueAutoPath;
    private Pose2d startPose;
    private boolean done = false;
    private TrajectorySequence collectTrajectory;

    public BlueCollect(BlueAutoPath blueAutoPath, Pose2d startPose) {
        this.blueAutoPath = blueAutoPath;
        this.startPose = startPose;
    }


    @Override
    public void onEntry(@Nullable State previousState) {
        blueAutoPath.getHardware().getMotors().get("intake").setPower(-1);

        double pow = MagicValues.autoMotorPowerForwardBlue;
        blueAutoPath.getDrive().setMotorPowers(-pow, pow, -pow, pow);
        blueAutoPath.getTimerService().registerSingleTimerEvent(500, () -> blueAutoPath.getDrive().setMotorPowers(pow , pow*2, pow, pow*2));

        if(blueAutoPath.getCycle()==1){
            blueAutoPath.getTimerService().registerSingleTimerEvent(500 + (int) MagicValues.collectDriveTimeFirst, () -> {
                blueAutoPath.getDrive().setMotorPowers(0, 0, 0, 0);
            });
        }else{
            blueAutoPath.getTimerService().registerSingleTimerEvent(500 + (int) MagicValues.collectDriveTimeSecond, () -> {
                blueAutoPath.getDrive().setMotorPowers(0, 0, 0, 0);
            });
        }

        blueAutoPath.getTimerService().registerSingleTimerEvent(500 + (int) MagicValues.collectTime, () -> {
            blueAutoPath.getHardware().getMotors().get("intake").setPower(0);
            blueAutoPath.getIntake().setIntakeState(Intake.IntakeState.AUTO);
            blueAutoPath.getIntake().setIntakeLiftState(Intake.IntakeLiftState.UP);
        });
        blueAutoPath.getTimerService().registerSingleTimerEvent(500 + (int) MagicValues.collectStopTime, () -> {
            done = true;
        });
    }

    @Override
    public void onExit(@Nullable State nextState) {
    }

    @Nullable
    @Override
    public State getNextState() {
        if (done) {
            return new BlueRelocalizePosition(blueAutoPath, startPose);
        }
        return null;
    }
}

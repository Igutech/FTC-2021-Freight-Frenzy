package org.igutech.auto.redstates;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.igutech.auto.RedAutoPath;
import org.igutech.auto.roadrunner.trajectorysequence.TrajectorySequence;
import org.igutech.teleop.modules.Intake;
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
    }


    @Override
    public void onEntry(@Nullable State previousState) {
        redAutoBase.getHardware().getMotors().get("intake").setPower(-1);

        double pow = MagicValues.autoMotorPowerForward;
        redAutoBase.getDrive().setMotorPowers(pow*2, -pow*2, pow*2, -pow*2);
        redAutoBase.getTimerService().registerSingleTimerEvent(500, () -> redAutoBase.getDrive().setMotorPowers(pow * 2, pow*2, pow * 2, pow*2));

        if(redAutoBase.getCycle()==1){
            redAutoBase.getTimerService().registerSingleTimerEvent(500 + (int) MagicValues.collectDriveTimeFirst, () -> {
                redAutoBase.getDrive().setMotorPowers(0, 0, 0, 0);
            });
        }else{
            redAutoBase.getTimerService().registerSingleTimerEvent(500 + (int) MagicValues.collectDriveTimeSecond, () -> {
                redAutoBase.getDrive().setMotorPowers(0, 0, 0, 0);
            });
        }

        redAutoBase.getTimerService().registerSingleTimerEvent(500 + (int) MagicValues.collectTime, () -> {
            redAutoBase.getHardware().getMotors().get("intake").setPower(0);
            redAutoBase.getIntake().setIntakeState(Intake.IntakeState.AUTO);
            redAutoBase.getIntake().setIntakeLiftState(Intake.IntakeLiftState.UP);
        });
        redAutoBase.getTimerService().registerSingleTimerEvent(500 + (int) MagicValues.collectStopTime, () -> {
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
            return new RelocalizePosition(redAutoBase, startPose);
        }
        return null;
    }
}

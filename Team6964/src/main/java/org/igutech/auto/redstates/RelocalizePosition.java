package org.igutech.auto.redstates;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.igutech.auto.RedAutoPath;
import org.igutech.teleop.Teleop;
import org.igutech.teleop.modules.Intake;
import org.igutech.utils.MagicValues;
import org.jetbrains.annotations.Nullable;

import dev.raneri.statelib.State;

public class RelocalizePosition extends State {
    private RedAutoPath redAutoBase;
    private Pose2d startPose;
    private boolean done = false;

    public RelocalizePosition(RedAutoPath redAutoBase, Pose2d startPose) {
        this.redAutoBase = redAutoBase;
        this.startPose = startPose;
    }


    @Override
    public void onEntry(@Nullable State previousState) {
        double pow = MagicValues.autoMotorPowerBackward;
        double strafePow = MagicValues.autoMotorPowerForward;
        redAutoBase.getDrive().setMotorPowers(strafePow*2, -strafePow*2, strafePow*2, -strafePow*2);
        redAutoBase.getTimerService().registerSingleTimerEvent(500,()->{
            redAutoBase.getDrive().setMotorPowers(pow,pow*2,pow,pow*2);

            redAutoBase.getIntake().setIntakeState(Intake.IntakeState.MANUAL_REVERSE);
            redAutoBase.getIntake().setIntakeLiftState(Intake.IntakeLiftState.DOWN);
        });
        redAutoBase.getTimerService().registerSingleTimerEvent(1000, () -> redAutoBase.getHardware().getServos().get("holderServo").setPosition(MagicValues.holderServoDown));

    }

    @Override
    public void loop() {
        if (redAutoBase.getColorDetection().getHsvValues()[2] > 0.5) {
            redAutoBase.getDrive().setMotorPowers(0,0,0,0);
            redAutoBase.getDrive().setPoseEstimate(new Pose2d(23, -63.5, 0));
            done=true;
        }
    }

    @Override
    public void onExit(@Nullable State nextState) {
        redAutoBase.getIntake().setIntakeState(Intake.IntakeState.WAITING);

    }

    @Nullable
    @Override
    public State getNextState() {
        if(redAutoBase.getCycle()==2){
            return null;
        }
        if (done) {
            return new ExitWareHouse(redAutoBase, new Pose2d(23, -63.5, 0));
        }
        return null;
    }
}

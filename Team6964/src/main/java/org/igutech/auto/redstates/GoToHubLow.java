package org.igutech.auto.redstates;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.igutech.auto.RedAutoPath;
import org.igutech.auto.roadrunner.trajectorysequence.TrajectorySequence;
import org.igutech.teleop.modules.Intake;
import org.igutech.utils.MagicValues;
import org.jetbrains.annotations.Nullable;

import dev.raneri.statelib.State;

@Config
public class GoToHubLow extends State {
    private RedAutoPath redAutoBase;
    private Pose2d startPose;
    private TrajectorySequence goToHub;
    public static double x = -4;
    public static double y = -44;
    public static double theta = -75;

    public GoToHubLow(RedAutoPath redAutoBase, Pose2d startPose) {
        this.redAutoBase = redAutoBase;
        this.startPose = startPose;
        goToHub = redAutoBase.getDrive().trajectorySequenceBuilder(startPose)
                .lineToConstantHeading(new Vector2d(10, -53))
                .setReversed(true)
                .lineToLinearHeading(new Pose2d(x, y, Math.toRadians(theta)))
                .build();
    }

    @Override
    public void onEntry(@Nullable State previousState) {
        redAutoBase.getIntake().setIntakeLiftState(Intake.IntakeLiftState.DOWN);
        redAutoBase.getIntake().setIntakeState(Intake.IntakeState.MANUAL);
        if(redAutoBase.getCycle()==0){
            redAutoBase.getTimerService().registerSingleTimerEvent(1250,()->{
                redAutoBase.getDelivery().setDeliveryStateBaseOnPattern(redAutoBase.getPattern());
            });
        }else{
            redAutoBase.getHardware().getServos().get("holderServo").setPosition(MagicValues.holderServoDown);
            redAutoBase.getHardware().getServos().get("deliveryServo").setPosition(MagicValues.deliverServoDown);
            redAutoBase.getDelivery().setDeliveryStateBaseOnPattern(redAutoBase.getPattern());
        }

        redAutoBase.getDrive().followTrajectorySequenceAsync(goToHub);

    }

    @Nullable
    @Override
    public State getNextState() {
        if (!redAutoBase.getDrive().isBusy()) {
            return new Deliver(redAutoBase, goToHub.end());
        }
        return null;
    }
}

package org.igutech.auto.redstates;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.igutech.auto.RedAutoPath;
import org.igutech.auto.roadrunner.trajectorysequence.TrajectorySequence;
import org.igutech.teleop.modules.Intake;
import org.igutech.utils.MagicValues;
import org.jetbrains.annotations.Nullable;

import dev.raneri.statelib.State;

public class GoToHub extends State {
    private RedAutoPath redAutoBase;
    private Pose2d startPose;
    private TrajectorySequence goToHub;

    public GoToHub(RedAutoPath redAutoBase, Pose2d startPose) {
        this.redAutoBase = redAutoBase;
        this.startPose = startPose;
        goToHub = redAutoBase.getDrive().trajectorySequenceBuilder(startPose)
                .setReversed(true)
                .splineTo(new Vector2d(-4.5, -40.0), Math.toRadians(112.0))
                .build();
    }

    @Override
    public void onEntry(@Nullable State previousState) {
        redAutoBase.getIntake().setIntakeLiftState(Intake.IntakeLiftState.DOWN);
        redAutoBase.getIntake().setIntakeState(Intake.IntakeState.MANUAL);
        redAutoBase.getHardware().getServos().get("holderServo").setPosition(MagicValues.holderServoDown);
        redAutoBase.getHardware().getServos().get("deliveryServo").setPosition(MagicValues.deliverServoDown);
        redAutoBase.getDelivery().setDeliveryStateBaseOnPattern(redAutoBase.getPattern());
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

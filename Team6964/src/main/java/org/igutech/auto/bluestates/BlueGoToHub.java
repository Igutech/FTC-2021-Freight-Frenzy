package org.igutech.auto.bluestates;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.igutech.auto.BlueAutoPath;
import org.igutech.auto.roadrunner.trajectorysequence.TrajectorySequence;
import org.igutech.teleop.modules.Intake;
import org.igutech.utils.MagicValues;
import org.jetbrains.annotations.Nullable;

import dev.raneri.statelib.State;

@Config
public class BlueGoToHub extends State {
    private BlueAutoPath blueAutoPath;
    private Pose2d startPose;
    private TrajectorySequence goToHub;
    public static double x = -5;
    public static double y = 45;
    public static double theta = 225;

    public BlueGoToHub(BlueAutoPath blueAutoPath, Pose2d startPose) {
        this.blueAutoPath = blueAutoPath;
        this.startPose = startPose;
        goToHub = blueAutoPath.getDrive().trajectorySequenceBuilder(startPose)
                .splineTo(new Vector2d(-5, 45), Math.toRadians(theta))
                .build();
    }


    @Override
    public void onEntry(@Nullable State previousState) {
        blueAutoPath.getIntake().setIntakeLiftState(Intake.IntakeLiftState.DOWN);
        blueAutoPath.getIntake().setIntakeState(Intake.IntakeState.MANUAL);
        blueAutoPath.getHardware().getServos().get("holderServo").setPosition(MagicValues.holderServoDown);
        blueAutoPath.getHardware().getServos().get("deliveryServo").setPosition(MagicValues.deliverServoDown);
        blueAutoPath.getDelivery().setDeliveryStateBaseOnPattern(3);
        blueAutoPath.getDrive().followTrajectorySequenceAsync(goToHub);

    }

    @Nullable
    @Override
    public State getNextState() {
        if (!blueAutoPath.getDrive().isBusy()) {
            return new BlueDeliver(blueAutoPath, goToHub.end());
        }
        return null;
    }
}

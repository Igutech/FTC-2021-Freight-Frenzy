package org.igutech.auto.redstates;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.igutech.auto.RedDuckAuto;
import org.igutech.auto.roadrunner.trajectorysequence.TrajectorySequence;
import org.igutech.utils.MagicValues;
import org.jetbrains.annotations.Nullable;

import dev.raneri.statelib.State;
@Config
public class DuckHubMiddle extends State {
    private RedDuckAuto redAutoPath;
    private Pose2d pose2d;
    private TrajectorySequence hubHigh;
    private boolean done = false;
    public static double x = -25;
    public static double y = -28;
    public static double theta = -130;
    public DuckHubMiddle(RedDuckAuto redAutoPath, Pose2d pose2d) {
        this.redAutoPath = redAutoPath;
        this.pose2d = pose2d;
        hubHigh = redAutoPath.getDrive().trajectorySequenceBuilder(pose2d)
                .setReversed(true)
                .lineToLinearHeading(new Pose2d(x, y, Math.toRadians(theta)))
                .build();
    }

    @Override
    public void onEntry(@Nullable State previousState) {
        redAutoPath.getDrive().followTrajectorySequenceAsync(hubHigh);
    }

    @Nullable
    @Override
    public State getNextState() {
        if(!redAutoPath.getDrive().isBusy()){
            redAutoPath.getHardware().getServos().get("deliveryServo").setPosition(MagicValues.deliverServoUp);
            redAutoPath.getHardware().getServos().get("holderServo").setPosition(MagicValues.holderServoPush);
            redAutoPath.getTimerService().registerUniqueTimerEvent(500, "moveToWareHouse", () -> {
                redAutoPath.getHardware().getServos().get("holderServo").setPosition(MagicValues.holderServoUp);
                redAutoPath.getHardware().getServos().get("deliveryServo").setPosition(MagicValues.deliverServoDown);
                redAutoPath.getDelivery().setDeliveryStatus(false);
                done = true;

            });
        }
        if(done){
            return new DuckGoToSpinner(redAutoPath,hubHigh.end());
        }
        return null;
    }

    @Override
    public void onExit(@Nullable State nextState) {
        redAutoPath.getDelivery().setDeliveryStatus(false);
    }
}

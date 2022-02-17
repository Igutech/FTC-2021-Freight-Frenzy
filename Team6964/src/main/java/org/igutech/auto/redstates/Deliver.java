package org.igutech.auto.redstates;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.igutech.auto.RedAutoPath;
import org.igutech.teleop.modules.Delivery;
import org.igutech.utils.MagicValues;
import org.jetbrains.annotations.Nullable;

import dev.raneri.statelib.State;

public class Deliver extends State {
    private RedAutoPath redAutoBase;
    private Pose2d startPose;
    private boolean done = false;

    public Deliver(RedAutoPath redAutoBase, Pose2d startPose) {
        this.redAutoBase = redAutoBase;
        this.startPose = startPose;
    }

    @Override
    public void onEntry(@Nullable State previousState) {
        redAutoBase.getHardware().getServos().get("deliveryServo").setPosition(MagicValues.deliverServoUp);
        redAutoBase.getHardware().getServos().get("holderServo").setPosition(MagicValues.holderServoPush);
        done = true;
        redAutoBase.getTimerService().registerUniqueTimerEvent(500, "moveToWareHouse", () -> {
            redAutoBase.getHardware().getServos().get("holderServo").setPosition(MagicValues.holderServoUp);
            redAutoBase.getHardware().getServos().get("deliveryServo").setPosition(MagicValues.deliverServoDown);
            redAutoBase.getDelivery().setDeliveryStatus(false);
        });
    }

    @Nullable
    @Override
    public State getNextState() {
        if (done) {
            return new GoToWareHouse(redAutoBase, startPose);
        }
        return null;
    }
}

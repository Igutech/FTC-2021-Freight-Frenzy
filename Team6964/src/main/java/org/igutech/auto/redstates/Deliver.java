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
        redAutoBase.addCycle();
        redAutoBase.getTimerService().registerSingleTimerEvent(500,()->{
            redAutoBase.getHardware().getServos().get("deliveryServo").setPosition(MagicValues.deliverServoUp);
            redAutoBase.getHardware().getServos().get("holderServo").setPosition(MagicValues.holderServoPush);
        });

        redAutoBase.getTimerService().registerUniqueTimerEvent(1000, "moveToWareHouse", () -> {
            redAutoBase.getHardware().getServos().get("holderServo").setPosition(MagicValues.holderServoUp);
            redAutoBase.getHardware().getServos().get("deliveryServo").setPosition(MagicValues.deliverServoDown);
            redAutoBase.getDelivery().setDeliveryStatus(false);
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
            if (redAutoBase.getCycle() == 1 && (redAutoBase.getPattern() == 2 || redAutoBase.getPattern() == 1)) {
                return new GoToWareHouseMiddle(redAutoBase, startPose);
            }
            return new GoToWareHouse(redAutoBase, startPose);
        }
        return null;
    }
}

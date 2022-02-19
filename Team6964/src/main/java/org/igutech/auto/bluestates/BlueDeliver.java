package org.igutech.auto.bluestates;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.igutech.auto.BlueAutoPath;
import org.igutech.utils.MagicValues;
import org.jetbrains.annotations.Nullable;

import dev.raneri.statelib.State;

public class BlueDeliver extends State {
    private BlueAutoPath blueAutoPath;
    private Pose2d startPose;
    private boolean done = false;

    public BlueDeliver(BlueAutoPath blueAutoPath, Pose2d startPose) {
        this.blueAutoPath = blueAutoPath;
        this.startPose = startPose;
    }

    @Override
    public void onEntry(@Nullable State previousState) {
        blueAutoPath.addCycle();
        blueAutoPath.getHardware().getServos().get("deliveryServo").setPosition(MagicValues.deliverServoUp);
        blueAutoPath.getHardware().getServos().get("holderServo").setPosition(MagicValues.holderServoPush);
        blueAutoPath.getTimerService().registerUniqueTimerEvent(500, "moveToWareHouse", () -> {
            blueAutoPath.getHardware().getServos().get("holderServo").setPosition(MagicValues.holderServoUp);
            blueAutoPath.getHardware().getServos().get("deliveryServo").setPosition(MagicValues.deliverServoDown);
            blueAutoPath.getDelivery().setDeliveryStatus(false);
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
            return new BlueGoToWareHouse(blueAutoPath, startPose);
        }
        return null;
    }
}

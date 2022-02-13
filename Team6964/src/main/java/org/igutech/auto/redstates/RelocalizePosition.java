package org.igutech.auto.redstates;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.igutech.auto.RedAutoPath;
import org.igutech.teleop.Teleop;
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
        redAutoBase.getHardware().getMotors().get("frontleft").setPower(MagicValues.autoMotorPower);
        redAutoBase.getHardware().getMotors().get("frontright").setPower(MagicValues.autoMotorPower);
        redAutoBase.getHardware().getMotors().get("backleft").setPower(MagicValues.autoMotorPower);
        redAutoBase.getHardware().getMotors().get("backright").setPower(MagicValues.autoMotorPower);
    }

    @Override
    public void loop() {
        if (redAutoBase.getColorDetection().getRGBA().red > 0.15) {
            redAutoBase.getHardware().getMotors().get("frontleft").setPower(0);
            redAutoBase.getHardware().getMotors().get("frontright").setPower(0);
            redAutoBase.getHardware().getMotors().get("backleft").setPower(0);
            redAutoBase.getHardware().getMotors().get("backright").setPower(0);
            redAutoBase.getDrive().setPoseEstimate(new Pose2d(30, -63.5, 0));
        }
    }

    @Nullable
    @Override
    public State getNextState() {
        if (done) {
            return new Collect(redAutoBase, new Pose2d(30, -63.5, 0));
        }
        return null;
    }
}

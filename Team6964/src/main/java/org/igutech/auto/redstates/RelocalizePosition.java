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
        redAutoBase.getHardware().getMotors().get("intake").setPower(-1);
        double pow = MagicValues.autoMotorPower;
        redAutoBase.getDrive().setMotorPowers(pow,pow,pow,pow);
//        redAutoBase.getHardware().getMotors().get("frontleft").setPower(MagicValues.autoMotorPower);
//        redAutoBase.getHardware().getMotors().get("frontright").setPower(MagicValues.autoMotorPower);
//        redAutoBase.getHardware().getMotors().get("backleft").setPower(MagicValues.autoMotorPower);
//        redAutoBase.getHardware().getMotors().get("backright").setPower(MagicValues.autoMotorPower);
    }

    @Override
    public void loop() {
        if (redAutoBase.getColorDetection().getHsvValues()[2] > 0.5) {
            redAutoBase.getDrive().setMotorPowers(0,0,0,0);
            redAutoBase.getDrive().setPoseEstimate(new Pose2d(25, -63, 0));
            done=true;
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

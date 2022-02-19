package org.igutech.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.igutech.auto.roadrunner.SampleMecanumDrive;
import org.igutech.auto.roadrunner.trajectorysequence.TrajectorySequence;
@Config
public class RedTrajectory {
    private SampleMecanumDrive sampleMecanumDrive;

    public  Pose2d middleHubPose = new Pose2d(-6,-45,Math.toRadians(115));
    public Pose2d highHubPose = new Pose2d(-14.5,-41,Math.toRadians(90));
    public RedTrajectory(SampleMecanumDrive sampleMecanumDrive, Pose2d startPose) {
        this.sampleMecanumDrive = sampleMecanumDrive;
    }

    public void init() {


    }
}

package org.igutech.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.igutech.auto.roadrunner.SampleMecanumDrive;
import org.igutech.auto.roadrunner.trajectorysequence.TrajectorySequence;

public class RedTrajectory {
    private SampleMecanumDrive sampleMecanumDrive;
    public TrajectorySequence goToHub;
    public TrajectorySequence goToWareHouse;
    public TrajectorySequence exitWareHouse;
    private Pose2d startPose;

    public RedTrajectory(SampleMecanumDrive sampleMecanumDrive, Pose2d startPose) {
        this.sampleMecanumDrive = sampleMecanumDrive;
        this.startPose = startPose;
    }

    public void init() {
        goToHub = sampleMecanumDrive.trajectorySequenceBuilder(startPose)
                .setReversed(true)
                .splineTo(new Vector2d(-4.5, -40.0), Math.toRadians(112.0))
                .build();
        goToWareHouse =sampleMecanumDrive.trajectorySequenceBuilder(goToHub.end())
                .setReversed(false)
                .splineToSplineHeading(new Pose2d(5, -62.0, Math.toRadians(-10.0)), Math.toRadians(-21.0))
                .splineToSplineHeading(new Pose2d(15.0, -63.5, Math.toRadians(0.0)), Math.toRadians(0.0))
                .strafeRight(5)
                .build();
        exitWareHouse = sampleMecanumDrive.trajectorySequenceBuilder( new Pose2d(24, -63.5, 0))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(3.0, -62.5, Math.toRadians(-10.0)), Math.toRadians(170.0))
                .build();

    }
}

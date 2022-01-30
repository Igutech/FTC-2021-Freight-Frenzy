package org.igutech.config;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.HashMap;
import java.util.Map;

public class Hardware {

    private HardwareMap hardwareMap;
    private Map<String, DcMotor> motors;
    private Map<String, Servo> servos;
    private Map<String, CRServo> CRservos;
    private Map<String, DigitalChannel> touchSensors;

    /**
     * Initialize the hardware object
     *
     * @param hardwareMap Hardware map provided by FIRST
     */
    public Hardware(HardwareMap hardwareMap, boolean teleop) {
        this.hardwareMap = hardwareMap;
        this.motors = new HashMap<>();
        this.servos = new HashMap<>();
        this.CRservos = new HashMap<>();
        this.touchSensors = new HashMap<>();
        motors.put("frontleft", hardwareMap.dcMotor.get("frontleft"));
        motors.put("frontright", hardwareMap.dcMotor.get("frontright"));
        motors.put("backleft", hardwareMap.dcMotor.get("backleft"));
        motors.put("backright", hardwareMap.dcMotor.get("backright"));
        motors.put("delivery", hardwareMap.dcMotor.get("delivery"));
        motors.put("intake", hardwareMap.dcMotor.get("intake"));
        motors.put("intakeLift", hardwareMap.dcMotor.get("intakeLift"));
        motors.put("spinner", hardwareMap.dcMotor.get("spinner"));


        motors.get("frontright").setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motors.get("frontleft").setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motors.get("backright").setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motors.get("backleft").setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

//        motors.get("frontright").setDirection(DcMotorSimple.Direction.REVERSE);
//        motors.get("frontleft").setDirection(DcMotorSimple.Direction.REVERSE);

        if (teleop) {
            motors.get("backright").setDirection(DcMotorSimple.Direction.REVERSE);
            motors.get("backleft").setDirection(DcMotorSimple.Direction.REVERSE);
        }


        motors.get("frontright").setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motors.get("frontleft").setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motors.get("backright").setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motors.get("backleft").setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        servos.put("deliveryServo", hardwareMap.servo.get("deliveryServo"));
        servos.put("holderServo", hardwareMap.servo.get("holderServo"));

        touchSensors.put("intakeBeamBreaker", hardwareMap.get(DigitalChannel.class, "intakeBeamBreaker"));
    }

    /**
     * Get the raw hardware map object provided by FIRST
     *
     * @return
     */
    public HardwareMap getHardwareMap() {
        return hardwareMap;
    }

    /**
     * Get a map of motors
     *
     * @return Map of motor name/motor
     */
    public Map<String, DcMotor> getMotors() {
        return motors;
    }


    /**
     * Get a map of servos
     *
     * @return
     */
    public Map<String, Servo> getServos() {
        return servos;
    }

    public Map<String, CRServo> getCRServos() {
        return CRservos;
    }

    /**
     * Get a map of touch sensors
     *
     * @return Map of touch sensor name/Digital Channel
     */
    public Map<String, DigitalChannel> getTouchSensors() {
        return touchSensors;
    }
}

package org.firstinspires.ftc.teamcode;

//TODO can't we just use built-in ElapsedTime instead of nanoTime()?
//TODO cut this down to only necessary imports
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous; //TODO is this necessary?
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled; //TODO is this necessary?

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@Autonomous(name="Autonomous Controller")

public class AutonomousController extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor lfDrive;
    private DcMotor lbDrive;
    private DcMotor rfDrive;
    private DcMotor rbDrive;
    private Servo fixedServo;
    private CRServo contServo;
    
    public static final double FEET_PER_SECOND = 3;
    public static final double DEGREES_PER_SECOND = 50;

    @Override
    public void main() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();


        //HARDWARE SETUP

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        lfDrive = hardwareMap.get(DcMotor.class, "lf");
        lbDrive = hardwareMap.get(DcMotor.class, "lb");
        rfDrive = hardwareMap.get(DcMotor.class, "rf");
        rbDrive = hardwareMap.get(DcMotor.class, "rb");
        fixedServo1 = hardwareMap.get(Servo.class, "fixed");
        contServo1 = hardwareMap.get(CRServo.class, "cont");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        lfDrive.setDirection(DcMotor.Direction.FORWARD);
        lbDrive.setDirection(DcMotor.Direction.FORWARD);
        rfDrive.setDirection(DcMotor.Direction.REVERSE);
        rbDrive.setDirection(DcMotor.Direction.REVERSE);
    

        // HIGH LEVEL AUTONOMOUS INSTRUCTIONS

        //comment goes here
        move(20);
        turn(10, left=true);
        turn(3 left=false);
        depositFlag();
    }

    private void move(int feet) {
        long startTime = System.nanoTime() / 1000000;

        //make robot move forward
        lfDrive.setPower(1);
        lbDrive.setPower(1);
        rfDrive.setPower(1);
        rbDrive.setPower(1);

        //wait until robot has gone "feet" feet.
        while ((System.nanoTime() / 1000000) - startTime < feet / FEET_PER_SECOND) {
            long feetMoved = ((System.nanoTime() / 1000000) - startTime) * FEET_PER_SECOND;
            telemetry.addData("Status", "Moving " + Long.toString(feetMoved) + "/" + Integer.toString(feet) + " feet.");
            telemetry.update();
        }

        telemetry.addData("Status", "Move Complete!");
        telemetry.update();

        //stop robot
        lfDrive.setPower(0);
        lbDrive.setPower(0);
        rfDrive.setPower(0);
        rbDrive.setPower(0);
    }

    
    private void turn(int degrees, boolean left) {
        long startTime = System.nanoTime() / 1000000;

        //make robot start turning (left or right based on if left is true)
        int directionMultiplier = left ? 1 : -1;
        lfDrive.setPower(-1 * directionMultiplier);
        lbDrive.setPower(-1 * directionMultiplier);
        rfDrive.setPower(1 * directionMultiplier);
        rbDrive.setPower(1 * directionMultiplier);

        //wait until turn has gone "degrees" degrees
        while ((System.nanoTime() / 1000000) - startTime < degrees / DEGREES_PER_SECOND) {
            long degreesTurned = ((System.nanoTime() / 1000000) - startTime) * DEGREES_PER_SECOND;
            telemetry.addData("Status", "Turning " + Long.toString(degreesTurned) + "/" + Integer.toString(degrees) + " degrees.");
            telemetry.update();
        }

        telemetry.addData("Status", "Turn Complete!");
        telemetry.update();

        //stop robot
        lfDrive.setPower(0);
        lbDrive.setPower(0);
        rfDrive.setPower(0);
        rbDrive.setPower(0);
    }

    private void depositFlag() {
        
    }
}
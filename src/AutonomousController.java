package org.firstinspires.ftc.teamcode;

//TODO cut this down to only necessary imports
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import java.util.logging.Logger;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@Autonomous(name="Autonomous Controller", group="Linear Opmode")

public class AutonomousController extends LinearOpMode {

    public static final int SCRIPT_CODE = 4; // 1 through 6
    public static final double INCHES_PER_SECOND = 109.5/3.1887;
    public static final double DEGREES_PER_SECOND = 720/3.81;

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor lfDrive;
    private DcMotor lbDrive;
    private DcMotor rfDrive;
    private DcMotor rbDrive;
    private Servo frontPlate;
    private DcMotor vertLift;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // HARDWARE SETUP

        //Initialize the hardware variables. Note that the strings used here as parameters
        //to 'get' must correspond to the names assigned during the robot configuration
        //step (using the FTC Robot Controller app on the phone).
        lfDrive = hardwareMap.get(DcMotor.class, "lf");
        lbDrive = hardwareMap.get(DcMotor.class, "lb");
        rfDrive = hardwareMap.get(DcMotor.class, "rf");
        rbDrive = hardwareMap.get(DcMotor.class, "rb");
        frontPlate = hardwareMap.get(Servo.class, "front_plate");
        vertLift = hardwareMap.get(DcMotor.class, "vert_lift");

        //Most robots need the motor on one side to be reversed to drive forward
        //Reverse the motor that runs backwards when connected directly to the battery
        lfDrive.setDirection(DcMotor.Direction.FORWARD);
        lbDrive.setDirection(DcMotor.Direction.FORWARD);
        rfDrive.setDirection(DcMotor.Direction.REVERSE);
        rbDrive.setDirection(DcMotor.Direction.REVERSE);
        vertLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        vertLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        vertLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        waitForStart();
        runtime.reset();

        // AUTONOMOUS SCRIPTS

        if (SCRIPT_CODE == 1) {
            turn(20, true);
            move(48);
            turn(47, false);
            move(26);
            depositFlag();
            turn(109, false);
            move(105);
        } else if (SCRIPT_CODE == 2) {
            extendVertLift();
            moveRight();
            move(50);
            depositFlag();
            frontPlate.setPosition(0.5);
            turn(160, false);
            move(105);
        } else if (SCRIPT_CODE == 3) {
            turn(21, false);
            move(49);
            turn(64, true);
            move(30);
            depositFlag();
            turn(177, false);
            move(110);
        } else if (SCRIPT_CODE == 4) {
            extendVertLift();
            moveRight();
            turn(29, true);
            move(39);
            turn(123, true);
            move(74);
            depositFlag();
            frontPlate.setPosition(0.5);
            turn(200, false);
            move(90);
        } else if (SCRIPT_CODE == 5) {
            move(35);
            turn(152, true);
            move(39);
            turn(72, false);
            move(20);
            turn(78, true);
            move(48);
            depositFlag();
            moveBackwards(86);
        } else if (SCRIPT_CODE == 6) {
            turn(18, false);
            move(38);
            turn(161, true);
            move(55);
            turn(90, false);
            move(36);
            turn(90, false);
            move(43);
            depositFlag();
            moveBackwards(86);
        }
    }
    
    private void moveRight() {
        double startTime = runtime.time();
        
        //when no time lol
        //this is all modified from the borrowed code in "current.java"
        double r = Math.hypot(-1, 0);
        double robotAngle = Math.atan2(0, -1) - Math.PI / 4;
        final double v1 = r * Math.cos(robotAngle);
        final double v2 = r * Math.sin(robotAngle);
        final double v3 = r * Math.sin(robotAngle);
        final double v4 = r * Math.cos(robotAngle);
            
        lfDrive.setPower(v1);
        rfDrive.setPower(v2);
        lbDrive.setPower(v3);
        rbDrive.setPower(v4);
        
        while (runtime.time() - startTime < 0.3) {
            telemetry.addData("test", "test");
        }
        
        lfDrive.setPower(0);
        rfDrive.setPower(0);
        lbDrive.setPower(0);
        rbDrive.setPower(0);
    }
    
    private void extendVertLift() {
        double startTime = runtime.time();
        
        vertLift.setTargetPosition(vertLift.getCurrentPosition() + 25000);
        vertLift.setPower(1);
        //TODO add logging with get curr pos against target pos

        //don't remove this. It isn't just for logging
        //it keeps other functions from running asynchonously
        //(aka would drive while still being lowered down)
        while (vertLift.isBusy()) {
            telemetry.addData("OK", "I really hope this shows up.");
        }
        
    }

    private void depositFlag() {
        frontPlate.setPosition(0);
        double startTime = runtime.time();
        
        //prevent async execution of next function
        while (runtime.time() - startTime < 3) {
            telemetry.addData("Front Plate", "Dropping");
        }
        telemetry.addData("Front Plate", "Dropped!");
    }

    private void move(int inches) {
        inches -= 8;
        
        double startTime = runtime.time();

        //make robot move forward
        lfDrive.setPower(-1);
        lbDrive.setPower(-1);
        rfDrive.setPower(-1);
        rbDrive.setPower(-1);

        //wait until robot has gone "feet" feet.
        while (runtime.time() - startTime < inches / INCHES_PER_SECOND) {
            double inchesMoved = (runtime.time() - startTime) * INCHES_PER_SECOND;
            telemetry.addData("Moving", Integer.toString((int)inchesMoved) + "/" + Integer.toString(inches) + " feet.");
            telemetry.update();
        }

        telemetry.addData("Moving", "Complete!");
        telemetry.update();

        //stop robot
        lfDrive.setPower(0);
        lbDrive.setPower(0);
        rfDrive.setPower(0);
        rbDrive.setPower(0);
    }

    private void moveBackwards(int inches) {
        inches -= 8;
        
        double startTime = runtime.time();

        //make robot move forward
        lfDrive.setPower(1);
        lbDrive.setPower(1);
        rfDrive.setPower(1);
        rbDrive.setPower(1);

        //wait until robot has gone "feet" feet.
        telemetry.addData("Seconds", runtime.time() - startTime);
        telemetry.addData("Total Seconds", inches / INCHES_PER_SECOND);
        
        while (runtime.time() - startTime < inches / INCHES_PER_SECOND) {
            double inchesMoved = (runtime.time() - startTime) * INCHES_PER_SECOND;
            telemetry.addData("Status", "Moving " + Integer.toString((int)inchesMoved) + "/" + Integer.toString(inches) + " feet.");
            telemetry.addData("Time", runtime);
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
        
        double startTime = runtime.time();

        //make robot start turning (left or right based on if left is true)
        int directionMultiplier = left ? -1 : 1;
        lfDrive.setPower(-1 * directionMultiplier);
        lbDrive.setPower(-1 * directionMultiplier);
        rfDrive.setPower(1 * directionMultiplier);
        rbDrive.setPower(1 * directionMultiplier);

        //wait until turn has gone "degrees" degrees
        while (runtime.time() - startTime < degrees / DEGREES_PER_SECOND) {
            double degreesTurned = (runtime.time() - startTime) * DEGREES_PER_SECOND;
            telemetry.addData("Turning", Integer.toString((int)degreesTurned) + "/" + Integer.toString(degrees) + " degrees.");
            telemetry.addData("Runtime", runtime.time());
            telemetry.update();
        }

        telemetry.addData("Turning", "Complete!");
        telemetry.update();

        //stop robot
        lfDrive.setPower(0);
        lbDrive.setPower(0);
        rfDrive.setPower(0);
        rbDrive.setPower(0);
    }
}
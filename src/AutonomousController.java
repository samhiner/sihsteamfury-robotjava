package org.firstinspires.ftc.teamcode;

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

@Autonomous(name="Autonomous Controller", group="Linear Opmode")

public class AutonomousController extends LinearOpMode {

    public static final int SCRIPT_CODE = 1; // 1 through 6
    public static final double INCHES_PER_SECOND = 109.5/3.1887;
    public static final double DEGREES_PER_SECOND = 720/3.81;

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor lfDrive;
    private DcMotor lbDrive;
    private DcMotor rfDrive;
    private DcMotor rbDrive;
    private Servo fixedServo1;
    private Servo fixedServo2;
    private CRServo contServo1;

    @Override
    public void runOpMode() {
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
        fixedServo1 = hardwareMap.get(Servo.class, "fixed1");
        fixedServo2 = hardwareMap.get(Servo.class, "fixed2");
        contServo1 = hardwareMap.get(CRServo.class, "cont");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        lfDrive.setDirection(DcMotor.Direction.FORWARD);
        lbDrive.setDirection(DcMotor.Direction.FORWARD);
        rfDrive.setDirection(DcMotor.Direction.REVERSE);
        rbDrive.setDirection(DcMotor.Direction.REVERSE);
    

        if (SCRIPT_CODE == 1) {
            turn(20, true);
            move(48);
            turn(47, false);
            move(26);
            depositFlag();
            turn(109, false);
            move(105);
        } else if (SCRIPT_CODE == 2) {
            move(48);
            turn(12, true);
            move(26);
            depositFlag();
            turn(158, false);
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
            turn(29, true);
            move(39);
            turn(104, true);
            move(78);
            depositFlag();
            turn(170, false);
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

    private void move(int inches) {
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
            double inchesMoved = ((System.nanoTime() / 1000000) - startTime) * INCHES_PER_SECOND;
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

    private void moveBackwards(int inches) {
        inches -= 8;
        
        double startTime = runtime.time();

        //make robot move forward
        lfDrive.setPower(-1);
        lbDrive.setPower(-1);
        rfDrive.setPower(-1);
        rbDrive.setPower(-1);

        //wait until robot has gone "feet" feet.
        telemetry.addData("Seconds", runtime.time() - startTime);
        telemetry.addData("Total Seconds", inches / INCHES_PER_SECOND);
        
        while (runtime.time() - startTime < inches / INCHES_PER_SECOND) {
            double inchesMoved = ((System.nanoTime() / 1000000) - startTime) * INCHES_PER_SECOND;
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
            telemetry.addData("Status", "Turning " + Integer.toString((int)degreesTurned) + "/" + Integer.toString(degrees) + " degrees.");
            telemetry.addData("Runtime", runtime.time());
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
        contServo1.setPower(1);
        double startTime = runtime.time();

        while (runtime.time() - startTime < 5) {
            telemetry.addData("Servo Spinning", (int)(runtime.time() - startTime) + "/5 seconds");
            telemetry.update();
        }
        contServo1.setPower(0);
    }
}
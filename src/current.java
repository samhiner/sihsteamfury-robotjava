package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo; //chris -- added
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="The Good Code", group="Linear Opmode")

public class current extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor lfDrive;
    private DcMotor lbDrive;
    private DcMotor rfDrive;
    private DcMotor rbDrive;
    private Servo fixedServo;
    private CRServo contServo;
    private CRServo servo;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        lfDrive = hardwareMap.get(DcMotor.class, "lf");
        lbDrive = hardwareMap.get(DcMotor.class, "lb");
        rfDrive = hardwareMap.get(DcMotor.class, "rf");
        rbDrive = hardwareMap.get(DcMotor.class, "rb");
        //A01: 
        fixedServo = hardwareMap.get(Servo.class, "fixed");
        contServo = hardwareMap.get(CRServo.class, "cont");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        lfDrive.setDirection(DcMotor.Direction.FORWARD);
        lbDrive.setDirection(DcMotor.Direction.FORWARD);
        rfDrive.setDirection(DcMotor.Direction.REVERSE);
        rbDrive.setDirection(DcMotor.Direction.REVERSE);
        
        
        /* Vars for A01.*/
        
        int numAClicks = 0;
        int hinge1Status = 0;
        int countdown_a = 0;

        int numBClicks = 0;
        int cont1Status = 0;
        int countdown_b = 0;
        
        boolean bClickedState = false;
        
        //TODO right stick is f/w to move robot forward and back. left stick is left and right to turn left and right

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Setup a variable for each drive wheel to save power level for telemetry
            double leftPower;
            double rightPower;

            // Choose to drive using either Tank Mode, or POV Mode
            // Comment out the method that's not used.  The default below is POV.

            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            double drive = gamepad1.left_stick_y;
            double turn = -gamepad1.right_stick_x;
            
            if (turn != 0) {
                leftPower = Range.clip(turn, -1.0, 1.0);
                rightPower = Range.clip(-turn, -1.0, 1.0);
            } else {
                leftPower = Range.clip(drive, -1.0, 1.0);
                rightPower = Range.clip(drive, -1.0, 1.0);
            }
            
            lfDrive.setPower(leftPower);
            lbDrive.setPower(leftPower);
            rfDrive.setPower(rightPower);
            rbDrive.setPower(rightPower);

            /* A01: Untested code that should control a fixed servo based on controller2.a*/
            
            if (gamepad2.a && countdown_a == 0) {
                numAClicks += 1;
                
                if (numAClicks == 3000) {
                    numAClicks = 0;
                }
                
                hinge1Status = numAClicks % 3;
                
                telemetry.addData("Status", "WWW");
                
                if (hinge1Status == 0) {
                    fixedServo.setPosition(0);
                } else if (hinge1Status == 1) {
                    fixedServo.setPosition(0.5);
                } else if (hinge1Status == 2) {
                    fixedServo.setPosition(1);
                }
                
                countdown_a = 5;
            }
            
            if (countdown_a != 0) {
                countdown_a -= 1;
            }

            if (gamepad2.b) {
                bClickedState = true;
            }
            
            if (!gamepad2.b && bClickedState) {
                bClickedState = false;
                
                numBClicks += 1;
                
                if (numBClicks == 3000) {
                    numBClicks = 0;
                }
                
                cont1Status = numBClicks % 4;

                if (cont1Status == 0) {
                    contServo.setPower(0);
                } else if (cont1Status == 1) {
                    contServo.setPower(1);
                } else if (cont1Status == 2) {
                    contServo.setPower(0);
                } else if (cont1Status == 3) {
                    contServo.setPower(-1);
                }
                
            }
            
            telemetry.addData("Status", cont1Status);
            telemetry.addData("Status", numBClicks);
            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Runtime: " + runtime.toString());
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
            telemetry.update();
        }
    }
    
    /* ALL OF THIS IS MIDDLE CODE TO CONNECT HIGH LEVEL TO LOW LEVEL
    
    private void move(int feet) {
        double FEET_PER_NANOSECOND = 3 * 1000000;
        
        long startTime = System.nanoTime();
        if (System.nanoTime() - startTime > 20 / FEET_PER_NANOSECOND) {
            
        }
    }
    
    private void turnLeft() {
        
    }
    
    private void turnRight() {
        
    }
    
    public void autononomous() {
        move(20);
        turnLeft(10);
        turnRight(3);
    }*/
}
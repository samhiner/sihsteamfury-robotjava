package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.navigation.Rotation;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
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
    private Servo fixedServo1;
    private Servo fixedServo2;
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
        fixedServo1 = hardwareMap.get(Servo.class, "fixed1");
        fixedServo2 = hardwareMap.get(Servo.class, "fixed2");
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
        
        int numBClicks = 0;
        int cont1Status = 0;
        
        int numXClicks = 0;
        int hinge2Status = 0;
        
        boolean aClickedState = false;
        boolean bClickedState = false;
        boolean xClickedState = false;
        
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
            
            if (gamepad2.a) {
                aClickedState = true;
            }
            
            if (!gamepad2.a && aClickedState) {
                aClickedState = false;
                
                numAClicks += 1;
                
                if (numAClicks == 3000) {
                    numAClicks = 0;
                }
                
                hinge1Status = numAClicks % 3;
                
                if (hinge1Status == 0) {
                    fixedServo1.setPosition(0);
                } else if (hinge1Status == 1) {
                    fixedServo1.setPosition(0.5);
                } else if (hinge1Status == 2) {
                    fixedServo1.setPosition(1);
                }
            }
            
            if (gamepad2.x) {
                xClickedState = true;
            }
            
            if (!gamepad2.x && xClickedState) {
                xClickedState = false;
                
                numXClicks += 1;
                
                if (numXClicks == 3000) {
                    numXClicks = 0;
                }
                
                hinge2Status = numXClicks % 3;
                
                if (hinge2Status == 0) {
                    fixedServo2.setPosition(0);
                } else if (hinge2Status == 1) {
                    fixedServo2.setPosition(0.5);
                } else if (hinge2Status == 2) {
                    fixedServo2.setPosition(1);
                }
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
            
            telemetry.addData("Main Hinge Number of Degrees / 180", hinge1Status);
            telemetry.addData("Number of A Clicks", numAClicks);

            telemetry.addData("\nTop Hinge Number of Degrees / 180", hinge2Status);
            telemetry.addData("Number of X Clicks", numXClicks);
            
            telemetry.addData("\nIntake Rotation Status", cont1Status);
            telemetry.addData("Number of B Clicks", numBClicks);

            

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Runtime: " + runtime.toString());
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
            telemetry.update();
        }
    }
}
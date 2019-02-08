package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous; //TODO is this necessary?
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled; //TODO is this necessary?

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

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
        fixedServo1 = hardwareMap.get(Servo.class, "fixed");
        contServo1 = hardwareMap.get(CRServo.class, "cont");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        lfDrive.setDirection(DcMotor.Direction.FORWARD);
        lbDrive.setDirection(DcMotor.Direction.FORWARD);
        rfDrive.setDirection(DcMotor.Direction.REVERSE);
        rbDrive.setDirection(DcMotor.Direction.REVERSE);
        
        //initialize handlers to automatically change servo speed based on buttons
        ServoHandler contServo1Handler = new ServoHandler(true, contServo, null);
        ServoHandler fixedServo1Handler = new ServoHandler(false, null, fixedServo);
        
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            double leftPower;
            double rightPower;

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

            //check if a click has been completed for the button given as a parameter
            //and change the servo speed accordingly
            fixedServo1Handler.checkClick(gamepad1.a);
            contServo1Handler.checkClick(gamepad1.b);

            telemetry.addData("Status", "Runtime: " + runtime.toString());
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
            telemetry.update();
        }
    }
}

public class ServoHandler {
    public void ServoHandler(boolean isContinuous, CRServo crServoClass, Servo fixedServoClass) {
        this.isContinuous = isContinuous;
        this.servoClass = crServoClass != null ? crServoClass : fixedServoClass;

        int this.numClicks = 0;
        boolean this.clickedState = false;
    }

    //if the given button has completed a press, update the servo's speed
    public void checkClick(boolean controllerClicked) {

        if (this.controllerClicked) {
            this.clickedState = true;
        }

        if (!this.controllerClicked && clickedState) {
            this.clickedState = false;

            this.numClicks++;

            //numClicks is set to zero when it reacher 12,000 to prevent it from getting too big to be an int
            //the code may require numClicks to use modulo 3 or 4, so that is why 12,000, which is divisible by 3 and 4, is the reset number
            if (this.numClicks == 12000) {
                this.numClicks = 0;
            }

            if (this.isContinuous) {                
                updateCrServo();
            } else {
                updateFixedServo();
            }
        }
    }

    private void updateCrServo() {
        int status = this.numClicks % 4;

        if (status == 0) {
            this.servoClass.setPower(0);
        } else if (status == 1) {
            this.servoClass.setPower(1);
        } else if (status == 2) {
            this.servoClass.setPower(0);
        } else if (status == 3) {
            this.servoClass.setPower(-1);
        }
    }

    private void updateFixedServo() {
        int status = this.numClicks % 3;
                
        if (status == 0) {
            this.servoClass.setPosition(0);
        } else if (status == 1) {
            this.servoClass.setPosition(0.5);
        } else if (status == 2) {
            this.servoClass.setPosition(1);
        }
    }
}
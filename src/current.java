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
    private DcMotor vertLift;
    private Servo frontPlate;

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
        vertLift = hardwareMap.get(DcMotor.class, "vert_lift");
        frontPlate = hardwareMap.get(Servo.class, "front_plate");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        lfDrive.setDirection(DcMotor.Direction.FORWARD);
        lbDrive.setDirection(DcMotor.Direction.FORWARD);
        rfDrive.setDirection(DcMotor.Direction.REVERSE);
        rbDrive.setDirection(DcMotor.Direction.REVERSE);

        int numAClicks = 0;
        int hinge1Status = 0;
        
        boolean aClickedState = false;
        
        //TODO right stick is f/w to move robot forward and back. left stick is left and right to turn left and right

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Setup a variable for each drive wheel to save power level for telemetry
            double leftPower;
            double rightPower;
            double vertLiftPower;

            //temporarily borrowed from https://ftcforum.usfirst.org/forum/ftc-technology/android-studio/6361-mecanum-wheels-drive-code-example
            double r = Math.hypot(-gamepad1.left_stick_x, gamepad1.left_stick_y);
            double robotAngle = Math.atan2(gamepad1.left_stick_y, -gamepad1.left_stick_x) - Math.PI / 4;
            double rightX = -gamepad1.right_stick_x;
            final double v1 = r * Math.cos(robotAngle) + rightX;
            final double v2 = r * Math.sin(robotAngle) - rightX;
            final double v3 = r * Math.sin(robotAngle) + rightX;
            final double v4 = r * Math.cos(robotAngle) - rightX;
            
            lfDrive.setPower(v1);
            rfDrive.setPower(v2);
            lbDrive.setPower(v3);
            rbDrive.setPower(v4);
            
            vertLiftPower = Range.clip(gamepad2.right_stick_y, -1.0, 1.0);
            vertLift.setPower(vertLiftPower);

            if (gamepad2.a) {
                aClickedState = true;
            }
            
            if (!gamepad2.a && aClickedState) {
                aClickedState = false;
                
                numAClicks += 1;
                
                if (numAClicks == 2000) {
                    numAClicks = 0;
                }
                
                hinge1Status = numAClicks % 2;
                
                if (hinge1Status == 0) {
                    frontPlate.setPosition(0.75);
                } else if (hinge1Status == 1) {
                    frontPlate.setPosition(0);
                } 
        
            }
            
            telemetry.addData("Current Position", vertLiftPower);
            telemetry.addData("Plate Up/Down", hinge1Status);
            telemetry.addData("Number of A-Button Clicks", numAClicks);

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Runtime: " + runtime.toString());
            telemetry.update();
        }
    }
}

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Basic: Linear OpMode", group="Linear Opmode")

public class Basic extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor lfDrive;
    private DcMotor lbDrive;
    private DcMotor rfDrive;
    private DcMotor rbDrive;
    ///
    private DcMotor turnBaseplate;
    ///

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
        ///
        baseplate = hardwareMap.get(DcMotor.class, "baseplate");
        hinge1 = hardwareMap.get(DcMotor.class, "hinge1");
        hinge2 = hardwareMap.get(DcMotor.class, "hinge2");
        claw = hardwareMap.get(DcMotor.class, "claw");
        ///

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        lfDrive.setDirection(DcMotor.Direction.FORWARD);
        lbDrive.setDirection(DcMotor.Direction.FORWARD);
        rfDrive.setDirection(DcMotor.Direction.REVERSE);
        rbDrive.setDirection(DcMotor.Direction.REVERSE);
        //
        baseplate.setDirection(DcMotor.Direction.FORWARD);
        hinge1.setDirection(DcMotor.Direction.FORWARD);
        hinge2.setDirection(DcMotor.Direction.FORWARD);
        claw.setDirection(DcMotor.Direction.FORWARD);
        //

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
            double drive = -gamepad1.right_stick_y;
            double turn =  gamepad1.left_stick_x;
            
            if (turn != 0) {
                leftPower = Range.clip(turn, -1.0, 1.0);
                rightPower = Range.clip(-turn, -1.0, 1.0);
            } else {
                leftPower = Range.clip(drive, -1.0, 1.0);
                rightPower = Range.clip(drive, -1.0, 1.0);
            }

            // Send calculated power to wheels
            lfDrive.setPower(leftPower);
            lbDrive.setPower(leftPower);
            rfDrive.setPower(rightPower);
            rbDrive.setPower(rightPower);

            //AMRITHA/SAAHITI PUT YOUR CODE BELOW THIS LINE

            double hingePower1 = 
            double hingePower2 = 

            hinge1.setPower();
            hinge2.setPower();

            //AMRITHA/SAAHITI PUT YOUR CODE ABOVE THIS LINE

            //FOLUWA PUT YOUR CODE BELOW THIS LINE

            double baseplatePower = 

            baseplate.setPower();

            //FOLUWA PUT YOUR CODE ABOVE THIS LINE

            //RYAN PUT YOUR CODE BELOW THIS LINE

            double clawPower =

            claw.setPower();

            //RYAN PUT YOUR CODE ABOVE THIS LINE

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
            telemetry.update();
        }
    }
}

/**
 *
 * Created by Keran Han
 *
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

/*
	Holonomic concepts from:
	http://www.vexforum.com/index.php/12370-holonomic-drives-2-0-a-video-tutorial-by-cody/0
   Robot wheel mapping:
          X FRONT X
        X           X
      X  FL       FR  X
              X
             XXX
              X
      X  BL       BR  X
        X           X
          X       X
*/
@TeleOp(name = "HolonomicDrivetrain", group = "TeleOp")
//@Disabled
public class teleOpHolonomicDrive extends OpMode {

    robotHardware robot   = new robotHardware();   // Use a Pushbot's hardware



    @Override
    public void init() {
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello Driver");    //
    }

     //Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY

    @Override
    public void init_loop() {
    }

    //Code to run ONCE when the driver hits PLAY

    @Override
    public void start() {
    }

    @Override
    public void loop() {


        // left stick controls direction
        // right stick X controls rotation

        double gamepad1LeftY = -gamepad1.left_stick_y;
        double gamepad1LeftX = -gamepad1.left_stick_x;
        double gamepad1RightX = -gamepad1.right_stick_x;


        // holonomic formulas

        double FrontLeft = -gamepad1LeftY - gamepad1LeftX - gamepad1RightX;
        double FrontRight = gamepad1LeftY - gamepad1LeftX - gamepad1RightX;
        double BackRight = gamepad1LeftY + gamepad1LeftX - gamepad1RightX;
        double BackLeft = -gamepad1LeftY + gamepad1LeftX - gamepad1RightX;

        //double speed setting, don't know if works
        if (gamepad1.a){

            FrontRight = Range.clip(FrontRight, -.5, .5);
            FrontLeft = Range.clip(FrontLeft, -.5, .5);
            BackLeft = Range.clip(BackLeft, -.5, .5);
            BackRight = Range.clip(BackRight, -.5, .5);

        } else {

            //clip the right/left values so that the values never exceed +/- 1
            FrontRight = Range.clip(FrontRight, -.8, .8);
            FrontLeft = Range.clip(FrontLeft, -.8, .8);
            BackLeft = Range.clip(BackLeft, -.8, .8);
            BackRight = Range.clip(BackRight, -.8, .8);
        }


        //write the values to the motors
        FrontRight = scaleInput(FrontRight);
        FrontLeft = scaleInput(FrontLeft);
        BackRight = scaleInput(BackRight);
        BackLeft = scaleInput(BackLeft);

        robot.motorFrontRight.setPower(FrontRight);
        robot.motorFrontLeft.setPower(FrontLeft);
        robot.motorBackLeft.setPower(BackLeft);
        robot.motorBackRight.setPower(BackRight);



        //Manipulator moves two servos when hit trigger
        if (gamepad2.a) {
            robot.servo.setPosition(1.0);
            robot.servo2.setPosition(0.0);
        }  else if (gamepad2.x){
            robot.servo.setPosition(.7);
            robot.servo2.setPosition(.3);
        } else {
            robot.servo.setPosition(.5);
            robot.servo2.setPosition(.5);
        }


        //lift code for gradual ascent and descent
        double liftPower = -gamepad2.left_stick_y;

        if (gamepad2.right_trigger > 0.2){
            liftPower = Range.clip(liftPower, -.5, .5);

        } else {

            liftPower = Range.clip(liftPower, -.75, .75);
        }

        robot.motorLift.setPower(liftPower);


		/*
		 * Telemetry for debugging
		 */
        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("Joy XL YL XR",  String.format("%.2f", gamepad1LeftX) + " " +
                String.format("%.2f", gamepad1LeftY) + " " +  String.format("%.2f", gamepad1RightX));
        telemetry.addData("f left pwr",  "front left  pwr: " + String.format("%.2f", FrontLeft));
        telemetry.addData("f right pwr", "front right pwr: " + String.format("%.2f", FrontRight));
        telemetry.addData("b right pwr", "back right pwr: " + String.format("%.2f", BackRight));
        telemetry.addData("b left pwr", "back left pwr: " + String.format("%.2f", BackLeft));

    }

    @Override
    public void stop() {

    }

    /*
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }

}
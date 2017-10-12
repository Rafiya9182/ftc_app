/**
 *
 * Created by Keran Han 10/08/17
 *
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name = "Lift Test", group = "TeleOp")
@Disabled
public class liftMotorTest extends OpMode {

    DcMotor motorLift;
    Servo servo;
    Servo servo2;



    @Override
    public void init() {
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
       motorLift = hardwareMap.dcMotor.get("lift");
        servo = hardwareMap.servo.get("servo");
        servo2 = hardwareMap.servo.get("servo2");

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello Driver");
        telemetry.update();
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

        //lift code for gradual ascent and descent
        double liftPower = -gamepad1.left_stick_y;
        liftPower = Range.clip(liftPower, -.5, 5);
        motorLift.setPower(liftPower);


        //manipulator code
        if (gamepad1.a) {
            servo.setPosition(1.0);
            servo2.setPosition(0.0);
        } else if (gamepad1.b) {
            servo.setPosition(.5);
            servo2.setPosition(.5);
        } else {
            servo.setPosition(0.0);
            servo2.setPosition(1.0);
        }



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
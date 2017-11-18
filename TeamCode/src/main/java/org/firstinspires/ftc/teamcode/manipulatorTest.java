/**
 *
 * Created by Keran Han
 *
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name = "Manipulator Test", group = "TeleOp")
//@Disabled
public class manipulatorTest extends OpMode {


    Servo servo;
    Servo servo2;


    @Override
    public void init() {
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
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

        //Manipulator moves two servos when hit trigger
        if (gamepad1.right_trigger > 0.2) {
            servo.setPosition(.37);
            servo2.setPosition(.63);
        } else if (gamepad1.left_trigger > 0.2) {
            servo.setPosition(.5);
            servo2.setPosition(.5);
        } else {
            servo.setPosition(.25);
            servo2.setPosition(.75);
        }


		/*
		 * Telemetry for debugging
		 */
        telemetry.addData("Servo", servo.getPosition());
        telemetry.addData("Servo2", servo2.getPosition());


    }

    @Override
    public void stop() {

    }

}


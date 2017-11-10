package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
 *  that performs the actual movement.
 */

@Autonomous(name="ColorBFar", group="Blue")
//@Disabled
public class autonBlueFBSCSPG extends LinearOpMode {

    /* Declare OpMode members. */
    robotHardware robot   = new robotHardware();   // Use a Pushbot's hardware

    private ElapsedTime runtime = new ElapsedTime();
    int[] colors;

    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED             = 0.3;

    static final double     LIFT_SPEED             = 0.2;

    static final double     SERVO_POSITION          = .2;
    static final double     SERVO_POSITION2          = .8;

    static final double     SERVO_START          = .5;
    static final double     SERVO_START2          = .5;


    ElapsedTime timer = new ElapsedTime();


    @Override
    public void runOpMode() {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);


        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        robot.motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :%7d",
            robot.motorFrontRight.getCurrentPosition(),
            robot.motorFrontLeft.getCurrentPosition(),
            robot.motorBackRight.getCurrentPosition(),
            robot.motorBackLeft.getCurrentPosition());

        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        robot.servo.setPosition(SERVO_START2);
        robot.servo2.setPosition(SERVO_START);

        //lift goes down
        encoderLiftDrive(LIFT_SPEED, -2, 2.0 );

        //grabbing glyph
        robot.servo.setPosition(SERVO_POSITION2);
        robot.servo2.setPosition(SERVO_POSITION);

        //lift goes up
        encoderLiftDrive(LIFT_SPEED, 2, 2.0 );

        //put down color sensor arm
        robot.servoColor.setPosition(.85);
        sleep(1000);// pause for servos to move


        //color sensor code for jewels
        //color sensor code for jewels
        runtime.reset();
        while (runtime.seconds() < 5 && opModeIsActive()) {
            colors = colorSensor();
            telemetry.addData("red", colors[0]);
            telemetry.addData("blue", colors[1]);
            telemetry.update();
            if (colors[0] > colors[1]) {
                sleep(500);
                encoderXDrive(DRIVE_SPEED, 2, 2, 5);
                robot.servoColor.setPosition(SERVO_START);
                encoderXDrive(DRIVE_SPEED, -2, -2, 5);

            } else if (colors[0] < colors[1]){
                encoderXDrive(DRIVE_SPEED, -2, -2, 5);
                robot.servoColor.setPosition(SERVO_START);
                encoderXDrive(DRIVE_SPEED, 2, 2, 5);

            } else {
                robot.servoColor.setPosition(.2);
            }
            break;
        }

        sleep(1000);
        //driving from CBS (close balancing stone) to cryptobox, robot front facing wall
        //X: (+, -) = left; (-, +) = right
        //Y: (-, +) = backward; (+, -) = forward
        encoderXDrive(TURN_SPEED, -12, -12, 7.0); //turn 180 to get gltph in front
        sleep(500);
        encoderYDrive(DRIVE_SPEED, 18, -18, 7.0); // continues right to front og cryptobox
        sleep(500);

        robot.servo.setPosition(SERVO_START2);
        robot.servo2.setPosition(SERVO_START);
        sleep(500);

        encoderYDrive(DRIVE_SPEED, -2, 2, 7.0);

        telemetry.addData("Path", "Complete");
        telemetry.update();
    }

    /*
     *  Method to perfmorm a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    public void encoderYDrive(double speedY,
                             double leftYInches, double rightYInches,
                             double timeoutYS) {
        int newLeftFrontTarget;
        int newLeftBackTarget;
        int newRightFrontTarget;
        int newRightBackTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftFrontTarget = robot.motorFrontLeft.getCurrentPosition() + (int)(leftYInches * COUNTS_PER_INCH);
            newLeftBackTarget = robot.motorBackLeft.getCurrentPosition() + (int)(leftYInches * COUNTS_PER_INCH);
            newRightFrontTarget = robot.motorFrontRight.getCurrentPosition() + (int)(rightYInches * COUNTS_PER_INCH);
            newRightBackTarget = robot.motorBackRight.getCurrentPosition() + (int)(rightYInches * COUNTS_PER_INCH);

            robot.motorFrontLeft.setTargetPosition(newLeftFrontTarget);
            robot.motorBackLeft.setTargetPosition(newLeftBackTarget);
            robot.motorFrontRight.setTargetPosition(newRightFrontTarget);
            robot.motorBackRight.setTargetPosition(newRightBackTarget);


            // Turn On RUN_TO_POSITION
            robot.motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();

            robot.motorFrontLeft.setPower(Math.abs(speedY));
            robot.motorBackLeft.setPower(Math.abs(speedY));
            robot.motorFrontRight.setPower(Math.abs(speedY));
            robot.motorBackRight.setPower(Math.abs(speedY));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutYS) &&
                    (robot.motorFrontLeft.isBusy() && robot.motorBackLeft.isBusy() &&
                        robot.motorFrontRight.isBusy() && robot.motorBackRight.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftFrontTarget,  newLeftBackTarget,
                        newRightFrontTarget, newRightBackTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        robot.motorFrontLeft.getCurrentPosition(),
                        robot.motorBackLeft.getCurrentPosition(),
                        robot.motorFrontRight.getCurrentPosition(),
                        robot.motorBackRight.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            robot.motorFrontLeft.setPower(0);
            robot.motorBackLeft.setPower(0);
            robot.motorFrontRight.setPower(0);
            robot.motorBackRight.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }
    public void encoderXDrive(double speedX,
                             double frontXInches, double backXInches,
                             double timeoutXS) {
        int newFrontLeftTarget;
        int newFrontRightTarget;
        int newBackLeftTarget;
        int newBackRightTarget;


        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newFrontRightTarget = robot.motorFrontRight.getCurrentPosition() + (int)(frontXInches * COUNTS_PER_INCH);
            newFrontLeftTarget = robot.motorFrontLeft.getCurrentPosition() + (int)(frontXInches * COUNTS_PER_INCH);
            newBackRightTarget = robot.motorBackRight.getCurrentPosition() + (int)(backXInches * COUNTS_PER_INCH);
            newBackLeftTarget = robot.motorBackLeft.getCurrentPosition() + (int)(backXInches * COUNTS_PER_INCH);

            robot.motorFrontRight.setTargetPosition(newFrontRightTarget);
            robot.motorFrontLeft.setTargetPosition(newFrontLeftTarget);
            robot.motorBackRight.setTargetPosition(newBackRightTarget);
            robot.motorBackLeft.setTargetPosition(newBackLeftTarget);

            // Turn On RUN_TO_POSITION
            robot.motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();

            robot.motorFrontRight.setPower(Math.abs(speedX));
            robot.motorFrontLeft.setPower(Math.abs(speedX));
            robot.motorBackRight.setPower(Math.abs(speedX));
            robot.motorBackLeft.setPower(Math.abs(speedX));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutXS) &&
                    (robot.motorFrontRight.isBusy() && robot.motorFrontLeft.isBusy() &&
                            robot.motorBackRight.isBusy() && robot.motorBackLeft.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newFrontRightTarget, newFrontLeftTarget,
                        newBackRightTarget, newBackLeftTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        robot.motorFrontRight.getCurrentPosition(),
                        robot.motorBackRight.getCurrentPosition(),
                        robot.motorFrontLeft.getCurrentPosition(),
                        robot.motorBackLeft.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            robot.motorFrontLeft.setPower(0);
            robot.motorBackLeft.setPower(0);
            robot.motorFrontRight.setPower(0);
            robot.motorBackRight.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }

    public void encoderLiftDrive(double speed,
                                 double Inches,
                                 double timeoutS) {
        int newTarget;


        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newTarget = robot.motorLift.getCurrentPosition() + (int)(Inches * COUNTS_PER_INCH);

            robot.motorLift.setTargetPosition(newTarget);

            // Turn On RUN_TO_POSITION
            robot.motorLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();

            robot.motorLift.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.motorLift.isBusy() )) {
            }
            // Stop all motion;
            robot.motorLift.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.motorLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        }
    }
    public int[] colorSensor () {
        int[] ret = new int[2];

        while (opModeIsActive()) {
            telemetry.addData("Red  ", robot.colorSensor.red());
            telemetry.addData("Blue ", robot.colorSensor.blue());
            ret[0] = robot.colorSensor.red();
            ret[1] = robot.colorSensor.blue();
            telemetry.update();
            break;
        }
        return ret;
    }


}

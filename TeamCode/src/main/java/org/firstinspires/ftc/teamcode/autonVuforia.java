package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/* created by Keran 10/03/17
 *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
 */

@Autonomous(name="AutoVuforia", group="Autonomous")
@Disabled
public class autonVuforia extends LinearOpMode {

    /* Declare OpMode members. */
    robotHardware robot   = new robotHardware();   // Use a Pushbot's hardware

    private ElapsedTime runtime = new ElapsedTime();

    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.6;

    vuforiaSensor vuforiaSensor = new vuforiaSensor();
    VuforiaLocalizer vuforia;

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


        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate");
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);



        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :%7d",
            robot.motorFrontRight.getCurrentPosition(),
            robot.motorFrontLeft.getCurrentPosition(),
            robot.motorBackRight.getCurrentPosition(),
            robot.motorBackLeft.getCurrentPosition());

        telemetry.update();


        vuforiaSensor.visionActivate();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Step through each leg of the path,
        // Note: Reverse movement is obtained by setting a negative distance (not speed)
        encoderXDrive(DRIVE_SPEED,  -12,  12, 7.0);  // S1: Forward 47 Inches with 5 Sec timeout
        encoderYDrive(DRIVE_SPEED,   -12, 12, 7.0);

        if (vuMark != RelicRecoveryVuMark.LEFT){
            encoderXDrive(DRIVE_SPEED, 12, 12, 5.0);
        }
        else if(vuMark != RelicRecoveryVuMark.CENTER){
            encoderXDrive(DRIVE_SPEED, 12, 12, 5.0);

        }
        else if(vuMark != RelicRecoveryVuMark.RIGHT){
            encoderXDrive(DRIVE_SPEED, 12, 12, 5.0);
        }


        robot.servo.setPosition(0.0);            // S4: Stop and close the claw.
        sleep(1000);     // pause for servos to move

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

    }



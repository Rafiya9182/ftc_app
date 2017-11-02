package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name="Driver", group = "TeleOp")
@Disabled
public class driverPeriod extends OpMode {

    DcMotorController controllerFR;
    DcMotorController controllerI;

    int motorBR = 1;
    int motorBL = 2;
    int motorIntake = 1;
    int motorShooter = 2;
    int EncoderTarget = 0;
    int timeout = 1;


   /*DigitalChannel limit;
   DigitalChannel limit1;
   boolean limitState;
   boolean limitState1;*/

    ElapsedTime runtime = new ElapsedTime();


    //VuforiaSensor vuforia = new VuforiaSensor(0,1,0);

    public driverPeriod() {
    }

    @Override
    public void init() {
        controllerFR = hardwareMap.dcMotorController.get("controller1");
        controllerI = hardwareMap.dcMotorController.get("controller2");
        controllerI.setMotorMode(motorShooter, DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        controllerI.setMotorMode(motorShooter, DcMotor.RunMode.RUN_USING_ENCODER);

       /*limit = hardwareMap.digitalChannel.get("limit");
       limit1 = hardwareMap.digitalChannel.get("limit1");*/
    }

    @Override
    public void loop() {


        //Left Motors
        float left = gamepad1.left_stick_y;

        left = Range.clip(left, -1, 1);

        left = (float) scaleInput(left);

        //Right Motors
        float right = -gamepad1.right_stick_y;

        right = Range.clip(right, -1, 1);

        right = (float) scaleInput(right);

        //Motor Powers
        controllerFR.setMotorPower(motorBR, -right);
        controllerFR.setMotorPower(motorBL, -left);


        //Limit Switch
       /*limitState = limit.getState();
       limitState1 = limit1.getState();*/

       /*telemetry.addData("LimitState", limitState);
       telemetry.addData("LimitState1", limitState1);*/


        //Intake
        if (gamepad2.right_trigger > 0.2) {
            controllerI.setMotorPower(motorIntake, .5);
        }
        else if(gamepad2.left_trigger > 0.2)
        {
            controllerI.setMotorPower(motorIntake, -.5);
        }
        else
        {
            controllerI.setMotorPower(motorIntake, 0.0);
        }

        if (gamepad2.a) {
            ElapsedTime timer = new ElapsedTime();
            int target = controllerI.getMotorCurrentPosition(motorShooter) + (-1050);
            controllerI.setMotorTargetPosition(motorShooter, target);

            controllerI.setMotorMode(motorShooter, DcMotor.RunMode.RUN_TO_POSITION);
            controllerI.setMotorPower(motorShooter, 1.0);
            timer.reset();
            while (controllerI.isBusy(motorShooter) && timer.seconds() < 1){
               /*if (limitState == true){
                   controllerI.setMotorPower(motorShooter, 0);
               }
               if (limitState1 == false){
                   controllerI.setMotorPower(motorShooter, 0);
               }*/
            }
            controllerI.setMotorPower(motorShooter, 0.0);
            controllerI.setMotorMode(motorShooter, DcMotor.RunMode.RUN_USING_ENCODER);
        }
        else if(gamepad2.dpad_down)
        {
            controllerI.setMotorPower(motorShooter, -.3);
        }
        else if(gamepad2.dpad_up)
        {
            controllerI.setMotorPower(motorShooter, .3);
        }
        else
        {
            controllerI.setMotorPower(motorShooter, 0.0);
        }


        telemetry.update();

        telemetry.addData("1 Text", "*** Robot Data***");
        telemetry.addData("2 Motor Power Left", String.format("%.2f", left));
        telemetry.addData("3 Motor Power Right", String.format("%.2f", right));

    }


    @Override
    public void stop() {

    }

    private double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);
        if (index < 0) {
            index = -index;
        } else if (index > 16) {
            index = 16;
        }

        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        return dScale;
    }

}





package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by abrah on 10/22/2016.
 */

@Autonomous(name = "Concept: VuforiaTest", group = "Concept")
@Disabled
public class vuforiaTest extends LinearOpMode {

    vuforiaSensor vuforia = new vuforiaSensor(0,1,0);
    public void runOpMode(){

        vuforia.setupVuforia();
        waitForStart();

        vuforia.visionActiavte();

        while(opModeIsActive()) {

            if (vuforia.isVis("Gears Target")){
                telemetry.addData("Robot Location", "{"+vuforia.getRobot("Gears Target","x")+","+vuforia.getRobot("Tools Target","y")+"}");
                telemetry.addData("Robot Rotation", vuforia.getRobot("Gears Target","angle"));
                telemetry.addData("X and Y from Gears", "{"+vuforia.getRobot("Gears Target","xAway")+","+vuforia.getRobot("Tools Target","yAway")+"}");
            }
            if (vuforia.isVis("Tools Target")){
                telemetry.addData("Robot Location", "{"+vuforia.getRobot("Tools Target","x")+","+vuforia.getRobot("Tools Target","y")+"}");
                telemetry.addData("Robot Rotation", vuforia.getRobot("Tools Target","angle"));
                telemetry.addData("X and Y from Tools", "{"+vuforia.getRobot("Tools Target","xAway")+","+vuforia.getRobot("Tools Target","yAway")+"}");
            }

            telemetry.update();
        }
    }
}


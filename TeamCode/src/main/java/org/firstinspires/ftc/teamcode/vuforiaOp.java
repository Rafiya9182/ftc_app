package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by Keran 10/02/17.
 */
@Autonomous(name="VuOp", group="Camera")
//@Disabled
public class vuforiaOp extends LinearOpMode {

    VuforiaLocalizer vuforia;


    public void runOpMode() throws InterruptedException {

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);


        parameters.vuforiaLicenseKey = "AcOQber/////AAAAGd+Wx7PVUULtlRxS6UeH3RgFL7O2kqLUIvryVwUgd7KQqprL1p5dzd2lpfSa0GIT1bxUPE33ZUWu8oe1S7pT7faMKK2buUugP8KJ3Vj2smsM7+K0LrTAWX/e5tW2zptEhgmH4XOGMD0rgiXHEopZWHVKfRzT2icGLg3ErUTYgHtNjLneooZhWiWDnXHEQFOc4JIoTz63aSIptNjN5q9fXbOwj1Wf4/nU+sxCU0EujqhoZWIztt2zI+mX1iOkGd/qyaSjaxdQ0q1E+YNx+v+gTZ5b0rmyr2ody3e4c4S6nTR9AhagdoDRL6VOm6v5CWWpNwM+ETWuYOBtGm5iTc/YxniKwXbClrFkXckzM+9A6lPt";


        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);


        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

        telemetry.addData(">", "Press Play to start");
        telemetry.update();

        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);

        relicTrackables.activate();

        waitForStart();

        while (opModeIsActive()) {


            if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
                /* Found an instance of the template. In the actual game, you will probably
                 * loop until this condition occurs, then move on to act accordingly depending
                 * on which VuMark was visible. */
                telemetry.addData("VuMark", "%s visible", vuMark);
            }
            else {
                telemetry.addData("VuMark", "not visible");
                }

            telemetry.update();

        }

    }
}


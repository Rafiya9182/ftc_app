package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.vuforia.HINT;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by abrah on 10/14/2016.
 */
@Autonomous(name="VufOp", group="vuf")
@Disabled
public class vuforiaOp extends LinearOpMode {

    public void runOpMode() throws InterruptedException {
        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        params.vuforiaLicenseKey = "AdksQ3j/////AAAAGVB9GUsSEE0BlMaVB7HcRZRM4Sv74bxusFbCpn3gwnUkr3GuOtSWhrTCHnTU/93+Im+JlrYI6///bytu1igZT48xQ6182nSTpVzJ2ZP+Q/sNzSg3qvIOMnjEptutngqB+e3mQ1+YTiDa9aZod1e8X7UvGsAJ3cfV+X/S3E4M/81d1IRSMPRPEaLpKFdMqN3AcbDpBHoqp82fAp7XWVN3qd/BRe0CAAoNsr26scPBAxvm9cizRG1WeRSFms3XkwFN6eGpH7VpNAdPPXep9RQ3lLZMTFQGOfiV/vRQXq/Tlaj/b7dkA12zBSW81MfBiXRxp06NGieFe7KvXNuu2aDyyXoaPFsI44FEGp1z/SVSEVR4";

        params.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES;

        VuforiaLocalizer vuforia = ClassFactory.createVuforiaLocalizer(params);


        VuforiaTrackables beacons = vuforia.loadTrackablesFromAsset("FTC_2016-17");
        beacons.get(0).setName("Wheels");
        beacons.get(1).setName("Tools");
        beacons.get(2).setName("Lego");
        beacons.get(3).setName("Gears");

        waitForStart();

        while (opModeIsActive()) {
            for (VuforiaTrackable beac : beacons) {
                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) beac.getListener()).getPose();

                if (pose != null) {
                    VectorF translation = pose.getTranslation();

                    telemetry.addData(beac.getName() + "-Translation", translation);

                    double degreesToTurn = Math.toDegrees(Math.atan2(translation.get(1), translation.get(2))); //vertical phone. for landscape phone translation.get(0), translation.get(2)

                    telemetry.addData(beac.getName() + "-Degrees", degreesToTurn);
                }
            }
            telemetry.update();
        }
    }
}


package org.firstinspires.ftc.teamcode;

import com.vuforia.HINT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/*
* This class is to do all the Vuforia calculations in different class instead of the main class.
*
* Note: this class is still very messy and can be simplified.
*
* HOWEVER: it works so you can always come back to this version if you need to.
*/
public class vuforiaSensor {
    // Variables to be used for later
    VuforiaLocalizer vuforiaLocalizer;
    VuforiaLocalizer.Parameters parameters;
    VuforiaTrackables visionTargets;
    VuforiaLocalizer vuforia;



    public static final String VUFORIA_KEY = "AcOQber/////AAAAGd+Wx7PVUULtlRxS6UeH3RgFL7O2kqLUIvryVwUgd7KQqprL1p5dzd2lpfSa0GIT1bxUPE33ZUWu8oe1S7pT7faMKK2buUugP8KJ3Vj2smsM7+K0LrTAWX/e5tW2zptEhgmH4XOGMD0rgiXHEopZWHVKfRzT2icGLg3ErUTYgHtNjLneooZhWiWDnXHEQFOc4JIoTz63aSIptNjN5q9fXbOwj1Wf4/nU+sxCU0EujqhoZWIztt2zI+mX1iOkGd/qyaSjaxdQ0q1E+YNx+v+gTZ5b0rmyr2ody3e4c4S6nTR9AhagdoDRL6VOm6v5CWWpNwM+ETWuYOBtGm5iTc/YxniKwXbClrFkXckzM+9A6lPt";

    //sets your starting location to whatever tile you are on. as well as how you have rotated it.
    public vuforiaSensor() {

    }

    //Tells the camera to start looking for the pictures
    public void visionActivate() {
        visionTargets.activate();
    }


    public void setupVuforia() {
        // Setup parameters to create localizer
        parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK; //You can use the front or back camera
        vuforiaLocalizer = ClassFactory.createVuforiaLocalizer(parameters);

        // These are the vision targets that we want to use
        // The string needs to be the name of the appropriate .xml file in the assets folder
        visionTargets = vuforiaLocalizer.loadTrackablesFromAsset("RelicVuMark");
        Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 3);

        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate");
        relicTrackables.activate();

        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);


        // Setup the gears to be tracked



    }
}
package org.firstinspires.ftc.teamcode;

import com.vuforia.HINT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/*
* This class is to do all the Vuforia calculations in different class instead of the main class.
*
* Note: this class is still very messy and can be simplified.
*
* HOWEVER: it works so you can always come back to this version if you need to.
*/
public class vuforiaSensor
{
    // Variables to be used for later
    VuforiaLocalizer vuforiaLocalizer;
    VuforiaLocalizer.Parameters parameters;
    VuforiaTrackables visionTargets;
    VuforiaTrackable wheels;
    VuforiaTrackable tools;
    VuforiaTrackable legos;
    VuforiaTrackable gears;
    VuforiaTrackableDefaultListener listener;
    VuforiaTrackableDefaultListener listen;
    VuforiaTrackableDefaultListener listening;
    VuforiaTrackableDefaultListener list;

    OpenGLMatrix lastKnownLocation;
    OpenGLMatrix phoneLocation;

    private float robotX = 0;
    private float robotY = 0;
    private float robotAngle = 0;

    float mmPerInch = 25.4f;
    float mmBotWidth = 18 * mmPerInch;            // ... or whatever is right for your robot
    float mmFTCFieldWidth = (12 * 12 - 2) * mmPerInch;   // the FTC field is ~11'10" center-to-center of the glass panels

    public static final String VUFORIA_KEY = "AdksQ3j/////AAAAGVB9GUsSEE0BlMaVB7HcRZRM4Sv74bxusFbCpn3gwnUkr3GuOtSWhrTCHnTU/93+Im+JlrYI6///bytu1igZT48xQ6182nSTpVzJ2ZP+Q/sNzSg3qvIOMnjEptutngqB+e3mQ1+YTiDa9aZod1e8X7UvGsAJ3cfV+X/S3E4M/81d1IRSMPRPEaLpKFdMqN3AcbDpBHoqp82fAp7XWVN3qd/BRe0CAAoNsr26scPBAxvm9cizRG1WeRSFms3XkwFN6eGpH7VpNAdPPXep9RQ3lLZMTFQGOfiV/vRQXq/Tlaj/b7dkA12zBSW81MfBiXRxp06NGieFe7KvXNuu2aDyyXoaPFsI44FEGp1z/SVSEVR4"; // Insert your own key here

    //sets your starting location to whatever tile you are on. as well as how you have rotated it.
    public vuforiaSensor(double x, double y, double a)
    {
        float u, v;
        u = (float)x-2.5f;
        v = (float)y-2.5f;

        //In order to not crash the program it needs to know your starting location.
        lastKnownLocation = createMatrix(-u*48*mmPerInch,v*48*mmPerInch, 0, 0, 0, (float)a);
    }

    //Tells the camera to start looking for the pictures
    public void visionActiavte()
    {
        visionTargets.activate();
    }

    //method to get any distance or angle needed.
    public float getRobot(String name, String param)
    {
        float ret = 10000;
        for(int i = 0; i < visionTargets.size(); i++) {
            if (visionTargets.get(i).getName() == name) {
                OpenGLMatrix latestLocation;
                switch (i)
                {
                    case 0:
                        latestLocation = listener.getUpdatedRobotLocation();
                        break;
                    case 1:
                        latestLocation = listen.getUpdatedRobotLocation();
                        break;
                    case 2:
                        latestLocation = listening.getUpdatedRobotLocation();
                        break;
                    case 3:
                        latestLocation = list.getUpdatedRobotLocation();
                        break;
                    default:
                        latestLocation = null;
                }

                if(latestLocation != null)
                    lastKnownLocation = latestLocation;

                float[] coordinates = lastKnownLocation.getTranslation().getData();

                robotX = coordinates[0];
                robotY = coordinates[1];
                robotAngle = Orientation.getOrientation(lastKnownLocation, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle;

                switch (param)
                {
                    case "x":
                        ret = robotX;
                        break;
                    case "y":
                        ret = robotY;
                        break;
                    case "angle":
                        ret = robotAngle;
                        break;
                    case "xAway":
                        ret = visionTargets.get(i).getLocation().getData()[12]-robotX;
                        break;
                    case "yAway":
                        ret = visionTargets.get(i).getLocation().getData()[13]-robotY;
                        break;
                    default:
                        ret = 10000;
                }

            }
        }
        return ret;
    }

    public boolean isVis(String name)
    {
        boolean ret = false;
        for (int i = 0; i < visionTargets.size(); i++) {
            if (visionTargets.get(i).getName() == name) {

                switch (i){
                    case 0:
                        ret = listener.isVisible();
                        break;
                    case 1:
                        ret = listen.isVisible();
                        break;
                    case 2:
                        ret = listening.isVisible();
                        break;
                    case 3:
                        ret = list.isVisible();
                        break;
                    default:
                        ret = false;
                }

            }
        }
        return ret;
    }


    public void setupVuforia()
    {
        // Setup parameters to create localizer
        parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK; //You can use the front or back camera
        vuforiaLocalizer = ClassFactory.createVuforiaLocalizer(parameters);

        // These are the vision targets that we want to use
        // The string needs to be the name of the appropriate .xml file in the assets folder
        visionTargets = vuforiaLocalizer.loadTrackablesFromAsset("FTC_2016-17");
        Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 4);


        // Setup the wheels to be tracked
        wheels = visionTargets.get(0); // 0 corresponds to the wheels target
        wheels.setName("Wheels Target");
        wheels.setLocation(createMatrix(12*mmPerInch, mmFTCFieldWidth/2, 0, 90, 0, 0));
        // Setup the tools to be tracked
        tools = visionTargets.get(1); // 0 corresponds to the wheels target
        tools.setName("Tools Target");
        tools.setLocation(createMatrix(-mmFTCFieldWidth/2, 36*mmPerInch, 0, 90, 0, 90));
        // Setup the legos to be tracked
        legos = visionTargets.get(2); // 0 corresponds to the wheels target
        legos.setName("Legos Target");
        legos.setLocation(createMatrix(-36*mmPerInch, mmFTCFieldWidth/2, 0, 90, 0, 0));
        // Setup the gears to be tracked
        gears = visionTargets.get(3); // 0 corresponds to the wheels target
        gears.setName("Gears Target");
        gears.setLocation(createMatrix(-mmFTCFieldWidth/2, -12*mmPerInch, 0, 90, 0, 90));

        // Set phone location on robot
        phoneLocation = createMatrix(0, mmBotWidth/2, 0, -90, 0, 90);

        // Setup listener and inform it of phone information
        listener = (VuforiaTrackableDefaultListener) wheels.getListener();
        listener.setPhoneInformation(phoneLocation, parameters.cameraDirection);

        listen = (VuforiaTrackableDefaultListener) tools.getListener();
        listen.setPhoneInformation(phoneLocation, parameters.cameraDirection);

        listening = (VuforiaTrackableDefaultListener) legos.getListener();
        listening.setPhoneInformation(phoneLocation, parameters.cameraDirection);

        list = (VuforiaTrackableDefaultListener) gears.getListener();
        list.setPhoneInformation(phoneLocation, parameters.cameraDirection);
    }

    // Creates a matrix for determining the locations and orientations of objects
    // Units are millimeters for x, y, and z, and degrees for u, v, and w
    private OpenGLMatrix createMatrix(float x, float y, float z, float u, float v, float w)
    {
        return OpenGLMatrix.translation(x, y, z).
                multiplied(Orientation.getRotationMatrix(
                        AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, u, v, w));
    }

    // Formats a matrix into a readable string
    private String formatMatrix(OpenGLMatrix matrix)
    {
        return matrix.formatAsTransform();
    }
}


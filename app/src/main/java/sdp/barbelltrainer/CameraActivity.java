package sdp.barbelltrainer;

/**
 * Created by Wuyuan on 6/6/17.
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import static org.opencv.core.Core.inRange;


public class CameraActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    int first = 1;
    private static String TAG = "MainActivity";
    JavaCameraView javaCameraView;
    Mat mRgba1,hueImg1, imgCanny, lowOrgHue, hueImg, upperOrgHue, OrgHueImg, lineImg, blankImg;
    int BlankImgFlag = 1;

//    Display display = getWindowManager().getDefaultDisplay();
//    getDisplaySize(display);
//
//    private static Point getDisplaySize(final Display display) {
//        final Point point = new Point();
//        try {
//            display.getSize(point);
//        } catch (java.lang.NoSuchMethodError ignore) { // Older device
//            point.x = display.getWidth();
//            point.y = display.getHeight();
//        }
//        return point;
//    }

    static {
        System.loadLibrary("MyOpencvLibs");
    }
    BaseLoaderCallback mLoaderCallBack = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case BaseLoaderCallback.SUCCESS:
                    javaCameraView.enableView();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_layout);

        javaCameraView = (JavaCameraView)findViewById(R.id.java_camera_view);
        javaCameraView.setVisibility(View.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(javaCameraView!=null)
            javaCameraView.disableView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(javaCameraView!=null)
            javaCameraView.disableView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(OpenCVLoader.initDebug()) {
            Log.i(TAG, "opencv loaded successfully");
            mLoaderCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }else {
            Log.i(TAG, "opencv not loaded");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, this, mLoaderCallBack);
        }
    }


    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba1 = new Mat(height, width, CvType.CV_8UC4);

        hueImg = new Mat(height, width, CvType.CV_8UC1);
        hueImg1 = new Mat(height, width, CvType.CV_8UC3);

        upperOrgHue = new Mat(height, width, CvType.CV_8UC3);
        lowOrgHue = new Mat(height, width, CvType.CV_8UC3);
        OrgHueImg = new Mat(height, width, CvType.CV_8UC3);

        imgCanny = new Mat(height, width, CvType.CV_8UC1);
        lineImg = new Mat(height, width, CvType.CV_8UC4);
        blankImg = new Mat(height, width, CvType.CV_8UC4);


    }

    @Override
    public void onCameraViewStopped() {
        mRgba1.release();
        lowOrgHue.release();
        blankImg.release();
        //   mRgba2.release();

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        //this is RGBA format
        mRgba1 = inputFrame.rgba();
        if(BlankImgFlag == 1) {
            blankImg = inputFrame.rgba();
            BlankImgFlag = 0;

        }




        //convert RGBA(4channel) to 3 channel
        Imgproc.cvtColor(mRgba1, hueImg1, Imgproc.COLOR_RGB2HSV);
        inRange(hueImg1, new Scalar(0, 255, 255), new Scalar(10, 255, 255), lowOrgHue);
        inRange(hueImg1, new Scalar(160, 100, 100), new Scalar(179, 255, 255), upperOrgHue);
        //  inRange(hueImg1, new Scalar(0, 57, 100), new Scalar(0, 66, 255), upperOrgHue);
        Core.addWeighted(lowOrgHue, 1.0, upperOrgHue, 1.0, 0.0, OrgHueImg);
        Imgproc.GaussianBlur(OrgHueImg, OrgHueImg, new Size(9, 9), 2, 2);

        //    Core.absdiff(imgGray1, imgGray2, differenceImage);
//  current.copyTo(previous);
        //  hueImg = inputFrame.rgba();
        OpencvNativeClass.MotionDetect(mRgba1.getNativeObjAddr(), OrgHueImg.getNativeObjAddr(), lineImg.getNativeObjAddr(), blankImg.getNativeObjAddr());
        return mRgba1;
    }
}


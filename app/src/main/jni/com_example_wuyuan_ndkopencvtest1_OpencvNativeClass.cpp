#include <com_example_wuyuan_ndkopencvtest1_OpencvNativeClass.h>

Rect objectBoundingRectangle = Rect(0,0,0,0);
int theObject[2] = {0,0};
int lastX = -1, lastY = -1;
int flag2 = 1;
JNIEXPORT jint JNICALL Java_sdp_barbelltrainer_OpencvNativeClass_MotionDetect
  (JNIEnv *, jclass, jlong mRgb1Adr, jlong thresholdAdr, jlong lineImgAdr, jlong blankImgAdr) {
        Mat& mRgb1 = *(Mat*)mRgb1Adr;
        Mat& threshold = *(Mat*)thresholdAdr;
        Mat& objTraj = *(Mat*)lineImgAdr;
        Mat& blankImg = *(Mat*)blankImgAdr;

        int flag = 0;
        int x = 0;
        int y = 0;

        searchForMovement(x, y, mRgb1, threshold, objTraj, blankImg);


        return 1;




}



string intToString(int number){

    //this function has a number input and string output
    std::stringstream ss;
    ss << number;
    return ss.str();
}

void searchForMovement(int& x, int& y, Mat& mRgb1, Mat& threshold, Mat& objTraj, Mat& blankImg){

    morphOps(threshold);

    Mat temp;
    threshold.copyTo(temp);
    //these two vectors needed for output of findContours
    vector< vector<Point> > contours;
    vector<Vec4i> hierarchy;
    //find contours of filtered image using openCV findContours function
    //In OpenCV, finding contours is like finding white object from black background.
    // So remember, object to be found should be white and background should be black.
    //CV_CHAIN_APPROX_SIMPLE to draw 4 points of the contour
    findContours(temp,contours,hierarchy,CV_RETR_CCOMP,CV_CHAIN_APPROX_SIMPLE );

    double refArea = 0;
    bool objectFound = false;
    if (hierarchy.size() > 0) {
        int numObjects = hierarchy.size();
        //if number of objects greater than MAX_NUM_OBJECTS we have a noisy filter
        if(numObjects<MAX_NUM_OBJECTS){
            for (int index = 0; index >= 0; index = hierarchy[index][0]) {

                Moments moment = moments((cv::Mat)contours[index]);
                double area = moment.m00;

                //if the area is less than 20 px by 20px then it is probably just noise
                //if the area is the same as the 3/2 of the image size, probably just a bad filter
                //we only want the object with the largest area so we safe a reference area each
                //iteration and compare it to the area in the next iteration.
                if(area>MIN_OBJECT_AREA && area<MAX_OBJECT_AREA && area>refArea){
                    x = moment.m10/area;
                    y = moment.m01/area;
                    objectFound = true;
                    refArea = area;
                }else{
                    LOGE("Object not found =================");
                    objectFound = false;
                }


            }
            //let user know you found an object
            if(objectFound ==true){
                putText(mRgb1,"Tracking Object",Point(0,50),2,1,Scalar(0,255,0),2);
                //draw object location on screen
                LOGE("X: %d, Y: %d", x, y);
                LOGE("lastX: %d, lastY: %d", lastX, lastY);
                drawObject(x,y, mRgb1, objTraj);
            }else {
                //reset the drawing
                objTraj = Mat::zeros(blankImg.size(), CV_8UC4);
                mRgb1 = mRgb1 + objTraj;
            }
                lastX = x;
                lastY = y;

        }else{
            putText(mRgb1,"TOO MUCH NOISE! ADJUST FILTER",Point(0,50),1,2,Scalar(0,0,255),2);
        }
    }

}

void morphOps(Mat &thresh){

    //create structuring element that will be used to "dilate" and "erode" image.
    //the element chosen here is a 3px by 3px rectangle

    Mat erodeElement = getStructuringElement( MORPH_RECT,Size(3,3));
    //dilate with larger element so make sure object is nicely visible
    Mat dilateElement = getStructuringElement( MORPH_RECT,Size(8,8));

    erode(thresh,thresh,erodeElement);
    erode(thresh,thresh,erodeElement);


    dilate(thresh,thresh,dilateElement);
    dilate(thresh,thresh,dilateElement);



}

void drawObject(int& x, int& y, Mat &frame, Mat& objTraj){

    //use some of the openCV drawing functions to draw crosshairs
    //on your tracked image!

    //UPDATE:JUNE 18TH, 2013
    //added 'if' and 'else' statements to prevent
    //memory errors from writing off the screen (ie. (-25,-25) is not within the window!)

    circle(frame,Point(x,y),20,Scalar(0,255,0),2);
    if(y-25>0)
        line(frame,Point(x,y),Point(x,y-25),Scalar(0,255,0),2);
    else line(frame,Point(x,y),Point(x,0),Scalar(0,255,0),2);
  //  frame = frame + objTraj;
    if(y+25<FRAME_HEIGHT)
        line(frame,Point(x,y),Point(x,y+25),Scalar(0,255,0),2);
    else line(frame,Point(x,y),Point(x,FRAME_HEIGHT),Scalar(0,255,0),2);
   // frame = frame + objTraj;
    if(x-25>0)
        line(frame,Point(x,y),Point(x-25,y),Scalar(0,255,0),2);
    else line(frame,Point(x,y),Point(0,y),Scalar(0,255,0),2);
   // frame = frame + objTraj;
    if(x+25<FRAME_WIDTH)
        line(frame,Point(x,y),Point(x+25,y),Scalar(0,255,0),2);
    else line(frame,Point(x,y),Point(FRAME_WIDTH,y),Scalar(0,255,0),2);

    //draw the trajectory
    if (lastX >= 0 && lastY >= 0 && x >= 0 && y >= 0) {
        line(objTraj, Point(x, y), Point(lastX, lastY), Scalar(255, 0, 0), 2);
    }
    frame = frame + objTraj;
    putText(frame,intToString(x)+","+intToString(y),Point(x,y+30),1,1,Scalar(0,255,0),2);

}



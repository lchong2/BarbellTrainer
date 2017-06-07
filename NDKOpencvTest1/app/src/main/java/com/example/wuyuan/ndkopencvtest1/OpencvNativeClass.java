package com.example.wuyuan.ndkopencvtest1;

/**
 * Created by Wuyuan on 5/30/17.
 */

public class OpencvNativeClass {
    public native static int MotionDetect(long mRgb1, long hueImg, long imgLine, long blankImg);
}

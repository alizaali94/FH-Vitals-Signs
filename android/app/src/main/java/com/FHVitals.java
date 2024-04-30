package com;

import android.content.Context;

import java.io.IOException;

import com.FHFile;

public class FHVitals {

    static {
        System.loadLibrary("FHVitals");
    }

    public static native String version();

    // System Test
    //----------------------------------------------------------------------------------------------------
    
    public static String test(Context context, String license, String host, int port) throws IOException {
        FHFile.copyAssets(context, "data");
        FHFile.copyAssets(context, "fhvitalssign");
        return testSDK(context, context.getFilesDir().getAbsolutePath(), license, host, port);
    }
    
    public static native String testSDK(Context context, String assets, String license, String host, int port);
    
    //----------------------------------------------------------------------------------------------------

    // Initializer
    //----------------------------------------------------------------------------------------------------

    public static final int SUCCESS                         =  1;
    public static final int ERROR_AUTHENTICATION_FAILED     =  0;
    public static final int ERROR_INITIALIZATION_FAILED     = -1;
    public static final int ERROR_ACTIVATION_FAILED         = -2;
    public static final int ERROR_INVALID_DEVICE            = -3;
    public static final int ERROR_INVALID_ASSETS            = -4;
    public static final int ERROR_INVALID_LICENSE           = -5;
    public static final int ERROR_SERVER_CONNECTION_FAILED  = -6;
    public static final int ERROR_INVALID_APPLICATION_ID    = -7;
    public static final int ERROR_CONFIG_NOT_SUPPORTED      = -8;
    public static int init(Context context, String license, String host, int port) throws IOException {
        FHFile.copyAssets(context, "data");
        return initJNI(context, context.getFilesDir().getAbsolutePath(), license, host, port);
    }

    public static native int initJNI(Context context, String assets, String license, String host, int port);

    public static native int activate();
    public static native int deactivate();
    
    //----------------------------------------------------------------------------------------------------


    // Public
    //----------------------------------------------------------------------------------------------------
    
    public static native boolean isActivated();
    
    public static native boolean resetFPS(int fps);
	public static native int getFPS();

    public static native void useFaceTrackMode(); // default
    public static native void useFixedRoIMode(int roi_center_x, int roi_center_y, int roi_width, int roi_height);
	
	public static native int[] detectFace(long frame);
	public static native boolean pushFrame(long frame);
	public static native boolean pushFrameWithFaceBox(long frame, int x1, int y1, int x2, int y2);

    public static final int VITALS_HR_HRV   = 1; 
    public static final int VITALS_BP       = 2; 
    public static final int VITALS_RESP     = 4; 
    public static final int VITALS_SPO2     = 8;
    public static final int VITAL_ALL       = VITALS_HR_HRV + VITALS_BP + VITALS_RESP + VITALS_SPO2;
    public static native void startMeasuring(int vital);
    public static native void stopMeasuring();
    
    public static native int[] getTrackedFaceBox();

    public static native int getMissFaceFrameCount();

    public static native boolean isAnyQueueFull();
    
    public static final int IQA_TYPE_BRIGHTNESS    = 0;
    public static final int IQA_TYPE_CONTRAST      = 1;
    public static final int IQA_TYPE_MOTION        = 2;
    public static native float getImageQualityScore(int type);

    public static final int VITALS_SYST_HR_HRV   = 0; 
    public static final int VITALS_SYST_BP       = 1; 
    public static final int VITALS_SYST_RESP     = 2; 
    public static final int VITALS_SYST_SPO2     = 3;
    public static native int getProcessedFrameCount(int syst);
    public static native float getSignalQualityScore(int syst);
    public static native float getAverageProcessTime(int syst);

    public static native float getHR();
    public static native float getSmoothedHR();
    
    public static native float[] getRecordRPPG();

	public static native float getPRQ();

    public static native float checkFeatureBufferForHRV();
    
    public static final int HRV_TYPE_LF          = 0;
    public static final int HRV_TYPE_HF          = 1;
    public static final int HRV_TYPE_LF_HF_RATIO = 2;
    public static final int HRV_TYPE_PLF         = 3;
    public static final int HRV_TYPE_SDNN        = 4;
    public static final int HRV_TYPE_RRIV        = 5;
    public static final int HRV_TYPE_MEAN_RR     = 6;
    public static final int HRV_TYPE_RMSSD       = 7;
    public static final int HRV_TYPE_PHF         = 8;
	public static final int HRV_TYPE_SD1         = 9;
	public static final int HRV_TYPE_SD2         = 10;
    public static native float getHRV(int type);
    
    public static native float getStressIndex();

	public static final int HRV_ANS_TYPE_PNS = 0;
	public static final int HRV_ANS_TYPE_SNS = 1;
	public static native float getANSIndex(int type);

    public static final int RADAR_TYPE_ACTIVITY    = 0;
    public static final int RADAR_TYPE_SLEEP       = 1;
    public static final int RADAR_TYPE_EQUILIBRIUM = 2;
    public static final int RADAR_TYPE_METABOLISM  = 3;
    public static final int RADAR_TYPE_HEALTH      = 4;
    public static final int RADAR_TYPE_RELAXATION  = 5;
    public static native int getRadar(int type);

    public static final int BP_MODE_BINARY         = 0;
    public static final int BP_MODE_TERNARY        = 1;
    public static native void setBloodPressureMode(int mode);

    public static final int FEMALE                 = 0;
    public static final int MALE                   = 1;
    public static final int BP_NORMAL              = 0;
    public static final int BP_PRE_HYPERTENSION    = 1;
    public static final int BP_HYPERTENSION        = 2;
    public static native void setSubjectInfo(
        int     gender,
        int     age,
        int     height, // cm
        int     weight, // kgw
        int     group   // BP_NORMAL, BP_PRE_HYPERTENSION, or BP_HYPERTENSION
                        //     For BP_MODE_BINARY mode, 
                        //         BP_NORMAL and BP_PRE_HYPERTENSION are in the same group, so
                        //         using either of them brings the same result.
    );

    public static native void resetCalibrationBP(float sbp, float dbp);

    public static native float getSBP();
    public static native float getDBP();
    
    public static native float getSmoothedSBP();
    public static native float getSmoothedDBP();

    public static native float getRR();
    public static native float getSmoothedRR();

    public static native float getSpO2();

    //----------------------------------------------------------------------------------------------------
}

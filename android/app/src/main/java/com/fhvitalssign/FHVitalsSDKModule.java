package com.fhvitalssign;

import android.util.Log;
import androidx.annotation.NonNull;

import com.FHVitals;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableNativeArray;

import java.io.IOException;

public class FHVitalsSDKModule extends ReactContextBaseJavaModule {
    private ReactApplicationContext reactContext_;
    private String TAG = "FHVitalsSDKModule";
    public FHVitalsSDKModule(ReactApplicationContext reactContext) {
        reactContext_ = reactContext;
    }

    @NonNull
    @Override
    public String getName() {
        return "FHVitalsSDKModule";
    }

    @ReactMethod
    public void version(Promise promise) {
        //Log.i(TAG,"version");
        String ret = FHVitals.version();
        promise.resolve(ret);
    }

    @ReactMethod
    public void initSDK(String assets,String license, String host, int port,Promise promise) {
        //Log.i(TAG,"initSDK");
        try {
            int code = FHVitals.init(reactContext_.getApplicationContext(),license,host,port);
            Log.i(TAG,"init finish " + code);
            promise.resolve(code);
        } catch (IOException e) {
            Log.i(TAG,"FHVitals init exception " + e);
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void activate(Promise promise) {

        //Log.i(TAG,"activate");
        int code = FHVitals.activate();
        promise.resolve(code);
    }

    @ReactMethod
    public void deactivate(Promise promise) {

        //Log.i(TAG,"deactivate");
        int code = FHVitals.deactivate();
        promise.resolve(code);
    }

    @ReactMethod
    public void isActivated(Promise promise) {

        //Log.i(TAG,"isActivated");
        boolean ret = FHVitals.isActivated();

        promise.resolve(ret);
    }

    @ReactMethod
    public void resetFPS(int fps,Promise promise) {

        //Log.i(TAG,"resetFPS " + fps);
        boolean ret = FHVitals.resetFPS(fps);

        promise.resolve(ret);
    }

    @ReactMethod
    public void getFPS(Promise promise) {

        //Log.i(TAG,"getFPS");
        int ret = FHVitals.getFPS();

        promise.resolve(ret);
    }

    @ReactMethod
    public void useFaceTrackMode() {

        //Log.i(TAG,"useFaceTrackMode");
        FHVitals.useFaceTrackMode();
    }

    @ReactMethod
    public void useFixedRoIMode(int roi_center_x, int roi_center_y, int roi_width, int roi_height) {

        //Log.i(TAG,"useFixedRoIMode");
        FHVitals.useFixedRoIMode(roi_center_x,roi_center_y,roi_width,roi_height);
    }

    @ReactMethod
    public void startMeasuring(int vital) {

        //Log.i(TAG,"stopMeasuring");
        FHVitals.startMeasuring(vital);
    }

    @ReactMethod
    public void stopMeasuring() {

        //Log.i(TAG,"stopMeasuring");
        FHVitals.stopMeasuring();
    }

    @ReactMethod
    public void getTrackedFaceBox(Promise promise) {

        //Log.i(TAG,"getTrackedFaceBox");
        int[] ret = FHVitals.getTrackedFaceBox();

        WritableNativeArray array = new WritableNativeArray();
        for (int r : ret) {
            array.pushInt(r);
        }

        promise.resolve(array);
    }

    @ReactMethod
    public void getMissFaceFrameCount(Promise promise) {

        //Log.i(TAG,"getMissFaceFrameCount");
        int ret = FHVitals.getMissFaceFrameCount();

        promise.resolve(ret);
    }

    @ReactMethod
    public void isAnyQueueFull(Promise promise) {

        //Log.i(TAG,"isAnyQueueFull");
        boolean ret = FHVitals.isAnyQueueFull();

        promise.resolve(ret);
    }

    @ReactMethod
    public void getImageQualityScore(int type,Promise promise) {

        //Log.i(TAG,"getImageQualityScore " + type);
        float ret = FHVitals.getImageQualityScore(type);

        promise.resolve(ret);
    }

    @ReactMethod
    public void getProcessedFrameCount(int syst,Promise promise) {

        //Log.i(TAG,"getProcessedFrameCount " + syst);
        float ret = FHVitals.getProcessedFrameCount(syst);

        promise.resolve(ret);
    }

    @ReactMethod
    public void getSignalQualityScore(int syst,Promise promise) {

        //Log.i(TAG,"getSignalQualityScore " + syst);
        float ret = FHVitals.getSignalQualityScore(syst);

        promise.resolve(ret);
    }

    @ReactMethod
    public void getAverageProcessTime(int syst,Promise promise) {

        //Log.i(TAG,"getSignalQualityScore " + syst);
        float ret = FHVitals.getAverageProcessTime(syst);

        promise.resolve(ret);
    }

    @ReactMethod
    public void getHR(Promise promise) {

        //Log.i(TAG,"getHR ");
        float ret = FHVitals.getHR();

        promise.resolve(ret);
    }

    @ReactMethod
    public void getSmoothedHR(Promise promise) {

        //Log.i(TAG,"getSmoothedHR");
        float ret = FHVitals.getSmoothedHR();

        promise.resolve(ret);
    }

    @ReactMethod
    public void getRecordRPPG(Promise promise) {

        //Log.i(TAG,"getRecordRPPG");
        float[] ret = FHVitals.getRecordRPPG();

        WritableNativeArray array = new WritableNativeArray();
        for (float r : ret) {
            array.pushDouble(r);
        }

        promise.resolve(array);
    }

    @ReactMethod
    public void getPRQ(Promise promise) {

        //Log.i(TAG,"getPRQ");
        float ret = FHVitals.getPRQ();

        promise.resolve(ret);
    }

    @ReactMethod
    public void checkFeatureBufferForHRV(Promise promise) {

        //Log.i(TAG,"checkFeatureBufferForHRV");
        float ret = FHVitals.checkFeatureBufferForHRV();

        promise.resolve(ret);
    }

    @ReactMethod
    public void getHRV(int type,Promise promise) {

        //Log.i(TAG,"getHRV " + type);
        float ret = FHVitals.getHRV(type);

        promise.resolve(ret);
    }

    @ReactMethod
    public void getStressIndex(Promise promise) {

        //Log.i(TAG,"getStressIndex");
        float ret = FHVitals.getStressIndex();

        promise.resolve(ret);
    }

    @ReactMethod
    public void getANSIndex(int type,Promise promise) {

        //Log.i(TAG,"getANSIndex " + type);
        float ret = FHVitals.getANSIndex(type);

        promise.resolve(ret);
    }

    @ReactMethod
    public void getRadar(int type,Promise promise) {

        //Log.i(TAG,"getRadar");
        int ret = FHVitals.getRadar(type);

        promise.resolve(ret);
    }

    @ReactMethod
    public void setBloodPressureMode(int mode) {

        //Log.i(TAG,"setBloodPressureMode " + mode);
        FHVitals.setBloodPressureMode(mode);
    }

    @ReactMethod
    public void setSubjectInfo(int gender,int age,int height,int weight,int group ) {

        //Log.i(TAG,"setSubjectInfo " + gender + "," + age + ","+ height + ","+ weight + ","+ group);
        FHVitals.setSubjectInfo( gender, age, height, weight, group );

    }

    @ReactMethod
    public void resetCalibrationBP(float sbp, float dbp) {

        //Log.i(TAG,"resetCalibrationBP " + sbp + "," + dbp);
        FHVitals.resetCalibrationBP(sbp,dbp);

    }

    @ReactMethod
    public void getSBP(Promise promise) {

        //Log.i(TAG,"getSBP ");
        float ret = FHVitals.getSBP();

        promise.resolve(ret);
    }

    @ReactMethod
    public void getDBP(Promise promise) {

        //Log.i(TAG,"getDBP ");
        float ret = FHVitals.getDBP();

        promise.resolve(ret);
    }

    @ReactMethod
    public void getSmoothedSBP(Promise promise) {

        //Log.i(TAG,"getSmoothedSBP ");
        float ret = FHVitals.getSmoothedSBP();

        promise.resolve(ret);
    }

    @ReactMethod
    public void getSmoothedDBP(Promise promise) {

        //Log.i(TAG,"getSmoothedDBP ");
        float ret = FHVitals.getSmoothedDBP();

        promise.resolve(ret);
    }

    @ReactMethod
    public void getRR(Promise promise) {

        //Log.i(TAG,"getRR ");
        float ret = FHVitals.getRR();

        promise.resolve(ret);
    }

    @ReactMethod
    public void getSmoothedRR(Promise promise) {

        //Log.i(TAG,"getSmoothedRR ");
        float ret = FHVitals.getSmoothedRR();

        promise.resolve(ret);
    }

    @ReactMethod
    public void getSpO2(Promise promise) {

        //Log.i(TAG,"getSpO2 ");
        float ret = FHVitals.getSpO2();

        promise.resolve(ret);
    }

}

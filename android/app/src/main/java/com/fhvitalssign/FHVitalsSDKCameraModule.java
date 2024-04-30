package com.fhvitalssign;

import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import com.FHVitals;
import static com.fhvitalssign.FHCameraModule.mCamera;

public class FHVitalsSDKCameraModule extends ReactContextBaseJavaModule {
    // Log.d("FHVitalsSDKCameraModule", "This is a debug log message");

    private Thread pushFrameTask;
    private boolean taskEnable;

    public static final String REACT_CLASS = "FHVitalsSDKCameraModule";

    FHVitalsSDKCameraModule(ReactApplicationContext context) {
        super(context);
        taskEnable = false;
    }

    @Override
    public String getName() {
        Log.d("FHVitalsSDKCameraModule", "Function() called");
        return REACT_CLASS;
    }

    @ReactMethod
    public void startPushFrame(){
        mCamera.clearFrameQueue();
        mCamera.pushFrameToQueueEnabled(true);
        startTask();
        Log.d("FHVitalsSDKCameraModule", "startPushFrame() called");
    }

    @ReactMethod
    public void stopPushFrame(){
        mCamera.pushFrameToQueueEnabled(false);
        mCamera.clearFrameQueue();
        stopTask();
    }

    private void startTask(){
        taskEnable = true;
        pushFrameTask = new Thread(()->{
            while (taskEnable){
                FHVitals.pushFrame(mCamera.getFrameData());
                Log.e("Test","Push frame");
            }

        });
        pushFrameTask.start();
    }

    private void stopTask() {
        taskEnable = false;
    }



}

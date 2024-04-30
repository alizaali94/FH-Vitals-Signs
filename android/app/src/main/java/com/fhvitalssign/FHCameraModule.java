package com.fhvitalssign;

import android.util.Log;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class FHCameraModule extends ReactContextBaseJavaModule {

    public static FHCamera mCamera;
    public static final String REACT_CLASS = "FHCameraModule";

    FHCameraModule(ReactApplicationContext context) {
        super(context);
        mCamera = new FHCamera(context);

    }

    // add to CalendarModule.java
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @ReactMethod
    public void checkCameraAccess(Promise promise) {
        boolean ret = mCamera.checkPermission();
        promise.resolve(ret);
    }

    @ReactMethod
    public void lock3A(){
        mCamera.lock3A();
    }

    @ReactMethod
    public void unlock3A(){
        mCamera.unlock3A();
    }

    @ReactMethod
    public void getFrameData(Promise promise){
        long frame = mCamera.getFrameData();
        promise.resolve(frame);
    }

    @ReactMethod
    public void getFrameQueueSize(Promise promise){
        int ret = mCamera.getFrameQueueSize();
        promise.resolve(ret);
    }

    @ReactMethod
    public void clearFrameBuffer(){
        mCamera.clearFrameQueue();
    }

    @ReactMethod
    public void startPushFrameToBuffer() {
        mCamera.pushFrameToQueueEnabled(true);
    }

    @ReactMethod
    public void stopPushFrameToBuffer(){
        mCamera.pushFrameToQueueEnabled(false);
        mCamera.clearFrameQueue();
    }

}
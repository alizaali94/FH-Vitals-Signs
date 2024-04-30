package com.fhvitalssign;

import static android.Manifest.permission.CAMERA;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Surface;

import androidx.annotation.RequiresApi;
import com.facebook.react.bridge.ReactApplicationContext;
import org.opencv.android.OpenCVLoader;

public class FHCamera {

    public static int DEFAULT_CAMERA_FRAME_WIDTH  = 480;
    public static int DEFAULT_CAMERA_FRAME_HEIGHT = 640;
    public static int DEFAULT_FRAME_RATE   = 30;
    public static Handler BGHandler;
    public static HandlerThread BGHandlerThread;

    private ReactApplicationContext mActivity;
    private FHCameraConfig mCameraConfig;
    private FHCameraController mCameraController;
    public FHCamera(ReactApplicationContext activity){
        mActivity = activity;
        mCameraController = null;
        OpenCVLoader.initDebug();
        startBGThread();
    }

    // Camera Setting
    //----------------------------------------------------------------------------------------------------

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void openFrontCam(int frame_rate, int frame_width, int frame_height, int frame_buffer_size, Surface surface){
        mCameraConfig       = new FHCameraConfig(mActivity, frame_rate);
        mCameraController = new FHCameraController();
        mCameraController.startCamera(mCameraConfig, frame_width, frame_height, frame_buffer_size, surface);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void closeFrontCamera() {
        if (mCameraController != null) {
            mCameraController.closeCamera();
            mCameraController = null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public boolean checkPermission() {
        boolean ret  = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permission = mActivity.checkSelfPermission(CAMERA);

            if (permission >= 0) {
                ret = true;
            }
        }
        return ret;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void unlock3A(){
        mCameraController.unlock3A();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void lock3A(){
        mCameraController.lock3A();
    }

    public void pushFrameToQueueEnabled(boolean enable){
        mCameraController.pushFrameToQueueEnabled(enable);
    }

    public long getFrameData() {
        return mCameraController.getFrameData();
    }

    public int getFrameQueueSize(){
        return mCameraController.getFrameBufferSize();
    }

    public void clearFrameQueue() {
        mCameraController.clearFrameBuffer();
    }

    private void startBGThread() {
        BGHandlerThread = new HandlerThread("MainBGThread");
        BGHandlerThread.start();
        BGHandler = new Handler(BGHandlerThread.getLooper());
    }

    private void stopBGThread() {
        BGHandlerThread.quit();
        try {
            BGHandlerThread.join();
            BGHandlerThread = null;
            BGHandler = null;
        }
        catch (InterruptedException e) {
            Log.e("BGThread",e.getMessage());
        }
    }

}



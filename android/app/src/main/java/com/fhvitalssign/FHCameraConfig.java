package com.fhvitalssign;

import static com.fhvitalssign.FHCamera.BGHandler;
import static com.fhvitalssign.FHCamera.DEFAULT_FRAME_RATE;

import android.content.Context;

import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.facebook.react.bridge.ReactApplicationContext;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class FHCameraConfig {
    public int m_frame_rate;

    private ReactApplicationContext m_activity;
    private String m_camera_id;

    public FHCameraConfig(ReactApplicationContext activity, int frame_rate) {
        m_activity = activity;
        m_frame_rate = (frame_rate > 0) ? frame_rate : DEFAULT_FRAME_RATE;
    }

    public boolean startCamera(CameraDevice.StateCallback state_callback) {
        return setupCamera() && openCamera(state_callback);
    }

    public boolean setupCamera() {
        boolean ret = false;
        CameraManager manager = (CameraManager) m_activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            m_camera_id = "";
            for (String camera_id : manager.getCameraIdList()) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(camera_id);
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if ((facing == null) || (facing == CameraCharacteristics.LENS_FACING_FRONT)) {
                    m_camera_id = camera_id;
                    Log.e("Camera: ", "Get Front Camera");
                    ret = true;
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    public boolean openCamera(CameraDevice.StateCallback state_callback) {
        boolean ret = false;
        CameraManager manager = (CameraManager) m_activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            ret = true;
            manager.openCamera(m_camera_id, state_callback, BGHandler);
            Log.e("Camera: ", "Open camera");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

}

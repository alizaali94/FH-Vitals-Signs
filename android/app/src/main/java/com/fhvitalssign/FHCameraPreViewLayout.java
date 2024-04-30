package com.fhvitalssign;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;

import static com.fhvitalssign.FHCameraModule.mCamera;

public class FHCameraPreViewLayout extends RelativeLayout {

    private int FRAME_RATE = 30;
    private int FRAME_WIDTH  = 480;
    private int FRAME_HEIGHT = 640;
    private int FRAME_BUFFER_SIZE = 300;

    private TextureView previewTextureView;
    protected static Surface PreviewSurface;

    public FHCameraPreViewLayout(Context context) {
        super(context);
    }

    public void setUpWidget()
    {
        boolean ret = mCamera.checkPermission();
        while (!ret)
        {
            ret = mCamera.checkPermission();
            Log.e("FHCameraPreview", "Wait camera access");
            SystemClock.sleep(1000);
        }

        previewTextureView = (TextureView) findViewById(R.id.Image_Plane);
        Matrix crop = new Matrix();
        crop.setScale((float) 1.0, (float) 1.0, FRAME_WIDTH, FRAME_HEIGHT);
        previewTextureView.setTransform(crop);
        previewTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {
                SurfaceTexture st = previewTextureView.getSurfaceTexture();
                st.setDefaultBufferSize(FRAME_WIDTH, FRAME_HEIGHT);
                PreviewSurface = new Surface(st);
                mCamera.openFrontCam(FRAME_RATE, FRAME_WIDTH, FRAME_HEIGHT, FRAME_BUFFER_SIZE, PreviewSurface);
            }

            @Override
            public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surfaceTexture) {
                mCamera.closeFrontCamera();
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surfaceTexture) {

            }
        });
    }

    public FHCameraPreViewLayout(Context context, AttributeSet attrs, int defStyles){
        super(context, attrs, defStyles);
    }

    public FHCameraPreViewLayout(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public void onFinishInflate(){
        super.onFinishInflate();
        setUpWidget();
    }

}
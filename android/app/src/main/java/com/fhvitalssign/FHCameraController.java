package com.fhvitalssign;

import static com.fhvitalssign.FHCamera.DEFAULT_CAMERA_FRAME_WIDTH;
import static com.fhvitalssign.FHCamera.DEFAULT_CAMERA_FRAME_HEIGHT;
import static com.fhvitalssign.FHCamera.BGHandler;
import com.FHVitals;

import android.graphics.ImageFormat;
import android.os.Build;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.util.Log;
import android.view.Surface;
import androidx.annotation.RequiresApi;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.opencv.imgproc.Imgproc;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class FHCameraController {

    private int m_image_width;
    private int m_image_height;
    private boolean m_push_frame_enable = false;

    private Surface m_preview_surface;
    private FHCameraConfig m_camera2_ctl;

    private CaptureRequest.Builder m_capture_request_builder;
    private CameraCaptureSession m_camera_capture_session;
    private CaptureRequest m_capture_request;

    private CameraDevice.StateCallback m_state_callback;

    private ImageReader m_image_reader;

    private BlockingQueue<Long> m_frame_queue = new LinkedBlockingQueue<Long>(300);

    public void startCamera(FHCameraConfig camera2_ctl, int width, int height, int frame_buffer_size ,Surface surface) {

        m_preview_surface = surface;
        m_camera2_ctl = camera2_ctl;
        m_image_width  = (width  > 0) ? width  : DEFAULT_CAMERA_FRAME_WIDTH;
        m_image_height = (height > 0) ? height : DEFAULT_CAMERA_FRAME_HEIGHT;
        m_frame_queue = new LinkedBlockingQueue<Long>(frame_buffer_size);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            m_state_callback = new CameraDevice.StateCallback() {
                @Override
                public void onOpened(CameraDevice camera) {
                    startCapturing(camera);
                }

                @Override
                public void onDisconnected(CameraDevice camera) {
                    camera.close();
                }

                @Override
                public void onError(CameraDevice camera, int error) {
                    camera.close();
                }
            };

            m_camera2_ctl.startCamera(m_state_callback);
        }
    }

    public void closeCamera() {
        FHVitals.stopMeasuring();
        m_camera_capture_session.close();
    }

    private void startCapturing(CameraDevice camera_device) {
        setupImageReader();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            try {
                m_capture_request_builder = camera_device.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);

                m_capture_request_builder.addTarget(m_image_reader.getSurface());
                m_capture_request_builder.addTarget(m_preview_surface);

                m_capture_request_builder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, new android.util.Range<>(m_camera2_ctl.m_frame_rate, m_camera2_ctl.m_frame_rate));
                m_capture_request_builder.set(CaptureRequest.NOISE_REDUCTION_MODE, CameraMetadata.NOISE_REDUCTION_MODE_OFF);

                m_capture_request_builder.set(CaptureRequest.CONTROL_AF_MODE, CameraMetadata.CONTROL_MODE_AUTO);
                m_capture_request_builder.set(CaptureRequest.CONTROL_AE_MODE, CameraMetadata.CONTROL_AE_ANTIBANDING_MODE_AUTO);
                m_capture_request_builder.set(CaptureRequest.CONTROL_AF_MODE, CameraMetadata.CONTROL_AE_MODE_ON);
                m_capture_request_builder.set(CaptureRequest.CONTROL_AWB_MODE, CameraMetadata.CONTROL_AWB_MODE_AUTO);

                camera_device.createCaptureSession(
                        Arrays.asList(m_image_reader.getSurface(), m_preview_surface),
                        new CameraCaptureSession.StateCallback() {
                            @Override
                            public void onConfigured(CameraCaptureSession session) {
                                try {
                                    m_capture_request        = m_capture_request_builder.build();
                                    m_camera_capture_session = session;
                                    m_camera_capture_session.setRepeatingRequest(m_capture_request, null, BGHandler);
                                } catch (CameraAccessException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onConfigureFailed(CameraCaptureSession session) {

                            }
                        }, BGHandler);

            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void lock3A(){
        Log.d("Camera2Preview", "lock3A");
        try {
            m_capture_request_builder.set(CaptureRequest.CONTROL_AE_LOCK, true);
            m_capture_request_builder.set(CaptureRequest.CONTROL_AWB_LOCK, true);

            // run AF algorithm once
            m_capture_request_builder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_START);
            m_camera_capture_session.capture(m_capture_request_builder.build(), null, BGHandler);

            m_capture_request_builder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_IDLE);
            m_camera_capture_session.setRepeatingRequest(m_capture_request_builder.build(), null, BGHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void unlock3A(){
        Log.d("Camera2Preview", "unlock3A");
        try {
            m_capture_request_builder.set(CaptureRequest.CONTROL_AE_LOCK, false);
            m_capture_request_builder.set(CaptureRequest.CONTROL_AWB_LOCK, false);
            m_capture_request_builder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_START);
            m_camera_capture_session.setRepeatingRequest(m_capture_request_builder.build(), null, BGHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void pushFrameToQueueEnabled(boolean enable){
        m_push_frame_enable = enable;
    }

    public long getFrameData() {
        long data = 0L;
        try {
            data =  m_frame_queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    public int getFrameBufferSize(){
        return m_frame_queue.size();
    }

    public void clearFrameBuffer(){
        m_frame_queue.clear();
    }


    private void setupImageReader() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            m_image_reader = ImageReader.newInstance(m_image_width, m_image_height, ImageFormat.YUV_420_888, m_camera2_ctl.m_frame_rate);
            m_image_reader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = reader.acquireLatestImage();

                    if (image != null && m_push_frame_enable) {
                        Mat cv_img = toMat(image);
                        Core.transpose(cv_img, cv_img);
                        Core.flip(cv_img, cv_img, 0);
                        Core.flip(cv_img, cv_img, 1);

                        try {
                            m_frame_queue.put(cv_img.getNativeObjAddr());
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    }
                    else
                    {
                        Log.e("[FHCamera2Preview] ", "Image is null");
                    }
                    image.close();

                }
            }, null);
        }
    }


    // Image Processing: YUV to BGR
    //--------------------------------------------------------------------------------------------//

    private static byte[] m_frame_cvt_nv21;
    private static int m_frame_cvt_y_size = 0;
    private static int m_frame_cvt_u_size = 0;
    private static int m_frame_cvt_v_size = 0;

    private static Mat m_frame_cvt_mat;
    private static Mat m_frame_color_mat;
    private static int m_frame_cvt_mat_row = 0;
    private static int m_frame_cvt_mat_col = 0;

    private static Mat toMat(Image image) {

        boolean nv21_malloc = false;

        if (image != null) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                ByteBuffer y = image.getPlanes()[0].getBuffer();
                ByteBuffer u = image.getPlanes()[1].getBuffer();
                ByteBuffer v = image.getPlanes()[2].getBuffer();

                if (m_frame_cvt_y_size != y.remaining()) {
                    m_frame_cvt_y_size = y.remaining();
                    nv21_malloc = true;
                }

                if (m_frame_cvt_u_size != u.remaining()) {
                    m_frame_cvt_u_size = u.remaining();
                    nv21_malloc = true;
                }

                if (m_frame_cvt_v_size != v.remaining()) {
                    m_frame_cvt_v_size = v.remaining();
                    nv21_malloc = true;
                }

                if (nv21_malloc) {
                    m_frame_cvt_nv21 = new byte[ m_frame_cvt_y_size + m_frame_cvt_u_size + m_frame_cvt_v_size ];
                }

                y.get(m_frame_cvt_nv21, 0, m_frame_cvt_y_size);
                v.get(m_frame_cvt_nv21, m_frame_cvt_y_size, m_frame_cvt_v_size);
                u.get(m_frame_cvt_nv21, m_frame_cvt_y_size + m_frame_cvt_v_size, m_frame_cvt_u_size);

                int row = image.getHeight() + image.getHeight() / 2;
                int col = image.getWidth();

                if ((m_frame_cvt_mat_row != row) || (m_frame_cvt_mat_col != col)) {
                    m_frame_cvt_mat_row = row;
                    m_frame_cvt_mat_col = col;
                    m_frame_cvt_mat = new Mat(m_frame_cvt_mat_row, m_frame_cvt_mat_col, CvType.CV_8UC1);
                }

                if (m_frame_color_mat == null) {
                    m_frame_color_mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
                }

                m_frame_cvt_mat.put(0, 0, m_frame_cvt_nv21);

                Imgproc.cvtColor(m_frame_cvt_mat, m_frame_color_mat, Imgproc.COLOR_YUV2BGR_NV21, 3);
            }
        }

        return m_frame_color_mat;
    }
}

package com.fhvitalssign;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;

public class FHCameraPreViewLayoutManager extends SimpleViewManager<RelativeLayout> {

    public static final String REACT_CLASS = "FHCameraPreViewLayout";

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public FHCameraPreViewLayout createViewInstance(ThemedReactContext context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FHCameraPreViewLayout preview = (FHCameraPreViewLayout)inflater.inflate(R.layout.preview, null);
        return preview;
    }

}
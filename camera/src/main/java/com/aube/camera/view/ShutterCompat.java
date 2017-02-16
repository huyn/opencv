package com.aube.camera.view;

import android.view.View;
import android.widget.TextView;

import com.aube.camera.presenter.IPreviewPresenter;

/**
 * Created by huyaonan on 17/2/15.
 */
public class ShutterCompat implements ShutterButton.OnVideoRecordProgressListener {

    private IPreviewPresenter mPreviewPresenter;
    private TextView mShutterTxt;
    private ShutterButton mShutterButton;

    public ShutterCompat(IPreviewPresenter previewPresenter, TextView textView, ShutterButton button) {
        this.mShutterButton = button;
        this.mShutterTxt = textView;
        this.mPreviewPresenter = previewPresenter;

        mShutterButton.setOnVideoRecordProgressListener(this);
    }

    public void switchToVideo(boolean toVideo) {
        mShutterTxt.setVisibility(toVideo ? View.VISIBLE : View.GONE);
        mShutterButton.swithToVideo(toVideo);
    }

    @Override
    public void onVideoProgress(long progressMillis) {
        long second = progressMillis%1000;
        mShutterTxt.setText(second + ":" + (progressMillis-second*1000)/10);
    }

    @Override
    public void onVideoEnd() {
        mPreviewPresenter.onVideoRecordEnd();
    }

    @Override
    public void onVideoStart() {
        mPreviewPresenter.onVideoRecordStart();
    }

    @Override
    public void onTakePhoto() {
        mPreviewPresenter.takePhoto();
    }
}

package com.aube.camera.presenter.impl;

/**
 * Created by huyaonan on 17/2/9.
 */
public interface CameraInstance {
    public void onCreate();
    public void onResume();
    public void onPause();
    public void takePictureOrRecordVideo();
    public boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);
}

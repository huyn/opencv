package com.aube.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.aube.camera.presenter.IPreviewController;
import com.aube.camera.presenter.IPreviewPresenter;
import com.aube.camera.presenter.TextureViewPresenter;

/**
 * Created by huyaonan on 17/2/8.
 */
public class PreviewAndRecordActivity extends Activity implements IPreviewController {

    private static final int REQUEST_PICK_IMAGE = 10000;

    private IPreviewPresenter iPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        iPresenter = new SurfaceViewPresenter(this);
        iPresenter = new TextureViewPresenter(this);
    }

    @Override
    public void setView(int resId) {
        setContentView(resId);
    }

    @Override
    public View findView(int id) {
        return findViewById(id);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(iPresenter != null && iPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults))
            return;
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        iPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        iPresenter.onPause();
    }

    private void loadPhotoOrVideoFromAlbum() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case REQUEST_PICK_IMAGE:
                if (resultCode == RESULT_OK) {
//                    handleImage(data.getData());
                } else {
                    finish();
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
}

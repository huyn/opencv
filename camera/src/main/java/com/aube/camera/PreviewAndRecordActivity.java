package com.aube.camera;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.aube.camera.presenter.IPreviewController;
import com.aube.camera.presenter.IPreviewPresenter;
import com.aube.camera.presenter.SurfaceViewPresenter;

/**
 * Created by huyaonan on 17/2/8.
 */
public class PreviewAndRecordActivity extends Activity implements IPreviewController {

    private IPreviewPresenter iPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iPresenter = new SurfaceViewPresenter(this);
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
}

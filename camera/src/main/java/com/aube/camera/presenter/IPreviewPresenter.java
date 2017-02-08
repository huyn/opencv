package com.aube.camera.presenter;

import android.content.Context;
import android.view.View;

/**
 * Created by huyaonan on 17/2/8.
 */
public interface IPreviewPresenter {
    public void setContentView(int resId);
    public void findAndInitView();
    public View findViewById(int id);
    public Context getContext();
}

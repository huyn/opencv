package com.aube.camera.presenter;

import android.content.Context;
import android.view.View;

/**
 * Created by huyaonan on 17/2/8.
 */
public interface IPreviewController {
    public void setView(int resId);
    public View findView(int id);
    public Context getContext();
}

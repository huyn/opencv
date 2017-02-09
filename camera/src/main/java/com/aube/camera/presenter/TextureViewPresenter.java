package com.aube.camera.presenter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.SurfaceHolder;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.aube.camera.R;
import com.aube.camera.util.DisplayUtil;
import com.aube.camera.v21.AutoFitTextureView;
import com.aube.camera.v21.CameraInstance;
import com.aube.camera.v21.CameraTextureInstanceV21;

/**
 * Created by huyaonan on 17/2/8.
 */
public class TextureViewPresenter implements IPreviewPresenter, CameraInterface.CamOpenOverCallback {

    private IPreviewController mController;

    private AutoFitTextureView mTexture;
    private Button mTakePhoto;
    private float previewRate = -1f;

    private CameraInstance mCameraInstance;

    public TextureViewPresenter(IPreviewController controller) {
        mController = controller;
        setContentView(R.layout.activity_preview_texture);
        if(Build.VERSION.SDK_INT >= 21)
            mCameraInstance = new CameraTextureInstanceV21(mTexture, (Activity) getContext());
        else
            mCameraInstance = new CameraTextureInstance(mTexture, (Activity) getContext());
        mCameraInstance.onCreate();
    }

    @Override
    public void setContentView(int resId) {
        if(mController != null)
            mController.setView(resId);

        findAndInitView();
    }

    @Override
    public void findAndInitView() {
        mTexture = (AutoFitTextureView) findViewById(R.id.preview_surfaceview);
        mTakePhoto = (Button)findViewById(R.id.preview_takephoto);

        initViewParams();
        mTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraInterface.getInstance().doTakePicture();
            }
        });
    }

    @Override
    public View findViewById(int id) {
        if(mController == null)
            throw new InvalidControllerException("mController is null");
        return mController.findView(id);
    }

    @Override
    public Context getContext() {
        if(mController == null)
            throw new InvalidControllerException("mController is null");
        return mController.getContext();
    }

    @Override
    public boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        return mCameraInstance.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onResume() {
        mCameraInstance.onResume();
    }

    @Override
    public void onPause() {
        mCameraInstance.onPause();
    }

    private void initViewParams(){
        ViewGroup.LayoutParams params = mTexture.getLayoutParams();
        Point p = DisplayUtil.getScreenMetrics(getContext());
        params.width = p.x;
        params.height = p.y;
        previewRate = DisplayUtil.getScreenRate(getContext()); //默认全屏的比例预览
        mTexture.setLayoutParams(params);

        //手动设置拍照ImageButton的大小为120dip×120dip,原图片大小是64×64
        ViewGroup.LayoutParams p2 = mTakePhoto.getLayoutParams();
        p2.width = DisplayUtil.dip2px(getContext(), 80);
        p2.height = DisplayUtil.dip2px(getContext(), 80);
        mTakePhoto.setLayoutParams(p2);
    }

    @Override
    public void cameraHasOpened() {
    }

}

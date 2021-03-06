package com.aube.camera.presenter;

import android.content.Context;
import android.graphics.Point;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.aube.camera.R;
import com.aube.camera.presenter.impl.CameraInterface;
import com.aube.camera.util.DisplayUtil;
import com.aube.camera.presenter.impl.CameraInstance;
import com.aube.camera.view.PreviewSurfaceView;

/**
 * Created by huyaonan on 17/2/8.
 */
public class SurfaceViewPresenter implements IPreviewPresenter, CameraInterface.CamOpenOverCallback {

    private IPreviewController mController;

    private PreviewSurfaceView mSurface;
    private Button mTakePhoto;
    private float previewRate = -1f;

    private CameraInstance mCameraInstance;

    public SurfaceViewPresenter(IPreviewController controller) {
        mController = controller;
        setContentView(R.layout.activity_preview_surfaceview);
//        if(Build.VERSION.SDK_INT >= 21)
//            mCameraInstance = new CameraTextureInstanceV21(mSurface, (Activity) getContext());
//        else
//            mCameraInstance = new CameraTextureInstance(mSurface, (Activity) getContext());
//        mCameraInstance.onCreate();
    }

    @Override
    public void setContentView(int resId) {
        if(mController != null)
            mController.setView(resId);

        findAndInitView();
    }

    @Override
    public void findAndInitView() {
        mSurface = (PreviewSurfaceView) findViewById(R.id.preview_surfaceview);
        mTakePhoto = (Button)findViewById(R.id.preview_takephoto);

        initViewParams();
        mSurface.addListener(new PreviewSurfaceView.OnSurfaceViewStateListener() {
            @Override
            public void onSurfaceCreated(SurfaceHolder holder) {
                CameraInterface.getInstance().doOpenCamera(SurfaceViewPresenter.this);
            }
        });
        mTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
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
        return false;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void takePhoto() {
        CameraInterface.getInstance().doTakePicture();
//                mCameraInstance.takePictureOrRecordVideo();
    }

    @Override
    public void onVideoRecordStart() {

    }

    @Override
    public void onVideoRecordEnd() {

    }

    private void initViewParams(){
        ViewGroup.LayoutParams params = mSurface.getLayoutParams();
        Point p = DisplayUtil.getScreenMetrics(getContext());
        params.width = p.x;
        params.height = p.y;
        previewRate = DisplayUtil.getScreenRate(getContext()); //默认全屏的比例预览
        mSurface.setLayoutParams(params);

        //手动设置拍照ImageButton的大小为120dip×120dip,原图片大小是64×64
        ViewGroup.LayoutParams p2 = mTakePhoto.getLayoutParams();
        p2.width = DisplayUtil.dip2px(getContext(), 80);
        p2.height = DisplayUtil.dip2px(getContext(), 80);
        mTakePhoto.setLayoutParams(p2);
    }

    @Override
    public void cameraHasOpened() {
        SurfaceHolder holder = mSurface.getSurfaceHolder();
        CameraInterface.getInstance().doStartPreview(holder, previewRate);
    }

}

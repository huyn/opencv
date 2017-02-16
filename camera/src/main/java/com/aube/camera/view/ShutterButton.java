package com.aube.camera.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by huyaonan on 17/2/15.
 */
public class ShutterButton extends View {

    private static final int mBorderColor = 0xa0333333;
    private static final int mMiddleColor = 0xff333333;
    private static final int mVideoCountDownBorderColor = 0xa0ff0000;
    private static final int mVideoCountDownMiddleColor = 0xffff0000;
    private static final int mBorderWidth = 40;
    private static final int mBorderGap = 10;
    private Paint mPaint;

    private boolean mTakingPhoto = true;
    private float mProgress = 0;

    private static final int DURATION = 10;
    private AtomicBoolean mRecording = new AtomicBoolean(false);

    private OnVideoRecordProgressListener mListener;
    private Handler mHandler;
    private Timer mTimer;
    private long mVideoStarttime=0;

    private static final int CODE = 0xff1;

    public ShutterButton(Context context) {
        this(context, null);
    }

    public ShutterButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShutterButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();

        //默认灰色外边
        mPaint.setColor(mBorderColor);
        canvas.drawRoundRect(new RectF(0, 0, width, width), width/2, width/2, mPaint);

        if(!mTakingPhoto) {
            //录制视频时候的外边
            mPaint.setColor(mVideoCountDownBorderColor);
            canvas.drawArc(new RectF(0, 0, width, width), 270, mProgress*360/DURATION, true, mPaint);
        }

        //绘制中心按钮
        mPaint.setColor(mMiddleColor);
        canvas.drawRoundRect(new RectF(mBorderWidth, mBorderWidth, width-mBorderWidth, width-mBorderWidth), width/2-mBorderWidth, width/2-mBorderWidth, mPaint);

        if(!mTakingPhoto) {
            //录制视频时候的中心按钮
            mPaint.setColor(mVideoCountDownMiddleColor);
            canvas.drawRoundRect(new RectF(mBorderWidth + mBorderGap, mBorderWidth + mBorderGap, width - mBorderWidth - mBorderGap, width - mBorderWidth - mBorderGap), width/2-mBorderWidth-mBorderGap, width/2-mBorderWidth-mBorderGap, mPaint);
        }
    }

    public void swithToVideo(boolean toVideo) {
        mTakingPhoto = !toVideo;
    }

    public void setOnVideoRecordProgressListener(OnVideoRecordProgressListener listener) {
        mListener = listener;
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(mRecording.get()) {
                    if (mVideoStarttime <= 0) {
                        mVideoStarttime = System.currentTimeMillis();
                    } else {
                        long progress = System.currentTimeMillis() - mVideoStarttime;
                        mProgress = progress*1f/1000;
                        invalidate();
                        if(progress >= DURATION * 1000) {
                            mRecording.set(false);
                            mListener.onVideoEnd();
                            stopRecording();
                        } else {
                            mListener.onVideoProgress(DURATION*1000-progress);
                        }
                    }
                } else {
                    stopRecording();
                }
            }
        };
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTakingPhoto) {
                    mListener.onTakePhoto();
                } else {
                    if(mRecording.get()) {
                        mRecording.set(false);
                        mListener.onVideoEnd();
                        stopRecording();
                    } else {
                        mRecording.set(true);
                        mListener.onVideoStart();
                        startRecording();
                    }
                }
            }
        });
    }

    private void startRecording() {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(CODE);
            }
        }, 0, 100);
    }

    private void stopRecording() {
        if(mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    public interface OnVideoRecordProgressListener {
        public void onVideoProgress(long progressMillis);
        public void onVideoEnd();
        public void onVideoStart();
        public void onTakePhoto();
    }

}

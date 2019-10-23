package com.as.somelikeyouapp;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.transition.TransitionManager;

import com.as.base_lib_sly.base.ui.activity.BaseActivity;
import com.as.somelikeyouapp.GlideUtils.progress.ProgressInterceptor;
import com.as.somelikeyouapp.GlideUtils.progress.ProgressListener;
import com.as.somelikeyouapp.databinding.ActivityMainBinding;
import com.as.somelikeyouapp.service.LocalMusicService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;

public class MainActivity extends BaseActivity<ActivityMainBinding> {


    private String imagePath = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1571301284993&di=19c23733860dd63ddc8fa138e5179a44&imgtype=0&src=http%3A%2F%2Fgss0.baidu.com%2F-vo3dSag_xI4khGko9WTAnF6hhy%2Fzhidao%2Fpic%2Fitem%2Fa8773912b31bb051c5404b92367adab44aede020.jpg";
    private ProgressDialog progressDialog;
    private MyReceiver myReceiver;
    public static SeekBar seekbar;
    private ConstraintLayout mainBot;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected void initView() {


//        startActivity(new Intent(this, TabActivity.class));

        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("media_progress");
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, intentFilter);


        Intent intent = new Intent(this, LocalMusicService.class);
        intent.setAction(LocalMusicService.ACTION_Play);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.startForegroundService(intent);
        } else {
            this.startService(intent);
        }


    }

    @Override
    protected void initData() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("图片加载中···");
    }

    @Override
    protected void initListener() {

//        seekbar = mViewBinding.seekbar;

        ProgressInterceptor.addListener(imagePath, new ProgressListener() {
            @Override
            public void onProgress(int progress) {
                progressDialog.setProgress(progress);
            }
        });


        Glide.with(this)
                .load(imagePath)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(new DrawableImageViewTarget(mViewBinding.ivHead) {
                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        progressDialog.show();
                    }

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        super.onResourceReady(resource, transition);
                        progressDialog.dismiss();
                        ProgressInterceptor.removeListener(imagePath);
                    }
                });


        ConstraintSet mConstraintSet1 = new ConstraintSet();
        ConstraintSet mConstraintSet2 = new ConstraintSet();

        mainBot = mViewBinding.mainBot;
        //把默认 constraintLayout 布局放到 mConstraintSet1 中
        mConstraintSet1.clone(mainBot);

        //把标定位置变换的 constraintLayout 布局放到 mConstraintSet2 中
        mConstraintSet2.clone(this, R.layout.bot);


        mViewBinding.star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(mainBot);
                mConstraintSet2.applyTo(mainBot);
            }
        });

        mViewBinding.star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(mainBot);
                mConstraintSet1.applyTo(mainBot);
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }

        if (myReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
            myReceiver = null;
        }

    }


    public static class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (TextUtils.equals("media_progress", action)) {
                int current_progress = intent.getIntExtra("current_progress", 0);
                int total_progress = intent.getIntExtra("total_progress", 0);
                if (total_progress > 0) {


                }


            }
        }
    }


}

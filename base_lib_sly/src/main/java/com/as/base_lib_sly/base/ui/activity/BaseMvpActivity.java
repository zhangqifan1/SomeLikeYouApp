package com.as.base_lib_sly.base.ui.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;


import com.as.base_lib_sly.base.ui.TUtil;
import com.as.base_lib_sly.base.ui.mvp.BaseIModel;
import com.as.base_lib_sly.base.ui.mvp.BaseIView;
import com.as.base_lib_sly.base.ui.mvp.BasePresenter;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.ParameterizedType;

/**
 * -----------------------------
 * Created by zqf on 2018/1/22.
 * ---------------------------
 */

public abstract class BaseMvpActivity<P extends BasePresenter, M extends BaseIModel, B extends ViewDataBinding> extends BaseStatuBarActivity implements BaseIView {

    protected M mMode;
    protected P mPresenter;

    /**
     * 上下文
     */
    protected Context mContext;

    protected B mViewBinding;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView = getLayoutInflater().inflate(this.getLayoutId(), null, false);
        rootView.setFitsSystemWindows(true);
        beforeSetContentView(rootView);
        this.setContentView(rootView);
        try {
            mViewBinding = DataBindingUtil.bind(rootView);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            initFontScale();
            beforeInitView(savedInstanceState);
            initView();
            initPresenter();
            initData();
            initListener();
        }
    }

    protected void beforeSetContentView(View rootView) {

    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this) && useEventBus()) {//加上判断
            EventBus.getDefault().register(this);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        if (EventBus.getDefault().isRegistered(this) && useEventBus()) {
            EventBus.getDefault().unregister(this);
//            EventBus.getDefault().removeAllStickyEvents();
        }
        super.onDestroy();
    }

    private void initFontScale() {
        Configuration configuration = getResources().getConfiguration();
        configuration.fontScale = (float) 1;
        //0.85 小, 1 标准大小, 1.15 大，1.3 超大 ，1.45 特大
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        getBaseContext().getResources().updateConfiguration(configuration, metrics);
    }

    /**
     * 屏幕发生改变调用
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void setContentView(View rootView) {
        super.setContentView(rootView);
    }

    /**
     * 获取资源文件布局
     *
     * @return 资源布局文件layout
     */
    protected abstract int getLayoutId();

    protected void beforeInitView(Bundle savedInstanceState) {
        mContext = this;
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            getBundleExtras(extras);
        }
    }

    /**
     * 获取传递的bundle数据
     *
     */
    protected abstract void getBundleExtras(Bundle extras);

    /**
     * 初始化view
     */
    public abstract void initView();


    /**
     * 初始化对象
     */
    protected abstract void initData();

    /**
     * 简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通
     */
    public void initPresenter() {
        if (this instanceof BaseIView &&
                this.getClass().getGenericSuperclass() instanceof ParameterizedType &&
                ((ParameterizedType) (this.getClass().getGenericSuperclass())).getActualTypeArguments().length > 0) {

            mPresenter = TUtil.getT(this, 0);
            mMode = TUtil.getT(this, 1);
            if (mPresenter != null) {
                mPresenter.setMV(mMode, this);
            }
        }
    }

    /**
     * 是否使用 {@link EventBus},默认为使用(false)，
     *
     */
    public boolean useEventBus() {
        return false;
    }

    /**
     * 初始化按钮监听
     */
    protected abstract void initListener();

    @Override
    public Context getCt() {
        return this;
    }

}

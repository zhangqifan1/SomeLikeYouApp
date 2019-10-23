package com.as.base_lib_sly.base.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.as.base_lib_sly.base.ui.TUtil;
import com.as.base_lib_sly.base.ui.mvp.BaseIModel;
import com.as.base_lib_sly.base.ui.mvp.BaseIView;
import com.as.base_lib_sly.base.ui.mvp.BasePresenter;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.ParameterizedType;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import me.yokeyword.fragmentation.SupportFragment;


/**
 * -----------------------------
 * Created by zqf on 2018/1/22.
 * ---------------------------
 */

public abstract class BaseMvpFragment<P extends BasePresenter, M extends BaseIModel, B extends ViewDataBinding> extends SupportFragment implements BaseIView {
    protected String TAG = getClass().getSimpleName();
    public LayoutInflater inflater;
    protected M mMode;
    protected P mPresenter;
    protected B mViewBinding;
    public View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        if (rootView == null) {
            mViewBinding = DataBindingUtil.inflate(inflater, this.getLayoutId(), container, false);
            rootView = mViewBinding.getRoot();
            initView(rootView);
            initPresenter();
            initData();
            initListener();
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }



    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
    }


    @Override
    public void onStart() {
        super.onStart();
        if(isAdded()) {
            if (!EventBus.getDefault().isRegistered(this)&&useEventBus()){
                //如果要使用 Eventbus 请将此方法返回 true
                EventBus.getDefault().register(this);//注册 Eventbus
            }
        }

    }

    @Override
    public void onDestroy() {
        if(mPresenter!=null){
            mPresenter.onDestroy();
        }
        if (EventBus.getDefault().isRegistered(this) && useEventBus()) {
            EventBus.getDefault().unregister(this);
//            EventBus.getDefault().removeAllStickyEvents();
        }
        super.onDestroy();
    }

    /**
     * 获取资源文件布局
     *
     * @return 资源布局文件layout
     */
    protected abstract int getLayoutId();

    /**
     * 初始化view
     *
     * @param rootView 资源布局view
     */
    public abstract void initView(View rootView);

    /**
     * 初始化对象
     */
    protected abstract void initData();

    /**
     * 简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通
     */
    public void initPresenter() {
        if (this instanceof BaseIView && this.getClass().getGenericSuperclass() instanceof ParameterizedType &&
                ((ParameterizedType) (this.getClass().getGenericSuperclass())).getActualTypeArguments().length > 0) {
            mMode = TUtil.getT(this, 1);
            mPresenter = TUtil.getT(this, 0);

            if (mPresenter != null) mPresenter.setMV(mMode,this);
        }
    }

    /**
     * 初始化按钮监听
     */
    protected abstract void initListener();

    /**
     * 是否使用 {@link EventBus},默认为使用(false)，
     *
     */
    public boolean useEventBus() {
        return false;
    }

    public void startActivity(Class clazz) {
        startActivity(new Intent(this.getActivity(),clazz));
    }


    @Override
    public Context getCt() {
        return getActivity();
    }
}
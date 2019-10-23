package com.as.somelikeyouapp;

import android.os.Bundle;

import com.as.base_lib_sly.base.ui.activity.BaseActivity;
import com.as.somelikeyouapp.databinding.ActivityTabBinding;

public class TabActivity extends BaseActivity<ActivityTabBinding> {


    private String[] titles = new String[]{"标题0", "标题1", "标题2", "标题3"};

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tab;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }
}

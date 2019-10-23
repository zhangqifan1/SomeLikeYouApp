package com.as.base_lib_sly.base.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.as.base_lib_sly.R;
import com.jaeger.library.StatusBarUtil;

/**
 * -----------------------------
 * Created by zqf on 2019/5/29.
 * ---------------------------
 */
public class BaseStatuBarActivity extends MySupportActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int statuBarColor = setStatuBarColor();
        int color = getResources().getColor(statuBarColor);
        StatusBarUtil.setColor(this, color, 0);
        StatusBarUtil.setLightMode(this);
    }

    /**
     * 设置状态栏颜色 这里设置默认
     * @return
     */
    private int setStatuBarColor() {
        return R.color.paleturquoise;
    }

}

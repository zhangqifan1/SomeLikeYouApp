package com.as.base_lib_sly.base.ui.mvp;

import android.content.Context;

/**
 * -----------------------------
 * Created by zqf on 2018/1/22.
 * ---------------------------
 */

public interface BaseIView {
    Context getCt();
    void showLoading();
    void showError();
    void showSuccess();

}

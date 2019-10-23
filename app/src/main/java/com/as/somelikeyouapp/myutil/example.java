package com.as.somelikeyouapp.myutil;


import android.os.Build;

import com.blankj.utilcode.util.ToastUtils;


/**
 * -----------------------------
 * Created by zqf on 2018/12/26.
 * ---------------------------
 */
public class example {
    public void exampleUse(){
        boolean isFirstUse = SecuritySharedPreference.getInstance().getBoolean("isFirstUse", false);
        if(!isFirstUse){
            ToastUtils.showShort("第一次登陆");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                SecuritySharedPreference.getInstance().edit().putBoolean("isFirstUse",true).apply();
            }
        }else{
            ToastUtils.showShort("第N次登陆");
        }
    }
}

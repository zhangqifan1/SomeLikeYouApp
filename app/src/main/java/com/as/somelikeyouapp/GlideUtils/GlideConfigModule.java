package com.as.somelikeyouapp.GlideUtils;

import android.content.Context;

import com.as.somelikeyouapp.GlideUtils.progress.OkHttpGlideUrlLoader;
import com.as.somelikeyouapp.GlideUtils.progress.ProgressInterceptor;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

import okhttp3.OkHttpClient;

@GlideModule
public class GlideConfigModule extends AppGlideModule {
    //FIXME: 该配置类需要在AndroidManifest.xml中声明3.0
    // <meta-data
    //    android:name="lib.self.network.image.glide.GlideModule.GlideConfigModule"
    //    android:value="GlideModule" />

//    @Override
//    public void applyOptions(Context context, GlideBuilder builder) {
//        //自定义Glide默认的Bitmap格式(加载图片的质量)
////        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
//
//        //自定义缓存目录
//        builder.setDiskCache(new InternalCacheDiskCacheFactory(context,
//                GlideConfig.GLIDE_CARCH_DIR, //缓存路径
//                GlideConfig.GLIDE_CATCH_SIZE)); //缓存大小
//    }


    @Override
    public void registerComponents( Context context,  Glide glide,Registry registry) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new ProgressInterceptor());
        OkHttpClient okHttpClient = builder.build();

        registry.replace(GlideUrl.class, InputStream.class,
                new OkHttpGlideUrlLoader.Factory(okHttpClient));
    }
}
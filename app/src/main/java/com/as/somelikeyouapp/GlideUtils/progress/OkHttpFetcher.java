package com.as.somelikeyouapp.GlideUtils.progress;


import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.util.ContentLengthInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class OkHttpFetcher implements DataFetcher<InputStream> {

    private final OkHttpClient mOkHttpClient;
    private final GlideUrl mGlideUrl;
    private InputStream mInputStream;
    private ResponseBody mResponseBody;
    private volatile boolean mCancelled;

    public OkHttpFetcher(OkHttpClient okHttpClient, GlideUrl glideUrl) {
        mOkHttpClient = okHttpClient;
        mGlideUrl = glideUrl;
    }

    @Override
    public void loadData( Priority priority,  DataCallback<? super InputStream> callback) {

        try {
            InputStream inputStream = loadDataWithRedirects();
            callback.onDataReady(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            callback.onLoadFailed(e);
        }
    }

    private InputStream loadDataWithRedirects() throws IOException {

        Request.Builder builder = new Request.Builder().url(mGlideUrl.toStringUrl());

        for (Map.Entry<String, String> headerEntry : mGlideUrl.getHeaders().entrySet()) {

            String key = headerEntry.getKey();
            builder.addHeader(key, headerEntry.getValue());
        }

        Request request = builder.build();

        if (mCancelled) {
            return null;
        }

        Response response = mOkHttpClient.newCall(request).execute();
        mResponseBody = response.body();

        if (!response.isSuccessful() || mResponseBody == null) {
            throw new IOException("Request failed with code: " + response.code());
        }

        mInputStream = ContentLengthInputStream.obtain(
                mResponseBody.byteStream(), mResponseBody.contentLength());

        return mInputStream;
    }

    @Override
    public void cleanup() {

        try {
            if (mInputStream != null) {
                mInputStream.close();
            }
            if (mResponseBody != null) {
                mResponseBody.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancel() {

        mCancelled = true;
    }


    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }


    @Override
    public DataSource getDataSource() {
        return DataSource.REMOTE;
    }
}

package com.jackie.sample.request;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Created by Administrator on 2016/12/16.
 */

public class CountingRequestBody extends RequestBody {
    private RequestBody mDelegate;
    private OnCountingListener mOnCountingListener;

    private CountingSink mCountingSink;

    public CountingRequestBody(RequestBody delegate, OnCountingListener onCountingListener) {
        this.mDelegate = delegate;
        this.mOnCountingListener = onCountingListener;
    }

    @Override
    public MediaType contentType() {
        return mDelegate.contentType();
    }

    @Override
    public long contentLength() {
        try {
            return mDelegate.contentLength();
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        mCountingSink = new CountingSink(sink);

        BufferedSink bufferedSink = Okio.buffer(mCountingSink);
        mDelegate.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    private class CountingSink extends ForwardingSink {
        private long byteWritten;

        public CountingSink(Sink delegate) {
            super(delegate);
        }

        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);

            byteWritten += byteCount;
            if (mOnCountingListener != null) {
                mOnCountingListener.onRequestProgress(byteWritten, contentLength());
            }
        }
    }

    public interface OnCountingListener {
        void onRequestProgress(long byteWritten, long contentLength);
    }
}

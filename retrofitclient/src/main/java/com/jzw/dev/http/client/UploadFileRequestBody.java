package com.jzw.dev.http.client;

import com.jzw.dev.http.callback.FileUploadObserver;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * @company 上海道枢信息科技-->
 * @anthor created by jingzhanwu
 * @date 2018/3/9 0009
 * @change
 * @describe 实现上传进度的主要类，继承OkHTTP的RequestBody,
 * 实现上传进度的提示
 **/
public class UploadFileRequestBody extends RequestBody {
    private RequestBody mRequestBody;
    private FileUploadObserver fileUploadObserver;

    public UploadFileRequestBody(RequestBody requestBody, FileUploadObserver fileUploadObserver) {
        this.mRequestBody = requestBody;
        this.fileUploadObserver = fileUploadObserver;
    }

    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return mRequestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        CountingSink countingSink = new CountingSink(sink);
        BufferedSink bufferedSink = Okio.buffer(countingSink);
        //写入
        mRequestBody.writeTo(bufferedSink);
        //必须调用flush 否则最后一部分数据可能不会被写入
        bufferedSink.flush();
    }

    protected final class CountingSink extends ForwardingSink {

        private long byteWriten = 0;

        public CountingSink(Sink delegate) {
            super(delegate);
        }

        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            byteWriten += byteCount;
            if (fileUploadObserver != null) {
                fileUploadObserver.onProgressChange(byteWriten, contentLength());
            }
        }
    }
}

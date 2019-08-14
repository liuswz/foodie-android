package com.foodie.base.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.foodie.base.base.Constant;


public class OssManager {

    private OSS oss;
    public static OssManager getInstance() {
        return OssInstance.instance;
    }
    private static class OssInstance{
        private static final OssManager instance= new OssManager();
    }
   // private TDialog tDialog;
    private OssManager(){}

    private String bucketName;
    Context context;
    /**
     * 初始化  getApplicationContext()
     * **/
    public OssManager init(Context context){
        if(oss==null){
            OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(Constant.AccessKeyId, Constant.AccessKeySecret);
            oss= new OSSClient(context, Constant.Endpoint, credentialProvider);
        }
        this.bucketName = Constant.BucketName;
        this.context = context;
        return  OssInstance.instance;
    }

    public void uploadFile(String name,String filePath){
        ProgressDialog progressDialog = new ProgressDialog(context);
//        progressDialog.setTitle("加载中");
        progressDialog.setMessage("拼命加载中...");
        progressDialog.show();
        PutObjectRequest put = new PutObjectRequest(bucketName, name, filePath);

// 异步上传时可以设置进度回调。
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });

        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                progressDialog.dismiss();
                Log.d("PutObject", "UploadSuccess");
                Log.d("ETag", result.getETag());
                Log.d("RequestId", result.getRequestId());
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                progressDialog.dismiss();
                // 请求异常。
                if (clientExcepion != null) {
                    // 本地异常，如网络异常等。
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常。
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
    }
}

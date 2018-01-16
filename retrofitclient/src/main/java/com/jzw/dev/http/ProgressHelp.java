package com.jzw.dev.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * @anthor created by jzw
 * @date 2017/11/15 0015
 * @change
 * @describe describe
 **/
public class ProgressHelp {
    private ProgressDialog mProgressDialog;
    private WeakReference<Context> mContext;


    public ProgressHelp(Context context) {
        mContext = new WeakReference<>(context);
    }

    public void showDialog() {
        mProgressDialog = new ProgressDialog(mContext.get(), R.style.jzw_simple_progress);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                Log.d("jzw-okhttp", "ProgressDialog.onCancel");
                dismissDialog();
            }
        });
        mProgressDialog.setMessage("请稍候...");
        mProgressDialog.show();
    }

    public void dismissDialog() {
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            mProgressDialog = null;
        }
        if (mContext != null) {
            mContext.clear();
            mContext = null;
        }
    }
}

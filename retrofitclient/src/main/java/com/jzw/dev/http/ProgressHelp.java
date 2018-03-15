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
    public WeakReference<Context> mContext;

    private static ProgressHelp mInstance;

    private ProgressHelp() {
    }

    public static ProgressHelp get() {
        if (mInstance == null) {
            synchronized (ProgressHelp.class) {
                if (mInstance == null) {
                    mInstance = new ProgressHelp();
                }
            }
        }
        return mInstance;
    }

    public void showDialog(Context context) {
        mContext = new WeakReference<>(context);
        if (!isShowing()) {
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
    }

    public boolean isShowing() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            return true;
        }
        return false;
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

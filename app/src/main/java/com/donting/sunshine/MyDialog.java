package com.donting.sunshine;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;

/**
 * Created by yanhuiwang on 16/10/16.
 */

public class MyDialog extends Dialog {
    public MyDialog(Context context) {
        super(context);
    }

    public MyDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected MyDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}

package com.donting.sunshine;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by yanhuiwang on 16/10/5.
 */

public class MyAccessibilityService extends AccessibilityService {
    private static final String TAG = MyAccessibilityService.class.getSimpleName();

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG, "package name" + event.getPackageName());
        Log.d(TAG, "event type" + event.getEventType());
        Log.d(TAG, "event time" + event.getEventTime());
        Log.d(TAG, "content description" + event.getContentDescription());
        Log.d(TAG, "text" + event.getText());
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        AccessibilityServiceInfo info = getServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.packageNames = new String[] {"com.donting.sunshine"};
        info.notificationTimeout = 100;
        setServiceInfo(info);
    }
}

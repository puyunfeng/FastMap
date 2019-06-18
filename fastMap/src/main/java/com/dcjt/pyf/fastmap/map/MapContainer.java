package com.dcjt.pyf.fastmap.map;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by halo on 2017/8/8.
 */

public class MapContainer extends LinearLayout {
    private NestedScrollView scrollView;
    private View view = null;
    private int x;
    private int y;

    public MapContainer(Context context) {
        super(context);
    }

    public void setScrollView(NestedScrollView scrollView) {
        this.scrollView = scrollView;
    }

    public MapContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MapContainer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MapContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            if(scrollView!=null){
                scrollView.requestDisallowInterceptTouchEvent(false);
            }
            if (mIReleaseFinger != null) {
                mIReleaseFinger.showAddress();
            }

        } else {
            if(scrollView!=null){
                scrollView.requestDisallowInterceptTouchEvent(true);
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getXPos() {
        return x;
    }

    public int getYPos() {
        return y;
    }

    public static interface IReleaseFinger {
        void showAddress();
    }

    public IReleaseFinger mIReleaseFinger = null;

    public void setIReleaseFinger(IReleaseFinger releaseFinger) {
        this.mIReleaseFinger = releaseFinger;
    }

}

package com.cherubinxxx.cnbetaxreader.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * Created by Administrator on 2015/3/5.
 */
public class FabSign extends TextView{
    private static final int TRANSLATE_DURATION_MILLIS = 200;
    private boolean mVisible;;
    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

    public FabSign(Context context) {
        this(context, null);
    }

    public FabSign(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FabSign(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        mVisible = true;
    }

    public boolean isVisible() {
        return mVisible;
    }

    public void show() {
        show(true);
    }

    public void hide() {
        hide(true);
    }

    public void show(boolean animate) {
        toggle(true, animate);
    }

    public void hide(boolean animate) {
        toggle(false, animate);
    }

    private void toggle(final boolean visible, final boolean animate) {
        if (mVisible != visible) {
            mVisible = visible;
            int height = getHeight();
            if (height == 0) {
                ViewTreeObserver vto = getViewTreeObserver();
                if (vto.isAlive()) {
                    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            ViewTreeObserver currentVto = getViewTreeObserver();
                            if (currentVto.isAlive()) {
                                currentVto.removeOnPreDrawListener(this);
                            }
                            toggle(visible, animate);
                            return true;
                        }
                    });
                    return;
                }
            }
            int translationY = visible ? 0 : height + getBottom();
            if (animate) {
                ViewPropertyAnimator.animate(this).setInterpolator(mInterpolator)
                        .setDuration(TRANSLATE_DURATION_MILLIS)
                        .translationY(translationY);
            } else {
                ViewHelper.setTranslationY(this, translationY);
            }

            // On pre-Honeycomb a translated view is still clickable, so we need to disable clicks manually
            /*if (!hasHoneycombApi()) {
                setClickable(visible);
            }*/
        }
    }
}

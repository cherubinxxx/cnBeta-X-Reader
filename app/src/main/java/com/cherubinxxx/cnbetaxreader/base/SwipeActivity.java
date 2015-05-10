package com.cherubinxxx.cnbetaxreader.base;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.cherubinxxx.cnbetaxreader.UI.CommentActivity;
import com.cherubinxxx.cnbetaxreader.R;
import com.cherubinxxx.cnbetaxreader.view.FabSign;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

//import com.cherubinxxx.cnbetaxreader.CommentActivity;

/**
 * Created by Administrator on 2015/4/2.
 */
public class SwipeActivity extends ActionBarActivity {
    private SwipeLayout swipeLayout;
    private int layerColor = Color.parseColor("#88000000");
    protected boolean swipeAnyWhere = true;//是否可以在页面任意位置右滑关闭页面，如果是false则从左边滑才可以关闭
    private boolean isBothSwipe = false;
    private boolean isWithFab = false;
    private FloatingActionButton fab;
    private FabSign fabSign;
    public SwipeActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        swipeLayout = new SwipeLayout(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        swipeLayout.replaceLayer(this);
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager manager = (WindowManager) context
                .getSystemService(WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    private boolean swipeFinished = false;

    @Override
    public void finish() {
        if (swipeFinished) {
            super.finish();
        } else {
            swipeLayout.cancelPotentialAnimation();
            super.finish();
            overridePendingTransition(0, R.anim.slide_out_right);
        }
    }

    class SwipeLayout extends FrameLayout {

        private View backgroundLayer;
        private View toolbarLayer;

        public SwipeLayout(Context context) {
            super(context);
        }

        public SwipeLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public void replaceLayer(Activity activity) {
            touchSlop = (int) (touchSlopDP * activity.getResources().getDisplayMetrics().density);
            sideWidth = (int) (sideWidthInDP * activity.getResources().getDisplayMetrics().density);
            mActivity = activity;
            screenWidth = getScreenWidth(activity);
            setClickable(true);

            // 覆盖层
            backgroundLayer = new View(activity);
            backgroundLayer.setBackgroundColor(layerColor);

            // 模拟Toolbar
            toolbarLayer = new View(mActivity);
            toolbarLayer.setBackgroundColor(getResources().getColor(R.color.theme_primary));
            toolbarLayer.setVisibility(INVISIBLE);
            toolbarLayer.setLayoutParams(new ViewGroup.LayoutParams(100,100));

            final ViewGroup root = (ViewGroup) activity.getWindow().getDecorView();
            content = root.getChildAt(0);

            //在Android5.0上，content的高度不再是屏幕高度，而是变成了Activity高度，比屏幕高度低一些，
            //如果this.addView(content),就会使用以前的params，这样content会像root一样比content高出一部分，导致底部空出一部分
            //在装有Android 5.0的Nexus5上，root,SwipeLayout和content的高度分别是1920、1776、1632，144的等差数列……
            //在装有Android4.4.3的HTC One M7上，root,SwipeLayout和content的高度分别相同，都是1920
            //所以我们要做的就是给content一个新的LayoutParams，Match_Parent那种，也就是下面的-1

            // 获取ActionBar高度
            /*TypedValue typedValue = new TypedValue();
            int[] textSizeAttr = new int[]{R.attr.actionBarSize};
            int indexOfAttrTextSize = 0;
            TypedArray a = obtainStyledAttributes(typedValue.data, textSizeAttr);
            int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);*/

            // 获取StatusBar高度
            /*activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getHeight();
            Rect frame = new Rect();
            root.getWindowVisibleDisplayFrame(frame);
            int statusBarHeight = frame.top;*/

            ViewGroup.LayoutParams params = content.getLayoutParams();
            ViewGroup.LayoutParams params2 = new ViewGroup.LayoutParams(-1, -1);
            //ViewGroup.LayoutParams params3 = new ViewGroup.LayoutParams(-1, actionBarSize + getResources().getDimensionPixelOffset(R.dimen.status_bar_margin));
            ViewGroup.LayoutParams params4 = new ViewGroup.LayoutParams(-1, -1);
            root.removeView(content);
            this.addView(backgroundLayer, params4);
            //this.addView(toolbarLayer, params3);
            this.addView(content, params2);
            root.addView(this, params);
        }

        boolean canSwipe = false;
        View content;
        Activity mActivity;
        int sideWidthInDP = 20;
        int sideWidth = 72;
        int screenWidth = 1080;
        VelocityTracker tracker;

        float downX;
        float downY;
        float lastX;
        float currentX;
        float currentY;


        int touchSlopDP = 30;
        int touchSlop = 60;

        /**
         * Layout拦截触屏事件，监听垂直滚动
         *
         * @param ev
         */
        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            // 全屏滑动
            if (swipeAnyWhere) {
                switch (ev.getAction()) {
                    // 按下屏幕
                    case MotionEvent.ACTION_DOWN:
                        downX = ev.getX();
                        downY = ev.getY();
                        currentX = downX;
                        currentY = downY;
                        lastX = downX;
                        break;
                    // 滑动屏幕
                    case MotionEvent.ACTION_MOVE:
                        float dx = ev.getX() - downX;
                        float dy = ev.getY() - downY;
                        if ((dy == 0f || Math.abs(dx / dy) > 1) && (dx * dx + dy * dy > touchSlop * touchSlop)) {
                            downX = ev.getX();
                            downY = ev.getY();
                            currentX = downX;
                            currentY = downY;
                            lastX = downX;
                            canSwipe = true;
                            tracker = VelocityTracker.obtain();
                            return true;
                        } else if (dy > 0){
                            if (isWithFab){
                                fab.show();
                                fabSign.show();
                            }
                        } else {
                            if (isWithFab){
                                fab.hide();
                                fabSign.hide();
                            }
                        }
                        break;
                }
            // 边缘滑动
            } else if (ev.getAction() == MotionEvent.ACTION_DOWN && ev.getX() < sideWidth) {
                canSwipe = true;
                tracker = VelocityTracker.obtain();
                return true;
            }
            return super.onInterceptTouchEvent(ev);
        }

        /**
         * Layout触屏事件，监听水平滚动
         *
         * @param event
         */
        @Override
        public boolean onTouchEvent(@NonNull MotionEvent event) {
            if (canSwipe) {
                tracker.addMovement(event);
                int action = event.getAction();
                switch (action) {
                    // 按下屏幕
                    case MotionEvent.ACTION_DOWN:
                        downX = event.getX();
                        downY = event.getY();
                        currentX = downX;
                        currentY = downY;
                        lastX = downX;
                        break;
                    // 滑动屏幕
                    case MotionEvent.ACTION_MOVE:
                        currentX = event.getX();
                        currentY = event.getY();
                        float dx = currentX - lastX;
                        if (content.getX() < 0) {
                        layerColor = Color.parseColor("#ffffffff");
                        backgroundLayer.setBackgroundColor(layerColor);
                            toolbarLayer.setVisibility(VISIBLE);
                        } else {
                        layerColor = Color.parseColor("#80000000");
                        backgroundLayer.setBackgroundColor(layerColor);
                            toolbarLayer.setVisibility(INVISIBLE);
                        }
                        if (content.getX() + dx < 0 && !isBothSwipe) {
                            setContentX(0);
                        } else {
                            setContentX(content.getX() + dx);
                        }
                        lastX = currentX;
                        break;
                    // 松开屏幕
                    case MotionEvent.ACTION_UP:
                    // 后续事件
                    case MotionEvent.ACTION_CANCEL:
                        tracker.computeCurrentVelocity(10000);
                        tracker.computeCurrentVelocity(1000, 20000);
                        canSwipe = false;
                        int mv = screenWidth / 200 * 1000;
                        // 滑动范围，1/4屏
                        if (content.getX() > screenWidth / 4) {
                            animateFinish(false);
                        } else if (content.getX() < - screenWidth / 4) {

                            swipeLeft(false);
                        } else {
                            animateBack(false);
                        }
                        tracker.recycle();
                        break;
                    default:
                        break;
                }
            }
            return super.onTouchEvent(event);
        }

        AnimatorSet animator;

        public void cancelPotentialAnimation() {
            if (animator != null) {
                animator.removeAllListeners();
                animator.cancel();
            }
        }

        /**
         * 重设X轴
         *
         * @param x
         */
        private void setContentX(float x) {
            content.setX(x);
            if (backgroundLayer != null) {
                backgroundLayer.setAlpha(1 - x / getWidth());
            }
        }


        /**
         * 滑动弹回，left是0，setX和setTranslationX效果一样
         *
         * @param withVel
         */
        private void animateBack(boolean withVel) {
            cancelPotentialAnimation();
            animator = new AnimatorSet();
            ObjectAnimator animatorX = ObjectAnimator.ofFloat(content, "x", content.getX(), 0);
            ObjectAnimator animatorA = ObjectAnimator.ofFloat(backgroundLayer, "alpha", backgroundLayer.getAlpha(), 1);
            ArrayList<Animator> animators = new ArrayList<>();
            animators.add(animatorX);
            animators.add(animatorA);
            if (withVel) {
                animator.setDuration((long) (duration * content.getX() / screenWidth));
            } else {
                animator.setDuration(duration);
            }
            animator.playTogether(animators);
            animator.start();
        }

        /**
         * 右滑事件
         *
         * @param withVel
         */
        private void animateFinish(boolean withVel) {
            cancelPotentialAnimation();
            animator = new AnimatorSet();

            ObjectAnimator animatorX = ObjectAnimator.ofFloat(content, "x", content.getX(), screenWidth);
            ObjectAnimator animatorA = ObjectAnimator.ofFloat(backgroundLayer, "alpha", backgroundLayer.getAlpha(), 0);
            ArrayList<Animator> animators = new ArrayList<>();
            animators.add(animatorX);
            animators.add(animatorA);
            if (withVel) {
                animator.setDuration((long) (duration * (screenWidth - content.getX()) / screenWidth));
            } else {
                animator.setDuration(duration);
            }
            animator.playTogether(animators);

            animator.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (!mActivity.isFinishing()) {
                        swipeFinished = true;
                        mActivity.finish();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }
            });
            animator.start();
        }

        /**
         * 左滑事件
         *
         * @param withVel
         */
        private void swipeLeft(boolean withVel) {
            cancelPotentialAnimation();
            animator = new AnimatorSet();
            ObjectAnimator animatorX = ObjectAnimator.ofFloat(content, "x", content.getX(), -screenWidth);
            //ObjectAnimator animatorA = ObjectAnimator.ofFloat(backgroundLayer, "alpha", backgroundLayer.getAlpha(), 0);
            ArrayList<Animator> animators = new ArrayList<>();
            animators.add(animatorX);
            //animators.add(animatorA);
            if (withVel) {
                animator.setDuration((long) (duration * (screenWidth + content.getX()) / screenWidth));
            } else {
                animator.setDuration(duration);
            }
            animator.playTogether(animators);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }
                @Override
                public void onAnimationRepeat(Animator animation) {
                }
                @Override
                public void onAnimationEnd(Animator animation) {
                    Bundle data = new Bundle();
                    data.putString("sid", mActivity.getIntent().getStringExtra("sid"));
                    data.putString("isSwipetoNext", "true");
                    Intent intent = new Intent (mActivity, CommentActivity.class);
                    intent.putExtras(data);
                    mActivity.startActivity(intent);
                    mActivity.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    swipeFinished = true;
                    /*Handler handler = new Handler(){
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            // 新建线程还原上一层
                            content.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        wait(1000);
                                        setContentX(0);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                        }
                    };*/
                    //handler.sendEmptyMessage(0);


                    //ObjectAnimator.ofFloat(content, "x", content.getX(), 0);
                    //mActivity.finish();
                /*if (!mActivity.isFinishing()) {
                    swipeFinished = true;
                    mActivity.finish();
                }*/
                }
                @Override
                public void onAnimationCancel(Animator animation) {
                }
            });
            animator.start();
        }

        private int duration = 200;

        private void animateFromVelocity(float v) {
            if (v > 0) {
                if (content.getX() < screenWidth / 2
                        && v * duration / 1000 + content.getX() < screenWidth) {
                    animateBack(false);
                } else {
                    animateFinish(true);
                }
            } else {
                if (content.getX() > screenWidth / 2
                        && v * duration / 1000 + content.getX() > screenWidth / 2) {
                    animateFinish(false);
                } else {
                    animateBack(true);
                }
            }

        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }
    }

    /**
     * 设置Fab
     *
     * @param fab
     * @param fabSign
     */
    public void setWithFab(FloatingActionButton fab,FabSign fabSign){
        this.fab = fab;
        this.fabSign = fabSign;
        isWithFab = true;
    }

    /**
     * 设置是否可向两边滑动
     *
     * @param isBothSwipe
     */
    public void setBothSwipe(boolean isBothSwipe){
        this.isBothSwipe = isBothSwipe;
    }

}

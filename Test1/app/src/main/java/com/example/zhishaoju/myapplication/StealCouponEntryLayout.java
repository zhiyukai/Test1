/*
package com.example.zhishaoju.myapplication;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.sankuai.meituan.mtimageloader.config.SingleConfig;
import com.sankuai.meituan.takeoutnew.net.util.ResponseUtil;
import com.sankuai.waimai.ceres.util.DensityUtil;
import com.sankuai.waimai.platform.capacity.imageloader.ImageLoaderSingleton;
import com.sankuai.waimai.platform.capacity.log.L;
import com.sankuai.waimai.platform.utils.MathUtils;

*/
/**
 * Created by jzj on 16/7/26.
 *//*

public class StealCouponEntryLayout extends FrameLayout {

    private static final int MSG_COLLAPSE = 1;

    private static final int COLLAPSE_DELAY_MS = 5000;

    private static final int STATE_HIDDEN = 0;
    private static final int STATE_COLLAPSED = 1;
    private static final int STATE_DOCKED = 2;

    private static final int STATE_DRAG = 4;
    private static final int STATE_COLLAPSING = 5; // onScroll: Docked --> Collapsed, onTouch(ACTION_UP / ACTION_CANCEL): Drag --> Collapsed
    private static final int STATE_EXPANDING = 6; // onClick: Collapsed --> Docked
    private static final int STATE_DOCKING = 7;

    @IntDef({STATE_HIDDEN, STATE_COLLAPSED, STATE_DOCKED,
            STATE_DRAG, STATE_COLLAPSING, STATE_EXPANDING, STATE_DOCKING})
    private @interface State {

    }

    private final Rect mBounds = new Rect(); // 完全可见时的x,y限制

    private View mContentView;
    private OnClickListener mListener;
    private ImageView mImg;

    private final Handler mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == MSG_COLLAPSE) {
                log("timing finish");
                collapse();
                return true;
            }
            return false;
        }
    });

    private PositionAnimation mAnimation;

    private final int mTouchSlop;

    private int mX = 0;
    private int mY = 0;

    private int mWidth;
    private int mHeight;

    private int mMarginSide = 0;
    private int mMarginBottom = 0;

    private int mCollapseWeight = 0;

    private boolean mFlagInitial = false;

    @State
    private int mState = STATE_HIDDEN;

    public StealCouponEntryLayout(Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public StealCouponEntryLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public StealCouponEntryLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public StealCouponEntryLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentView = LayoutInflater.from(getContext()).inflate(R.layout.takeout_view_steal_coupon_entry, this, false);
        mContentView.setOnTouchListener(new ClsOnTouchListener());
        mImg = (ImageView) mContentView.findViewById(R.id.img_steal_coupon_entry);
        addView(mContentView);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child == mContentView) {
            super.addView(child, index, params);
        } else {
            if (BuildConfig.DEBUG) {
                throw new IllegalAccessError("method addView() not supported!");
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = mContentView.getMeasuredWidth();
        mHeight = mContentView.getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mBounds.left = 0;
        mBounds.top = 0;
        mBounds.right = right - left - mWidth;
        if (isMarginSideValid()) {
            mBounds.left += mMarginSide;
            mBounds.right -= mMarginSide;
        }
        mBounds.bottom = bottom - top - mHeight;
        if (mFlagInitial) {
            mFlagInitial = false;
            mX = mBounds.right;
            mY = (mBounds.top + mBounds.bottom) / 2;
            if (isMarginBottomValid()) {
                mY = mBounds.bottom - mMarginBottom - mHeight;
            }
        }
        mContentView.layout(mX, mY, mX + mWidth, mY + mHeight);
    }

    public void setOnEntryClickListener(OnClickListener listener) {
        mListener = listener;
    }

    public void show(String url) {
        show(url, 0);
    }

    public void show(String url, @DrawableRes int placeHolderResId) {
        if (mState == STATE_HIDDEN) {
            ImageLoaderSingleton.getInstance()
                    .with(getContext())
                    .url(url)
                    .placeHolder(placeHolderResId)
                    .asBitmap(new SingleConfig.BitmapListener() {
                        @Override
                        public void onSuccess(Bitmap bitmap) {
                            mImg.setImageBitmap(bitmap);
                            showInitial();
                        }

                        @Override
                        public void onFail() {
                            ResponseUtil.showErrorToast(getContext(), null);
                        }
                    });
        }
    }

    public void dismiss() {
        cancelTiming();
        cancelAnimation();
        mContentView.setVisibility(GONE);
        setState(STATE_HIDDEN);
    }

    public void onScrollStart() {
        collapse();
        mContentView.setAlpha(0.5f);
    }

    public void onScrollStop() {
        mContentView.setAlpha(1f);
    }

    */
/**
     * 设置两侧的间距，如果不设置，则默认为0
     * @param marginSideDp 两侧的间距，单位dp
     *//*

    public void setMarginSideByDp(int marginSideDp) {
        mMarginSide = DensityUtil.dip2px(getContext(), marginSideDp);
    }

    */
/**
     * 设置距离底部的间距，如果不设置，则默认为可见部分的一半
     * @param marginBottomDp 距离底部的间距，单位dp
     *//*

    public void setMarginBottomByDp(int marginBottomDp) {
        mMarginBottom = DensityUtil.dip2px(getContext(), marginBottomDp);
    }

    */
/**
     * 设置收起的比例，不设置默认为2
     * @param weight 收起遮住的宽度为整个图片宽度的(1/weight). 如: weight==3时,收起1/3; weight==2时,收起1/2
     *//*

    public void setCollapseWeight(int weight) {
        mCollapseWeight = weight;
    }

    */
/**
     * 收起到边缘，只展示一部分
     *//*

    private void collapse() {
        log("collapse, state = " + mState);
        if (mState != STATE_DOCKED && mState != STATE_DRAG) {
            return;
        }
        log("collapse start");
        cancelTiming();
        cancelAnimation();
        final int x = getContentX();
        int collapseWeight = 2;
        if (isCollapseWeightValid()) {
            collapseWeight = mCollapseWeight;
        }
        int delta = -(mWidth / collapseWeight);
        if (isMarginSideValid()) {
            delta -= mMarginSide;
        }
        mAnimation = new PositionAnimation(x, calcX(x, delta), STATE_COLLAPSING, STATE_COLLAPSED);
        mAnimation.start();
    }

    */
/**
     * 停靠在边缘
     *//*

    private void dock() {
        log("dock, state = " + mState);
        if (mState != STATE_DRAG) {
            return;
        }
        log("dock start");
        cancelTiming();
        cancelAnimation();
        final int x = getContentX();
        mAnimation = new PositionAnimation(x, calcX(x, 0), STATE_DOCKING, STATE_DOCKED);
        mAnimation.start();
    }

    */
/**
     * 展开到边缘
     *//*

    private void expand() {
        log("expand, state = " + mState);
        if (mState != STATE_COLLAPSED) {
            return;
        }
        log("expand start");
        cancelTiming();
        cancelAnimation();
        final int x = getContentX();
        mAnimation = new PositionAnimation(x, calcX(x, 0), STATE_EXPANDING, STATE_DOCKED);
        mAnimation.start();
    }

    private int calcX(int x, int margin) {
        final int centerX = (mBounds.right + mBounds.left) / 2;
        if (x < centerX) {
            return mBounds.left + margin;
        } else {
            return mBounds.right - margin;
        }
    }

    private void startTiming() {
        cancelTiming();
        log("timing start");
        mHandler.sendEmptyMessageDelayed(MSG_COLLAPSE, COLLAPSE_DELAY_MS);
    }

    private void cancelTiming() {
        log("timing cancel");
        mHandler.removeMessages(MSG_COLLAPSE);
    }

    private void showInitial() {
        mFlagInitial = true;
        mContentView.setVisibility(VISIBLE); // 设置后会触发requestLayout
        setState(STATE_DOCKED);
    }

    private void setState(@State int state) {
        mState = state;
        if (mState == STATE_DOCKED) {
            startTiming();
        }
    }

    private int getContentX() {
        return mX;
    }

    private int getContentY() {
        return mY;
    }

    private void locate(int x, int y, boolean withBounds) {
        if (withBounds) {
            x = MathUtils.constrain(x, mBounds.left, mBounds.right);
        }
        y = MathUtils.constrain(y, mBounds.top, mBounds.bottom);
        mX = x;
        mY = y;
        requestLayout();
    }

    private void locate(int x, int y) {
        locate(x, y, false);
    }

    private void locateWithBounds(int x, int y) {
        locate(x, y, true);
    }

    private void cancelAnimation() {
        if (mAnimation != null && mAnimation.isRunning()) {
            mAnimation.cancel();
            mAnimation = null;
        }
    }

    private boolean isHidden() {
        return mState == STATE_HIDDEN;
    }

    private boolean isAnimating() {
        return mState == STATE_COLLAPSING || mState == STATE_EXPANDING || mState == STATE_DOCKING;
    }

    private void log(String s) {
        L.d(this.getClass().getSimpleName(), s);
    }

    private boolean isMarginSideValid() {
        return ((mMarginSide > 0) && (mMarginSide < ((mBounds.right - mBounds.left - mWidth) / 2)));
    }

    private boolean isMarginBottomValid() {
        return ((mMarginBottom > 0) && (mMarginSide < (mBounds.bottom - mBounds.top - mHeight)));
    }

    private boolean isCollapseWeightValid() {
        return (mCollapseWeight > 1);
    }

    private class ClsOnTouchListener implements OnTouchListener {

        private int mStartX;
        private int mStartY;

        private int mStartLocationY;
        private int mStartLocationX;

        private boolean mBounds;

        private void onClick() {
            log("onClick, state = " + mState);
            if (mState == STATE_DOCKED) {
                if (mListener != null) {
                    mListener.onClick(mContentView);
                }
            } else if (mState == STATE_COLLAPSED) {
                expand();
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            log("event = " + event);

            if (isHidden() || isAnimating()) {
                log("onTouch canceled, state = " + mState);
                return false;
            }

            log("onTouch");

            final int x = (int) event.getRawX();
            final int y = (int) event.getRawY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mStartX = x;
                    mStartY = y;
                    mStartLocationX = getContentX();
                    mStartLocationY = getContentY();
                    mBounds = mState == STATE_DOCKED;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (Math.abs(x - mStartX) > mTouchSlop || Math.abs(y - mStartY) > mTouchSlop) {
                        setState(STATE_DRAG);
                    }
                    if (mState == STATE_DRAG) {
                        locate(x - mStartX + mStartLocationX, y - mStartY + mStartLocationY, mBounds);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (mState != STATE_DRAG) {
                        onClick();
                        break;
                    }
                case MotionEvent.ACTION_CANCEL:
                    if (mState == STATE_DRAG) {
                        locate(x - mStartX + mStartLocationX, y - mStartY + mStartLocationY, mBounds);
                        collapse();
                    }
                    break;
            }
            return true;
        }
    }

    private class PositionAnimation extends ValueAnimator
            implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {

        private final int mY;
        @State
        private final int mPlayingState;
        @State
        private final int mFinishState;

        public PositionAnimation(int fromX, int toX, @State int playingState, @State int finishState) {
            super();
            mAnimation = this;
            setIntValues(fromX, toX);
            mY = getContentY();
            setInterpolator(new LinearInterpolator());
            setDuration(200);
            addListener(this);
            addUpdateListener(this);
            mPlayingState = playingState;
            mFinishState = finishState;
        }

        @Override
        public void start() {
            setState(mPlayingState);
            super.start();
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            int current = (int) animation.getAnimatedValue();
            locate(current, mY);
        }

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mAnimation = null;
            setState(mFinishState);
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }
}
*/

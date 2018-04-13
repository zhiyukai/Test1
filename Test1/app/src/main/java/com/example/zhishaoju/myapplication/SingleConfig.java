/*
package com.example.zhishaoju.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.sankuai.meituan.mtimageloader.constants.AnimationMode;
import com.sankuai.meituan.mtimageloader.constants.BitmapPixelFormat;
import com.sankuai.meituan.mtimageloader.utils.BitmapTransformation;
import com.sankuai.meituan.mtimageloader.utils.CdnResizeUtil;

import java.io.File;

import static com.sankuai.meituan.mtimageloader.constants.AnimationMode.NONE;
import static com.sankuai.meituan.mtimageloader.utils.CdnResizeUtil.getViewHeight;
import static com.sankuai.meituan.mtimageloader.utils.CdnResizeUtil.getViewWidth;


*/
/**
 * Created by MuJiaJie on 2017/7/2.
 *//*


public class SingleConfig {
    private Object context;
    private boolean ignoreCertificateVerify; //https是否忽略校验
    private String url;
    private String originalUrl;
    private String filePath; //文件路径

    private int resId;  //资源id
    private boolean async;
    private String contentProvider; //内容提供者
    private ImageView target;
    private final View autoResizeTarget;
    private final int format;
    private final BitmapTransformation[] transformations;

    private int priority;

    private int animationType;
    private int animationId;
    private Animation animation;

    private ViewPropertyAnimation.Animator animator;

    private int placeHolderResId;
    private int errorResId;

    private boolean diskCache;//是否跳过磁盘存储

    private BitmapListener bitmapListener;

    public SingleConfig(ConfigBuilder builder) {
        this.context = builder.context;
        this.url = builder.url;
        this.originalUrl = builder.originalUrl;
        this.filePath = builder.filePath;
        this.resId = builder.resId;
        this.contentProvider = builder.contentProvider;
        this.format = builder.format;
        this.async = builder.async;

        this.ignoreCertificateVerify = builder.ignoreCertificateVerify;

        this.target = builder.target;
        this.autoResizeTarget = builder.autoResizeTarget;

        this.diskCache = builder.diskCache;

        this.animationId = builder.animationId;
        this.animationType = builder.animationType;
        this.animator = builder.animator;
        this.animation = builder.animation;

        this.priority = builder.priority;
        this.placeHolderResId = builder.placeHolderResId;
        this.transformations = builder.transformations;

        this.asBitmap = builder.asBitmap;
        this.bitmapListener = builder.bitmapListener;

        this.errorResId = builder.errorResId;
    }

    public boolean isAsBitmap() {
        return asBitmap;
    }

    private boolean asBitmap;//只获取bitmap

    public Context getContext() {
        if (context instanceof Context) {
            return (Context) context;
        } else if (target != null && target.getContext() != null) {
            return target.getContext();
        } else if (autoResizeTarget != null && autoResizeTarget.getContext() != null) {
            return autoResizeTarget.getContext();
        }
        return GlobalConfig.getContext();
    }

    public FragmentActivity getFragmentActivity() {
        return (context instanceof FragmentActivity) ? (FragmentActivity) context : null;
    }

    public Activity getActivity() {
        return context instanceof Activity ? (Activity) context : null;
    }

    public Fragment getFragment() {
        return context instanceof Fragment ? (Fragment) context : null;
    }

    public android.support.v4.app.Fragment getSupportFragment() {
        return context instanceof android.support.v4.app.Fragment ? (android.support.v4.app.Fragment) context : null;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public boolean getDiskCache() {
        return diskCache;
    }

    public int getErrorResId() {
        return errorResId;
    }

    public String getContentProvider() {
        return contentProvider;
    }

    public int getFormat() {
        return format;
    }

    public String getFilePath() {
        return filePath;
    }

    public boolean isAsync() {
        return async;
    }

    public int getPlaceHolderResId() {
        return placeHolderResId;
    }

    public int getResId() {
        return resId;
    }

    public ImageView getTarget() {
        return target;
    }

    public String getUrl() {
        return url;
    }

    public int getAnimationType() {
        return animationType;
    }

    public int getAnimationId() {
        return animationId;
    }

    public Animation getAnimation() {
        return animation;
    }

    public ViewPropertyAnimation.Animator getAnimator() {
        return animator;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isIgnoreCertificateVerify() {
        return ignoreCertificateVerify;
    }

    public BitmapListener getBitmapListener() {
        return bitmapListener;
    }

    public void setBitmapListener(BitmapListener bitmapListener) {
        this.bitmapListener = bitmapListener;
    }

    private void show() {
        try {
            GlobalConfig.getLoader().request(this);
        } catch (Exception e) {
            if (GlobalConfig.isDebugMode()) {
                throw e;
            } else {
                e.printStackTrace();
            }
        }
    }

    public BitmapTransformation[] getTransformations() {
        return transformations;
    }

    public interface BitmapListener {
        void onSuccess(Bitmap bitmap);

        void onFail();
    }

    public static class ConfigBuilder {
        private Object context;

        private boolean ignoreCertificateVerify = GlobalConfig.isIgnoreCertificateVerify();

        private String url;
        private String originalUrl;
        private String filePath;
        private int resId;
        private String contentProvider;

        private ImageView target;
        private View autoResizeTarget;

        @CdnResizeUtil.Mode
        private int cdnResizeMode = GlobalConfig.getCdnResizeMode();

        private int cdnTargetWidth = 0;
        private int cdnTargetHeight = 0;
        private int cdnQuality = 100;
        private boolean convertToWebp = GlobalConfig.isPreferWebp();

        private boolean asBitmap;
        private BitmapListener bitmapListener;

        private int placeHolderResId;

        private int errorResId;

        private boolean diskCache = true;

        private int priority; //请求优先级

        private int animationId; //动画资源id
        private int animationType = NONE; //动画资源Type
        private Animation animation; //动画资源
        private ViewPropertyAnimation.Animator animator; //动画资源id
        private int format = BitmapPixelFormat.ARGB_8888; // 位图像素格式
        private BitmapTransformation[] transformations;
        private boolean async;
        private static Handler sMainHandler;

        public ConfigBuilder with(Context context) {
            this.context = context;
            return this;
        }

        public ConfigBuilder with(Activity context) {
            this.context = context;
            return this;
        }

        public ConfigBuilder with(FragmentActivity context) {
            this.context = context;
            return this;
        }

        public ConfigBuilder with(Fragment context) {
            this.context = context;
            return this;
        }

        public ConfigBuilder with(android.support.v4.app.Fragment context) {
            this.context = context;
            return this;
        }

        */
/**
         * <p>
         * 使用{@link SingleConfig.ConfigBuilder#into(ImageView)}或者{@link SingleConfig.ConfigBuilder#asBitmap}
         * 都支持变换操作，但是在Glide版实现中{@link SingleConfig.ConfigBuilder#asBitmap}的请求不支持变换结果的缓存，
         * 而{@link SingleConfig.ConfigBuilder#into(ImageView)}会把指定Bitmap的指定Transformation类处理后的结果
         * 缓存起来，不会对同一个资源做重复变换，所以尽量不要再使用{@link SingleConfig.ConfigBuilder#asBitmap}的
         * 同时使用该方法。
         * </p>
         *
         * @param transformations 处理Bitmap内容的实例列表
         *//*

        public ConfigBuilder transform(BitmapTransformation... transformations) {
            this.transformations = transformations;
            return this;
        }

        public ConfigBuilder ignoreCertificateVerify(boolean ignoreCertificateVerify) {
            this.ignoreCertificateVerify = ignoreCertificateVerify;
            return this;
        }

        */
/**
         * 设置网络路径
         *//*

        public ConfigBuilder url(String url) {
            this.originalUrl = url;
            return this;
        }

        public ConfigBuilder autoResize() {
            this.cdnResizeMode = CdnResizeUtil.RESIZE_MODE_AUTO;
            return this;
        }

        public ConfigBuilder autoResize(View sizeTarget) {
            cdnResizeMode = CdnResizeUtil.RESIZE_MODE_AUTO;
            autoResizeTarget = sizeTarget;
            return this;
        }

        public ConfigBuilder cdnResize(int width, int height) {
            cdnResizeMode = CdnResizeUtil.RESIZE_MODE_MANUAL;
            if (width == 0 && height == 0) {
                return this;
            } else if (width == 0) {
                return cdnResizeByHeight(height);
            } else if (height == 0) {
                return cdnResizeByWidth(width);
            }
            cdnTargetWidth = width;
            cdnTargetHeight = height;
            return this;
        }

        public ConfigBuilder cdnResizeByHeight(int height) {
            cdnResizeMode = CdnResizeUtil.RESIZE_MODE_BY_HEIGHT;
            cdnTargetHeight = height;
            return this;
        }

        public ConfigBuilder cdnResizeByWidth(int width) {
            cdnResizeMode = CdnResizeUtil.RESIZE_MODE_BY_WIDTH;
            cdnTargetWidth = width;
            return this;
        }

        */
/**
         * error图
         *//*

        public ConfigBuilder error(int errorResId) {
            this.errorResId = errorResId;
            return this;
        }

        public ConfigBuilder file(String filePath) {
            if (filePath.startsWith("content:")) {
                this.contentProvider = filePath;
                return this;
            }

            if (!new File(filePath).exists()) {
                return this;
            }

            this.filePath = filePath;
            return this;
        }

        public ConfigBuilder res(int resId) {
            this.resId = resId;
            return this;
        }

        public ConfigBuilder content(String contentProvider) {
            this.contentProvider = contentProvider;
            return this;
        }

        public ConfigBuilder async(boolean async) {
            this.async = async;
            return this;
        }

        public ConfigBuilder webp(boolean webp) {
            this.convertToWebp = webp;
            return this;
        }

        private void buildAndShow() {
            if (autoResizeTarget == null) {
                autoResizeTarget = target;
            }
            if (context == null && autoResizeTarget != null) {
                with(autoResizeTarget.getContext());
            }
            Runnable showRunnable = new Runnable() {
                @Override
                public void run() {
                    checkDebugMode();
                    url = CdnResizeUtil.adjustCdnUrl(originalUrl, cdnResizeMode, cdnTargetWidth, cdnTargetHeight, cdnQuality, autoResizeTarget, convertToWebp);
                    new SingleConfig(ConfigBuilder.this).show();
                }
            };
            if (async) {
                getMainHandler().post(showRunnable);
            } else {
                showRunnable.run();
            }
        }

        private static Handler getMainHandler() {
            if (sMainHandler == null) {
                sMainHandler = new Handler(Looper.getMainLooper());
            }
            return sMainHandler;
        }

        private void checkDebugMode() {
            if (!GlobalConfig.isDebugMode()) {
                return;
            }
            if (cdnResizeMode == CdnResizeUtil.RESIZE_MODE_AUTO) {
                if (autoResizeTarget == null) {
                    throw new IllegalArgumentException("使用AUTO_RESIZE却没有指定任何target，请使用autoResizeTarget(View)指定缩放参照对象");
                } else if (getViewHeight(autoResizeTarget) == 0
                        && getViewWidth(autoResizeTarget) == 0) {
                    throw new IllegalArgumentException("使用AUTO_RESIZE但target大小为0，请检查ImageView" +
                            "参数设置或使用cdnResize(width, height)指定缩放尺寸");
                }
            }
        }

        public void into(ImageView targetView) {
            this.target = targetView;
            if (targetView == null) {
                if (GlobalConfig.isDebugMode()) {
                    throw new IllegalArgumentException("targetView不能为null，请检查View实例");
                } else {
                    Log.w("ImageLoader", "targetView为null，忽略图片请求");
                    return;
                }
            }
            buildAndShow();
        }

        public void asBitmap(BitmapListener bitmapListener) {
            this.bitmapListener = bitmapListener;
            this.asBitmap = true;
            buildAndShow();
        }

        */
/**
         * format 支持的值有: {@link com.sankuai.meituan.mtimageloader.constants.BitmapPixelFormat#ARGB_8888}、{@link com.sankuai.meituan.mtimageloader.constants.BitmapPixelFormat#RGB_565}
         *
         * @param format 加载Bitmap时设置像素格式
         *//*

        public void asBitmap(BitmapListener bitmapListener, int format) {
            this.bitmapListener = bitmapListener;
            this.asBitmap = true;
            this.format = format;
            buildAndShow();
        }

        */
/**
         * 占位图
         *//*

        public ConfigBuilder placeHolder(int placeHolderResId) {
            this.placeHolderResId = placeHolderResId;
            return this;
        }

        */
/**
         * 跳过磁盘缓存
         *//*

        public ConfigBuilder diskCache(boolean diskCache) {
            this.diskCache = diskCache;
            return this;
        }

        public ConfigBuilder animate(int animationId) {
            this.animationType = AnimationMode.ANIMATION_ID;
            this.animationId = animationId;
            return this;
        }

        public ConfigBuilder dontAnimate() {
            this.animationType = NONE;
            return this;
        }

        public ConfigBuilder animate(ViewPropertyAnimation.Animator animator) {
            this.animationType = AnimationMode.ANIMATOR;
            this.animator = animator;
            return this;
        }

        public ConfigBuilder animate(Animation animation) {
            this.animationType = AnimationMode.ANIMATION;
            this.animation = animation;
            return this;
        }

        public ConfigBuilder priority(int priority) {
            this.priority = priority;

            return this;
        }

        public ConfigBuilder cdnQuality(int cdnQuality) {
            this.cdnQuality = cdnQuality;
            return this;
        }
    }


}
*/

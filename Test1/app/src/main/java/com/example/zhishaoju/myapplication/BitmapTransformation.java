package com.example.zhishaoju.myapplication;

import android.graphics.Bitmap;
import android.support.annotation.Keep;
import android.widget.ImageView;

/**
 * <p>
 *     实现Bitmap变换的接口类
 *     在Glide版实现中，图片库会根据实现该接口的类名称作为缓存key，所以尽量使用注解 {@link Keep} 防止实现类被混淆
 * </p>
 * Created by shaojielee on 2017/8/23.
 */
@Keep
public interface BitmapTransformation {
    /**
     * <p>
     *     src 由图片库负责回收，该方法内不需要调用{@link Bitmap#recycle()}方法
     * </p>
     * @param src 图片库加载完成后的Bitmap
     * @param outWidth 在使用{@link SingleConfig.ConfigBuilder#into(ImageView)}时可能是ImageView的大小，否则就是Bitmap自身的大小
     * @param outHeight 在使用{@link SingleConfig.ConfigBuilder#into(ImageView)}时可能是ImageView的大小，否则就是Bitmap自身的大小
     * @return 变换处理后的Bitmap
     */
    Bitmap transform(Bitmap src, int outWidth, int outHeight);
}

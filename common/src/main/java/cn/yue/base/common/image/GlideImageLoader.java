package cn.yue.base.common.image;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.bumptech.glide.request.RequestOptions;

import java.security.MessageDigest;

import cn.yue.base.common.R;

/**
 * Description :
 * Created by yue on 2018/11/15
 */
public class GlideImageLoader implements ImageLoader.Loader {

    @Override
    public void loadImage(ImageView imageView, String url) {
        if (imageView == null) {
            return;
        }
        loadImage(imageView, url, false);
    }

    @Override
    public void loadImage(ImageView imageView, String url, boolean fitCenter) {
        if (imageView == null) {
            return;
        }
        Glide.with(imageView.getContext())
                .load(url)
                .apply(fitCenter ? getRequestOptions().fitCenter() : getRequestOptions().centerCrop())
                .into(imageView);
    }

    @Override
    public void loadImage(ImageView imageView, int resId) {
        if (imageView == null) {
            return;
        }
        loadImage(imageView, resId, false);
    }

    @Override
    public void loadImage(ImageView imageView, int resId, boolean fitCenter) {
        if (imageView == null) {
            return;
        }
        Glide.with(imageView.getContext())
                .load(resId)
                .apply(fitCenter ? getRequestOptions().fitCenter() : getRequestOptions().centerCrop())
                .into(imageView);
    }

    @Override
    public void loadImage(ImageView imageView, Drawable drawable) {
        if (imageView == null) {
            return;
        }
        loadImage(imageView, drawable, false);
    }

    @Override
    public void loadImage(ImageView imageView, Drawable drawable, boolean fitCenter) {
        if (imageView == null) {
            return;
        }
        Glide.with(imageView.getContext())
                .load(drawable)
                .apply(fitCenter ? getRequestOptions().fitCenter() : getRequestOptions().centerCrop())
                .into(imageView);
    }

    @Override
    public void loadGif(ImageView imageView, String url) {
        if (imageView == null) {
            return;
        }
        RequestOptions requestOptions = getRequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .dontAnimate();
        Glide.with(imageView.getContext())
                .asGif()
                .load(url)
                .apply(requestOptions)
                .into(imageView);
    }

    @Override
    public void loadRoundImage(ImageView imageView, String url, int radius) {
        if (imageView == null) {
            return;
        }
        Glide.with(imageView.getContext())
                .load(url)
                .apply(getRequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .transform(new GlideRoundTransform(radius))
                        .dontAnimate())
                .into(imageView);
    }


    @Override
    public void loadCircleImage(ImageView imageView, String url) {
        if (imageView == null) {
            return;
        }
        Glide.with(imageView.getContext())
                .load(url)
                .apply(getRequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .transform(new GlideCircleTransform())
                        .dontAnimate())
                .into(imageView);
    }

    private RequestOptions getRequestOptions() {
        return new RequestOptions()
                .placeholder(R.drawable.drawable_default)
                .error(R.drawable.drawable_default);
    }

    final static class GlideRoundTransform extends BitmapTransformation {

        private static float radius = 0f;

        public GlideRoundTransform() {
            this(4);
        }

        public GlideRoundTransform(int dp) {
            super();
            this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            Bitmap bitmap = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight);
            return roundCrop(pool, bitmap);
        }

        private static Bitmap roundCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            canvas.drawRoundRect(rectF, radius, radius, paint);
            return result;
        }

        public String getId() {
            return getClass().getName() + Math.round(radius);
        }

        @Override
        public void updateDiskCacheKey(MessageDigest messageDigest) {

        }

    }

    final static class GlideCircleTransform extends BitmapTransformation {

        private float mBorderWidth;
        private Paint mBorderPaint;

        public GlideCircleTransform() {
            mBorderPaint = new Paint();
        }

        public GlideCircleTransform(int borderWidth, int borderColor) {
            super();
            mBorderWidth = Resources.getSystem().getDisplayMetrics().density * borderWidth;
            mBorderPaint = new Paint();
            mBorderPaint.setDither(true);
            mBorderPaint.setAntiAlias(true);
            mBorderPaint.setColor(borderColor);
            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setStrokeWidth(mBorderWidth);
        }

        @Override
        protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
            Bitmap bitmap = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight);
            return circleCrop(pool, bitmap);
        }

        private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = (int) (Math.min(source.getWidth(), source.getHeight()) - (mBorderWidth / 2));
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);
            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }
            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            if (mBorderPaint != null) {
                float borderRadius = r - mBorderWidth / 2;
                canvas.drawCircle(r, r, borderRadius, mBorderPaint);
            }
            return result;
        }

        @Override
        public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

        }
    }
}

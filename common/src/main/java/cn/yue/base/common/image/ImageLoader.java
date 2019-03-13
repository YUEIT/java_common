package cn.yue.base.common.image;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

/**
 * Description :
 * Created by yue on 2018/11/15
 */

public class ImageLoader {

    private static Loader loader;

    public static Loader getLoader(){
        if(loader == null){
            loader = new GlideImageLoader();
        }
        return loader;
    }

    public  interface Loader{

        void loadImage(ImageView imageView, String url);

        void loadImage(ImageView imageView, String url, boolean fitCenter);

        void loadImage(ImageView imageView, @DrawableRes int resId);

        void loadImage(ImageView imageView, @DrawableRes int resId, boolean fitCenter);

        void loadImage(ImageView imageView, Drawable drawable);

        void loadImage(ImageView imageView, Drawable drawable, boolean fitCenter);

        void loadGif(ImageView imageView, String url);

        void loadRoundImage(ImageView imageView, String url, int radius);

        void loadCircleImage(ImageView imageView, String url);
    }
}
package id.findcat_store.app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SuppressWarnings("FinalStaticMethod")
public class ImageUtils {

    /**
     * Validate image with regular expression
     * http://www.mkyong.com/regular-expressions/how-to-validate-image-file-extension-with-regular-expression/
     *
     * @param image image for validation
     * @return true valid image, false invalid image
     */
    public static boolean validateGif(final String image) {
        String IMAGE_PATTERN_GIF = "([^\\s]+(\\.(?i)(gif))$)";
        Pattern pattern = Pattern.compile(IMAGE_PATTERN_GIF);

        Matcher matcher = pattern.matcher(image);
        return matcher.matches();

    }

    /**
     * Load local gif image into ImageView, scale down as necessary .
     */
    public static final void loadLocalGifImage(@NonNull Context ctx,
                                               View parent,
                                               @IdRes int imageViewResId,
                                               @DrawableRes int imageResId) {
        ImageView imageView = Convert.as(ImageView.class, parent.findViewById(imageViewResId));
        loadLocalGifImage(ctx, imageView, imageResId);
    }

    /**
     * Load local gif image into ImageView, scale down as necessary .
     */
    public static final void loadLocalGifImage(@NonNull Context ctx,
                                               View parent,
                                               @IdRes int imageViewResId,
                                               @DrawableRes int imageResId,
                                               @DrawableRes int placeholderResId) {
        ImageView imageView = Convert.as(ImageView.class, parent.findViewById(imageViewResId));
        loadLocalGifImage(ctx, imageView, imageResId, placeholderResId);
    }

    /**
     * Load gif image from server url into ImageView, scale down as necessary.
     */
    public static final void loadServerGifImage(@NonNull Context ctx,
                                                View parent,
                                                @IdRes int imageViewResId,
                                                @NonNull String url,
                                                @DrawableRes int placeholderResId) {
        ImageView imageView = Convert.as(ImageView.class, parent.findViewById(imageViewResId));
        loadServerGifImage(ctx, imageView, url, placeholderResId);
    }

    /**
     * Load gif image from server url into ImageView, scale down as necessary.
     */
    public static final void loadServerGifImage(@NonNull Context ctx,
                                                View parent,
                                                @IdRes int imageViewResId,
                                                @NonNull String url) {
        ImageView imageView = Convert.as(ImageView.class, parent.findViewById(imageViewResId));
        loadServerGifImage(ctx, imageView, url);
    }

    /**
     * Load local gif image into ImageView, scale down as necessary .
     */
    public static final void loadLocalGifImage(@NonNull Context context,
                                               @Nullable ImageView imageView,
                                               @DrawableRes int imageResId) {
        if (imageView == null)
            return;

        Glide.with(context)
                .load(imageResId)
                .asGif()
                .skipMemoryCache(true)
                .fitCenter()
                .into(imageView);
    }

    /**
     * Load local gif image into ImageView with placeholder
     */
    public static final void loadLocalGifImage(@NonNull Context context,
                                               @Nullable ImageView imageView,
                                               @DrawableRes int imageResId,
                                               @DrawableRes int placeholderResId) {
        if (imageView == null)
            return;

        Glide.with(context)
                .load(imageResId)
                .asGif()
                .placeholder(placeholderResId)
                .skipMemoryCache(true)
                .fitCenter()
                .into(imageView);
    }

    /**
     * Load gif image from server url into ImageView with placeholder.
     */
    public static final void loadServerGifImage(@NonNull Context context,
                                                @Nullable ImageView imageView,
                                                @NonNull String url,
                                                @DrawableRes int placeholderResId) {
        if (imageView == null)
            return;

        Glide.with(context)
                .load(url)
                .asGif()
                .placeholder(placeholderResId)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .fitCenter()
                .into(imageView);
    }

    /**
     * Load gif image from server url into ImageView
     */
    public static final void loadServerGifImage(@NonNull Context context,
                                                @Nullable ImageView imageView,
                                                @NonNull String url) {
        if (imageView == null)
            return;

        Glide.with(context)
                .load(url)
                .asGif()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .fitCenter()
                .into(imageView);
    }

    /**
     * Load image into ImageView, scale down as necessary .
     */
    public static final void loadImage(Context ctx, String url, View parent, int imageViewResId) {
        ImageView imageView = Convert.as(ImageView.class, parent.findViewById(imageViewResId));
        if (imageView != null)
            loadImage(ctx, url, imageView);
    }

    /**
     * Load image into ImageView, scale down as necessary .
     */
    public static final void loadImage(Context ctx, String url, ImageView imageView) {
        if (TextUtils.isEmpty(url))
            return;
        Glide.with(ctx).load(url)
                .skipMemoryCache(true)
                .fitCenter()
                .into(imageView);
        Log.d("Image", url);
    }
    /**
     * Load image into ImageView, scale down as necessary . rotate image to potrate
     */
    public static final void loadImageRotate(Context ctx, String url, ImageView imageView) {
        if (TextUtils.isEmpty(url))
            return;
        Glide.with(ctx).load(url)
            .skipMemoryCache(true)
            .fitCenter()
            .transform(new RotateTransformation(ctx,90f))
            .into(imageView);
        Log.d("Image", url);
    }

    /**
     * Load image into ImageView with default place holder, scale down as necessary .
     */
    public static final void loadImageWithPlaceHolder(Context ctx, String url, View parent,
                                                      int imageViewResId, int placeHolderRes) {
        ImageView imageView = Convert.as(ImageView.class, parent.findViewById(imageViewResId));
        if (imageView != null)
            loadImageWithPlaceHolder(ctx, url, imageView, placeHolderRes);
    }

    /**
     * Load image into ImageView with default place holder, scale down as necessary .
     */
    public static final void loadImageWithPlaceHolder(Context ctx, String url, ImageView imageView, int placeHolderRes) {
        if (TextUtils.isEmpty(url))
            return;
        Glide.with(ctx).load(url)
                .placeholder(placeHolderRes)
                .skipMemoryCache(true)
                .fitCenter()
                .into(imageView);
        Log.d("Image", url);
    }

    /**
     * Load local image into ImageView.
     */
    public static final void loadImageWithPlaceHolder(Context ctx, ImageView imageView, int placeHolderRes) {
        Glide.with(ctx).load("")
                .placeholder(placeHolderRes)
                .skipMemoryCache(true)
                .fitCenter()
                .into(imageView);
    }

    /**
     * Load image into ImageView, no scale down (deprecated, now all image loaded need to be scaled
     * down).
     */
    @Deprecated
    public static final void loadImageNoScale(Context ctx, String url, View parent, int imageViewResId) {
        loadImage(ctx, url, parent, imageViewResId);
    }

    /**
     * Load image into ImageView with place holder, no scale down (deprecated, now all image loaded
     * need to be scaled down).
     */
    @Deprecated
    public static final void loadImageNoScale(
            Context ctx, String url, View parent, int imageViewResId, int placeHolderRes) {
        loadImageWithPlaceHolder(ctx, url, parent, imageViewResId, placeHolderRes);
    }

    /**
     * Load image into ImageView, no scale down. (deprecated, now all image loaded need to be scaled
     * down).
     */
    @Deprecated
    public static final void loadImageNoScale(Context ctx, String url, ImageView imageView) {
        loadImage(ctx, url, imageView);
    }

    /**
     * Load scaled image into ImageView, with placeholder image, and crop it circular.
     */
    public static final void loadImageRound(
            Context ctx, String url, View parent, int imageViewResId, int placeholderImgResId) {

        ImageView imageView = Convert.as(ImageView.class, parent.findViewById(imageViewResId));
        if (imageView != null)
            loadImageRound(ctx, url, imageView, placeholderImgResId);
    }

    /**
     * Load scaled image into ImageView, with placeholder image, and crop it circular.
     */
    public static final void loadImageRound(
            final Context ctx, String url, final ImageView imageView, int placeholderImgResId) {

        if (TextUtils.isEmpty(url))
            return;
        Glide.with(ctx).load(url)
                .asBitmap()
                .skipMemoryCache(true)
                .placeholder(placeholderImgResId)
                .fitCenter()
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(ctx.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
        Log.d("Image", url);
    }

    /**
     * Load scaled image into ImageView, from local File, with placeholder image, and crop it circular.
     */
    public static final void loadImageRoundLocal(
            final Context ctx, File file, View parent, int imageViewResId, int placeholderImgResId) {

        final ImageView imageView = Convert.as(ImageView.class, parent.findViewById(imageViewResId));
        if (imageView == null)
            return;
        Glide.with(ctx).load(file)
                .asBitmap()
                .skipMemoryCache(true)
                .placeholder(placeholderImgResId)
                .fitCenter()
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(ctx.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    /**
     * Load actual sized image into ImageView, and make the corner round.
     */
    public static final void loadImageRoundCornerNoScale(
            final Context ctx, String url, final ImageView imageView) {

        if (TextUtils.isEmpty(url))
            return;

        Glide.with(ctx).load(url).asBitmap().dontTransform().fitCenter()
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable rbd =
                                RoundedBitmapDrawableFactory.create(ctx.getResources(), resource);
                        rbd.setCornerRadius(16.0f);
                        imageView.setImageDrawable(rbd);
                    }
                });
        Log.d("Image", url);
    }


    /**
     * Returns the URI path to the Bitmap displayed in specified ImageView.
     * From https://github.com/codepath/android_guides/wiki/Sharing-Content-with-Intents
     * <p/>
     * WARNING: For Glide, this is not the recommended way to get image. Use Glide ViewTarget instead.
     * See https://github.com/bumptech/glide/issues/1083
     */
    @Deprecated
    public static final Uri getBitmapUriFromImageView(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else if (drawable instanceof GlideBitmapDrawable) {
            bmp = ((GlideBitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File mediaFile = StorageUtils.getOutputMediaFile();
            if (mediaFile == null)
                return null;
            // try compress bmp to jpg and output to media file
            FileOutputStream out = new FileOutputStream(mediaFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(mediaFile);

        } catch (IOException e) {
            Log.printStackTrace(e);
        }
        return bmpUri;
    }

    /**
     * Get image from uri.
     */
    public static final Bitmap getBitmapFromUri(@NonNull Context ctx, @NonNull Uri uri) {
        InputStream is = null;
        BufferedInputStream buffer = null;

        try {
            // use buffered stream to fix "skia decoder->decode returned false",
            // http://stackoverflow.com/questions/2787015/
            is = ctx.getContentResolver().openInputStream(uri);
            if (is == null)
                return null;

            buffer = new BufferedInputStream(is);
            BitmapFactory.Options options = new BitmapFactory.Options();
            return BitmapFactory.decodeStream(buffer, null, options);

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            if (buffer != null)
                try {
                    buffer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

        }
    }

}

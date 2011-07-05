package com.raptureinvenice.webimageview.image;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.raptureinvenice.webimageview.cache.WebImageCache;
import com.raptureinvenice.webimageview.download.WebImageManager;

public class WebImageView extends ImageView {

	public WebImageView(Context context) {
		super(context);
	}

	public WebImageView(Context context, AttributeSet attSet) {
		super(context, attSet);
	}

	public static void setMemoryCachingEnabled(boolean enabled) {
		WebImageCache.setMemoryCachingEnabled(enabled);
	}

	public static void setDiskCachingEnabled(boolean enabled) {
		WebImageCache.setDiskCachingEnabled(enabled);
	}

	public static void setDiskCachingDefaultCacheTimeout(int seconds) {
		WebImageCache.setDiskCachingDefaultCacheTimeout(seconds);
	}

	@Override
	public void onDetachedFromWindow() {
		// cancel loading if view is removed
		cancelCurrentLoad();
	}

	public void setImageWithURL(Context context, String urlString, Drawable placeholderDrawable, int diskCacheTimeoutInSeconds) {
		setImageDrawable(placeholderDrawable);
		
		// get image
		setImageWithURL(context, urlString, diskCacheTimeoutInSeconds);
	}

	public void setImageWithURL(Context context, String urlString, Drawable placeholderDrawable) {
		setImageDrawable(placeholderDrawable);
		
		// get image
		setImageWithURL(context, urlString, -1);
	}

	public void setImageWithURL(final Context context, final String urlString, int diskCacheTimeoutInSeconds) {
	    final WebImageManager mgr = WebImageManager.getInstance();

	    // cancel any existing request
	    cancelCurrentLoad();
	    
	    // clear
    	setImageDrawable(null);

    	// load the image any way we can
	    if (urlString != null) {
	    	mgr.downloadURL(context, urlString, WebImageView.this, diskCacheTimeoutInSeconds);
	    }
	}

	public void setImageWithURL(final Context context, final String urlString) {
	    final WebImageManager mgr = WebImageManager.getInstance();

	    // cancel any existing request
	    cancelCurrentLoad();
	    
	    // clear
    	setImageDrawable(null);

    	// load the image any way we can
	    if (urlString != null) {
	    	mgr.downloadURL(context, urlString, WebImageView.this, -1);
	    }
	}

	public void cancelCurrentLoad() {
	    WebImageManager mgr = WebImageManager.getInstance();

	    // cancel any existing request
	    mgr.cancelForWebImageView(this);
	}

}

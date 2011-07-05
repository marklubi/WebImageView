package com.raptureinvenice.webimageview.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class WebImageCache {
	private final String TAG = getClass().getSimpleName();

	// cache rules
	private static boolean mIsMemoryCachingEnabled = true;
	private static boolean mIsDiskCachingEnabled = true;
	
	private Map<String, SoftReference<Bitmap>> mMemCache;
	
	public WebImageCache() {
		mMemCache = new HashMap<String, SoftReference<Bitmap>>();
	}

	public static void setMemoryCachingEnabled(boolean enabled) {
		mIsMemoryCachingEnabled = enabled;
	}

	public static void setDiskCachingEnabled(boolean enabled) {
		mIsDiskCachingEnabled = enabled;
	}

	public Bitmap getBitmapFromMemCache(String urlString) {
		if (mIsMemoryCachingEnabled) {
			synchronized (mMemCache) {
				SoftReference<Bitmap> bitmapRef = mMemCache.get(urlString);
				
				if (bitmapRef != null) {
					Bitmap bitmap = bitmapRef.get();
					
					if (bitmap == null) {
						mMemCache.remove(urlString);
					} else {
						return bitmap; 
					}
				}				
			}
		}
		
		return null;
	}

	public Bitmap getBitmapFromDiskCache(Context context, String urlString) {
		if (mIsDiskCachingEnabled) {
			Bitmap bitmap = null;
			File path = context.getCacheDir();
	        InputStream is = null;
	        String encodedURLString = encodedURLString(urlString);
	        
	        File file = new File(path, encodedURLString);
	
	        if (file.exists() && file.canRead()) {
		        try {
		        	is = new FileInputStream(file);
			
		        	bitmap = BitmapFactory.decodeStream(is);
			        Log.v(TAG, "Retrieved " + urlString + " from cache.");
		        } catch (Exception ex) {
		        	Log.e(TAG, "Could not retrieve " + urlString + " from disk cache: " + ex.toString());
		        } finally {
		        	try {
		        		is.close();
		        	} catch (Exception ex) {}
		        }
	        }			
			
			return bitmap;
		}
		
		return null;
	}


	public void addBitmapToMemCache(String urlString, Bitmap bitmap) {
		if (mIsMemoryCachingEnabled) {
			synchronized (mMemCache) {
				mMemCache.put(urlString, new SoftReference<Bitmap>(bitmap));
			}
		}
	}
	
	public void addBitmapToCache(Context context, String urlString, Bitmap bitmap) {
		// mem cache
		addBitmapToMemCache(urlString, bitmap);

		// disk cache
		// TODO: manual cache cleanup
		if (mIsDiskCachingEnabled) {
			File path =  context.getCacheDir();
	        OutputStream os = null;
	        String encodedURLString = encodedURLString(urlString);
	        
	        try {
		        // NOWORKY File tmpFile = File.createTempFile("wic.", null);
		        File file = new File(path, encodedURLString);
		        os = new FileOutputStream(file.getAbsolutePath());
		
		        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
		        os.flush();
		        os.close();
		        
		        // NOWORKY tmpFile.renameTo(file);
	        } catch (Exception ex) {
	        	Log.e(TAG, "Could not store " + urlString + " to disk cache: " + ex.toString());
	        } finally {
	        	try {
	        		os.close();
	        	} catch (Exception ex) {}
	        }
		}
	}
	
	private String encodedURLString(String urlString) {
		urlString = urlString.replace("://", "_");
		urlString = urlString.replace("/", "_");
		urlString = urlString.replace("\\", "_");
		
		return urlString;
	}
}

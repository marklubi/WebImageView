package com.raptureinvenice.webimageview.download;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.raptureinvenice.webimageview.cache.WebImageCache;

public class WebImageManagerRetriever extends AsyncTask<Void, Void, Bitmap> {
	private final String TAG = getClass().getSimpleName();

	// cache
	private static WebImageCache mCache;
	
	// what we're looking for
	private Context mContext;
	private String mURLString;

	static {
		mCache = new WebImageCache();
	}
	
	public WebImageManagerRetriever(Context context, String urlString) {
		mContext = context;
		mURLString = urlString;
	}
	
	@Override
	protected Bitmap doInBackground(Void... params) {
		// check mem cache first
		Bitmap bitmap = mCache.getBitmapFromMemCache(mURLString);

		// check disk cache first
		if (bitmap == null) {
			bitmap = mCache.getBitmapFromDiskCache(mContext, mURLString);
			mCache.addBitmapToMemCache(mURLString, bitmap);
		}
		
		if (bitmap == null) {
			InputStream is = null;
			FlushedInputStream fis = null;
			
			try {
				URL url = new URL(mURLString);
				URLConnection conn = url.openConnection();
				
				is = conn.getInputStream();
				fis = new FlushedInputStream(is);
				
				bitmap = BitmapFactory.decodeStream(fis);

				// cache
				if (bitmap != null) {
					mCache.addBitmapToCache(mContext, mURLString, bitmap);
				}
			} catch (Exception ex) {
				Log.e(TAG, "Error loading image from URL " + mURLString + ": " + ex.toString());
			} finally {
				try {
					is.close();
				} catch (Exception ex) {}
			}
		}

		return bitmap;
	}

	@Override
    protected void onPostExecute(Bitmap bitmap) {
		// complete!
		WebImageManager.getInstance().reportImageLoad(mContext, mURLString, bitmap);
    }

    static class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            
            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                
                if (bytesSkipped == 0L) {
                    int b = read();
                    
                    if (b < 0) {
                        break;  // we reached EOF
                    } else {
                        bytesSkipped = 1; // we read one byte
                    }
                }
                
                totalBytesSkipped += bytesSkipped;
            }
            
            return totalBytesSkipped;
        }
    }
}

package com.raptureinvenice.webimageview.download;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.graphics.Bitmap;

import com.raptureinvenice.webimageview.image.WebImageView;

public class WebImageManager {
	private static WebImageManager mInstance = null;
	
	// TODO: pool retrievers
	
	// views waiting for an image to load in
	private Map<String, WebImageManagerRetriever> mRetrievers;
	private Map<WebImageManagerRetriever, Set<WebImageView>> mRetrieverWaiters;
	private Set<WebImageView> mWaiters;
	
	public static WebImageManager getInstance() {
		if (mInstance == null) {
			mInstance = new WebImageManager();
		}
		
		return mInstance;
	}
	
	private WebImageManager() {
		mRetrievers = new HashMap<String, WebImageManagerRetriever>();
		mRetrieverWaiters = new HashMap<WebImageManagerRetriever, Set<WebImageView>>();
		mWaiters = new HashSet<WebImageView>();
	}

	public void downloadURL(Context context, String urlString, final WebImageView view) {
		WebImageManagerRetriever retriever = mRetrievers.get(urlString);

		if (mRetrievers.get(urlString) == null) {
			retriever = new WebImageManagerRetriever(context, urlString);			
			mRetrievers.put(urlString, retriever);
			mWaiters.add(view);

			Set<WebImageView> views = new HashSet<WebImageView>();
			views.add(view);
			mRetrieverWaiters.put(retriever, views);
			
			// start!
			retriever.execute();
		} else {
			mRetrieverWaiters.get(retriever).add(view);
			mWaiters.add(view);
		}
	}

	public void reportImageLoad(Context context, String urlString, Bitmap bitmap) {
		WebImageManagerRetriever retriever = mRetrievers.get(urlString);

		for (WebImageView iWebImageView : mRetrieverWaiters.get(retriever)) {
			if (mWaiters.contains(iWebImageView)) {
				iWebImageView.setImageBitmap(bitmap);
				mWaiters.remove(iWebImageView);
			}
		}
		
		mRetrievers.remove(urlString);
		mRetrieverWaiters.remove(retriever);
	}

	public void cancelForWebImageView(WebImageView view) {
		// TODO: cancel connection in progress, too
		mWaiters.remove(view);
	}
}

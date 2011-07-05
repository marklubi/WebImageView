The Android WebImageView
========================

Ever need to display an image that's on a Web server somewhere, but don't want to write the HTTP code to load it?  Does the thought of writing that code to show images in a ListView make your eyes glaze over?  Do you want to be able to cache Web images so they don't have be loaded over and over again?  Would you even want to *support* that code anyway?

WebImageView is an Android drop-in image view replacement that can be set to display a Web-based URL that automatically handles in-memory and to-disk caching for optimal performance. And since it extends ImageView, you can use it to display bundled images just as you did before.

You have everything to gain and nothing to lose.  Use WebImageView. :-)


Features
--------

- Set an image URL directly from the Web.
- Images are cached in memory and unused images are released automatically when memory becomes low.
- Images are cached to disk.
- Disk cache expiration times per image for fine-grained control.
- Both image and disk caches can be disabled to serve the needs of your application.
- All Web and disk loads are asynchronous to achieve critically important user response time.
- Supports list-based usage when images are loaded and canceled repeatedly. 
- Plus all the ImageView functionality you've already come to expect (WebImageView extends ImageView)


Getting Started
---------------

Using WebImageView is simple.  It extends ImageView, so you can define your images in XML like this:

	<com.raptureinvenice.webimageview.image.WebImageView
		android:id="@+id/my_img"
		android:layout_width="fill_parent"
		android:layout_height="fill_parent"
		android:background="#00000000" />

And then you can set images in your activity, or other code, like so:

	WebImageView *myImage = (WebImageView)findViewById(R.id.my_img);
    myImage.setImageWithURL(context, "http://raptureinvenice.com/images/samples/pic-2.png");
	
You may also specify the disk cache timeout per image. (Memory cache lasts as long as memory does.) You might have cases where you want to expire certain images such as revolving images more quickly than, perhaps, non-critical images like ballplayer bio images.  You can change the global default (1 day) with:

	WebImageView.setDiskCachingDefaultCacheTimeout(60 * 60); // 1 hour expiration
	
To override the default, and specify other options, you may call setImageWithURL() in any number of ways:

	void setImageWithURL(Context context, String urlString, Drawable placeholderDrawable, int diskCacheTimeoutInSeconds)
	void setImageWithURL(Context context, String urlString, Drawable placeholderDrawable)
	void setImageWithURL(final Context context, final String urlString, int diskCacheTimeoutInSeconds)
	void setImageWithURL(final Context context, final String urlString)
	

Disabling the Caches
--------------------

By default, memory and disk caching are active at all times.  In a few cases, however, you may want to disable one or both of them.

The memory cache may be disabled to conserve RAM by calling:

	WebImageView.setMemoryCachingEnabled(false);
	
The disk cache may be disabled to prevent disk writing by calling:

	WebImageView.setDiskCachingEnabled(false);
	
Either method should be called in a place that will be called before any caching occurs.  This can either be in the onCreate() of
the activity you will be using WebImageView, or in the onCreate() of your base activity so it's always disabled.  Additionally,
you can implement a custom Application and place it in the onCreate() as well.


Logging
-------

To keep track of what's happening with the image cache, create a Log filter for WebImageCache.  You'll see all the images being accessed from the caches and when they expire.


Features Coming Real Soon!
--------------------------

- Placeholder views so arbitrary views can be present while images are loading.


Licensing
---------

This code is released under the MIT License by [Rapture In Venice](http://www.raptureinvenice.com)

The Android WebImageView
========================

An Android drop-in image view replacement that can be set to display a Web-based URL that automatically handles in-memory and to-disk caching for optimal performance.


Features
--------

- Set an image directly from the Web.
- Images are cached in memory and released when memory becomes low.
- Images are cached to disk.
- Both image and disk caches can be disabled to match the needs of your application.
- All loads are asynchronous for maximum speed and main-thread interactivity.
- Supports list-based usage when images are loaded and canceled repeatedly. 


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


Features Coming Real Soon
-------------------------

- Placeholder views so arbitrary views can be present while images are loading.
- Disk cache expiration times per image.


Licensing
---------

This code is released under the MIT License by [Rapture In Venice](http://www.raptureinvenice.com)

package com.jackie.sample.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;


public class BitmapUtils {
	// 把Resource转化成Bitmap
	public static Bitmap getBitmapFromResource(Context c, int resource) {
		Resources res = c.getResources();
		return BitmapFactory.decodeResource(res, resource);
	}
	
	// 把指定的Bitmap缩放到指定的大小
	public static Bitmap scaleBtimap(Bitmap bitmap, int width, int height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		return newbmp;
	}
	
	// 把drawable转化成Bitmap
	public static Bitmap drawableToBitmap(Drawable drawable) {
    	int w = drawable.getIntrinsicWidth();
    	int h = drawable.getIntrinsicHeight();   

    	// 取 drawable 的颜色格式   
    	Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888 : Config.RGB_565;
    	// 建立对应 bitmap   
    	Bitmap bitmap = Bitmap.createBitmap(w, h, config);
    	// 建立对应 bitmap 的画布   
    	Canvas canvas = new Canvas(bitmap);
    	drawable.setBounds(0, 0, w, h);   
    	// 把 drawable 内容画到画布中   
    	drawable.draw(canvas);   
    	return bitmap;   
    } 
	
    // 把Bitmap转化为Drawable
    public static Drawable drawableToBitmap(Bitmap bmp) {
    	BitmapDrawable drawable = new BitmapDrawable(bmp);
    	return drawable;
    }
    
    // 获得带倒影的Bitmap图片
    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
    	final int reflectionGap = 4;   
    	int w = bitmap.getWidth();   
    	int h = bitmap.getHeight();   

    	Matrix matrix = new Matrix();
    	matrix.preScale(1, -1);   

    	Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, h / 2, w,
    			h / 2, matrix, false);   

    	Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2),
    			Config.ARGB_8888);

    	Canvas canvas = new Canvas(bitmapWithReflection);
    	canvas.drawBitmap(bitmap, 0, 0, null);   
    	Paint deafalutPaint = new Paint();
    	canvas.drawRect(0, h, w, h + reflectionGap, deafalutPaint);   

    	canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);   
    	Paint paint = new Paint();
    	LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
    			bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
    	paint.setShader(shader);   
    	// Set the Transfer mode to be porter duff and destination in   
    	paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
    	// Draw a rectangle using the paint with our linear gradient   
    	canvas.drawRect(0, h, w, bitmapWithReflection.getHeight()   
    			+ reflectionGap, paint);   

    	return bitmapWithReflection;   
    }  
    
    // 获得一个圆角Bitmap图片
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
    	int w = bitmap.getWidth();   
    	int h = bitmap.getHeight();   
    	Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
    	Canvas canvas = new Canvas(output);
    	final int color = 0xff424242;   
    	final Paint paint = new Paint();
    	final Rect rect = new Rect(0, 0, w, h);
    	final RectF rectF = new RectF(rect);
    	paint.setAntiAlias(true);   
    	canvas.drawARGB(0, 0, 0, 0);   
    	paint.setColor(color);   
    	canvas.drawRoundRect(rectF, roundPx, roundPx, paint);   
    	paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
    	canvas.drawBitmap(bitmap, rect, rect, paint);   

    	return output;   
    } 
    
    // 把一张Bitmap转化为一个字节数组
    public static byte[] Bitmap2Bytes(Bitmap bm) {
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
    	return baos.toByteArray();   
    } 
    
    public static Bitmap Bytes2Bimap(byte[] b) {
    	if (b.length != 0) {   
    		return BitmapFactory.decodeByteArray(b, 0, b.length);
    	} else {   
    		return null;   
    	}   
    }     
    
	// 图片从中心旋转degree度
	public static Bitmap rotateImage(Bitmap bp, int degree) {
		Matrix matrix = new Matrix();
		// 从图片的中心旋转90度
		matrix.postRotate(degree, bp.getWidth()/2, bp.getHeight()/2);
		Bitmap bitmap = Bitmap.createBitmap(bp, 0, 0, bp.getWidth(), bp.getHeight(), matrix, true);
		return bitmap;
	}

	public static int getBitmapDegree(String path) {
		short degree = 0;

		try {
			ExifInterface e = new ExifInterface(path);
			int orientation = e.getAttributeInt("Orientation", 1);
			switch (orientation) {
				case 3:
					degree = 180;
					break;
				case 6:
					degree = 90;
					break;
				case 8:
					degree = 270;
			}
		} catch (IOException var4) {
			var4.printStackTrace();
		}

		return degree;
	}

	public static Bitmap rotateBitmapByDegree(Bitmap bitmap, int degree) {
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//		if (bitmap != null && !bitmap.isRecycled()) {
//			bitmap.recycle();
//		}
		return newBitmap;
	}

	public static Bitmap decodeBitmapFromFile(File imageFile, int requestWidth, int requestHeight) {
		return imageFile != null ? decodeBitmapFromFile(imageFile.getAbsolutePath(), requestWidth, requestHeight) : null;
	}

	private static final String TAG = "BitmapUtil";

	public static Bitmap decodeBitmapFromFile(String imagePath, int requestWidth, int requestHeight) {
		if (TextUtils.isEmpty(imagePath)) {
			return null;
		} else {
			Log.i(TAG, "requestWidth: " + requestWidth);
			Log.i(TAG, "requestHeight: " + requestHeight);
			if (requestWidth > 0 && requestHeight > 0) {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(imagePath, options);
				Log.i(TAG, "original height: " + options.outHeight);
				Log.i(TAG, "original width: " + options.outWidth);
				if (options.outHeight == -1 || options.outWidth == -1) {
					try {
						ExifInterface e = new ExifInterface(imagePath);
						int height = e.getAttributeInt("ImageLength", 1);
						int width = e.getAttributeInt("ImageWidth", 1);
						Log.i(TAG, "exif height: " + height);
						Log.i(TAG, "exif width: " + width);
						options.outWidth = width;
						options.outHeight = height;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				options.inSampleSize = calculateInSampleSize2(options, requestWidth, requestHeight);
				Log.i(TAG, "inSampleSize: " + options.inSampleSize);
				options.inJustDecodeBounds = false;
				return BitmapFactory.decodeFile(imagePath, options);
			} else {
				Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
				return bitmap;
			}
		}
	}

	public static int calculateInSampleSize2(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		int height = options.outHeight;
		int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			int halfHeight = height / 2;

			for (int halfWidth = width / 2; halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth; inSampleSize *= 2) {
			}

			long totalPixels = (long) (width * height / inSampleSize);//压缩后的原图的总像素

			//	如果压缩后的原图总像素还是比要求宽高的照片的像素还要高, 那么继续压缩
			for (long totalReqPixelsCap = (long) (reqWidth * reqHeight * 2); totalPixels > totalReqPixelsCap; totalPixels /= 2L) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public static Bitmap getCroppedRoundBitmap(Bitmap bmp, int radius) {
		Bitmap scaledSrcBmp;
		int diameter = radius * 2;

		// 为了防止宽高不相等，造成圆形图片变形，因此截取长方形中处于中间位置最大的正方形图片
		int bmpWidth = bmp.getWidth();
		int bmpHeight = bmp.getHeight();
		int squareWidth = 0, squareHeight = 0;
		int x = 0, y = 0;
		Bitmap squareBitmap;
		if (bmpHeight > bmpWidth) {// 高大于宽
			squareWidth = squareHeight = bmpWidth;
			x = 0;
			y = (bmpHeight - bmpWidth) / 2;
			// 截取正方形图片
			squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,
					squareHeight);
		} else if (bmpHeight < bmpWidth) {// 宽大于高
			squareWidth = squareHeight = bmpHeight;
			x = (bmpWidth - bmpHeight) / 2;
			y = 0;
			squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,
					squareHeight);
		} else {
			squareBitmap = bmp;
		}

		if (squareBitmap.getWidth() != diameter
				|| squareBitmap.getHeight() != diameter) {
			scaledSrcBmp = Bitmap.createScaledBitmap(squareBitmap, diameter,
					diameter, true);

		} else {
			scaledSrcBmp = squareBitmap;
		}
		Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(),
				scaledSrcBmp.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(),
				scaledSrcBmp.getHeight());

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawCircle(scaledSrcBmp.getWidth() / 2,
				scaledSrcBmp.getHeight() / 2, scaledSrcBmp.getWidth() / 2,
				paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);
		// bitmap回收(recycle导致在布局文件XML看不到效果)
		// bmp.recycle();
		// squareBitmap.recycle();
		// scaledSrcBmp.recycle();
		bmp = null;
		squareBitmap = null;
		scaledSrcBmp = null;
		return output;
	}

	// 计算图片的缩放值int reqWidth, int reqHeight这两个参数表示图片希望压缩到多大
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;//	原图片的高度
		final int width = options.outWidth;//	原图片的宽度
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height / (float) reqHeight);//	四舍五入, 原图高度除以希望压缩到的高度
			final int widthRatio = Math.round((float) width / (float) reqWidth);//	四舍五入, 原图宽度除以以往压缩到的宽度

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;//	选择较小的那个比例, 保证宽和高压缩后的大小都不会小于期望的大小
		}

		return inSampleSize;
	}

	/**
	 * Android中将View的内容保存为图像的方法
	 * @param view
	 * @return
	 */
	public Bitmap createViewBitmap(View view) {
		Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);

		return bitmap;
	}

	// 虚化
	public static Bitmap blurBitmap(Context applicationContext, Bitmap bitmap, float radius) {
		//Let's create an empty bitmap with the same size of the bitmap we want to blur
		Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

		//Instantiate a new Renderscript
		RenderScript rs = RenderScript.create(applicationContext);

		//Create an Intrinsic Blur Script using the Renderscript
		ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

		//Create the Allocations (in/out) with the Renderscript and the in/out bitmaps
		Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
		Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);
		//Set the radius of the blur
		blurScript.setRadius(radius);
		//Perform the Renderscript
		blurScript.setInput(allIn);
		blurScript.forEach(allOut);

		//Copy the final bitmap created by the out Allocation to the outBitmap
		allOut.copyTo(outBitmap);

		//recycle the original bitmap
		bitmap.recycle();

		//After finishing everything, we destroy the Renderscript.
		rs.destroy();

		return outBitmap;
	}

	public static Bitmap drawable2Bitmap(Drawable drawable, int... defaultWH) {
		if (drawable == null)
			return null;
		if (drawable instanceof BitmapDrawable)
			return ((BitmapDrawable) drawable).getBitmap();
		try {
			Bitmap bitmap;
			if (drawable instanceof ColorDrawable)
				bitmap = Bitmap.createBitmap(defaultWH[0], defaultWH[1], Bitmap.Config.ARGB_8888);
			else
				bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
						Bitmap.Config.ARGB_8888);

			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
			drawable.draw(canvas);
			return bitmap;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		}
	}
}

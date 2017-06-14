package com.jackie.sample.wechat_image_picker.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by Jackie on 2015/12/26.
 * 图片加载类
 */
public class ImageLoader {
    private ImageLoader(){};

    /**
     *
     * @param threadCount  线程数
     * @param type         队列调用方式
     */
    public ImageLoader(int threadCount, Type type) {
        init(threadCount, type);
    }

    private void init(int threadCount, Type type) {
        //后台轮询线程
        mLooperThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                mLooperHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        //线程池去取出一个任务进行执行
                        mThreadPool.execute(getTask());
                        try {
                            mThreadPoolSemaphore.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };

                //释放一个信号量
                mLooperHandlerSemaphore.release();
                Looper.loop();
            }
        });

        mLooperThread.start();

        //应用的最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory / 8;
        mLruCache = new LruCache<String, Bitmap>(cacheMemory) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
//                return value.getRowBytes() * value.getHeight();
            }
        };

        //创建线程池
        mThreadPool = Executors.newFixedThreadPool(threadCount);
        mTaskQueue = new LinkedList<>();
        mType = type;

        mThreadPoolSemaphore = new Semaphore(threadCount);
    }

    private static ImageLoader mInstance;

    /**
     * 图片缓存的核心对象
     */
    private LruCache<String ,Bitmap> mLruCache;
    /**
     * 线程池
     */
    private ExecutorService mThreadPool;
    private static final int DEFAULT_THREAD_COUNT = 1;
    private Semaphore mThreadPoolSemaphore;

    /**
     * 队列的调度方式
     * FIFO  先进先出
     * LIFO  后进先出
     */
    private Type mType = Type.LIFO;

    /**
     * 任务队列
     */
    private LinkedList<Runnable> mTaskQueue;

    /**
     * 后台轮循线程
     */
    private Thread mLooperThread;
    private Handler mLooperHandler;
    private Semaphore mLooperHandlerSemaphore = new Semaphore(0); //默认是0，acquire的时候肯定会阻塞，release的时候会+1

    /**
     * UI线程中的Handler
     */
    private Handler mUIHandler;

    public enum Type{
        FIFO, LIFO;
    }

    public static ImageLoader getInstance() {
        if (mInstance == null) {
            synchronized (ImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoader(DEFAULT_THREAD_COUNT, Type.LIFO);
                }
            }
        }

        return mInstance;
    }

    public static ImageLoader getInstance(int threadCount, Type type) {
        if (mInstance == null) {
            synchronized (ImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoader(threadCount, type);
                }
            }
        }

        return mInstance;
    }

    /**
     * 根据图片的path为ImageView设置图片
     * @param path       路径
     * @param imageView  ImageView
     */
    public void loadImage(final String path, final ImageView imageView) {
        imageView.setTag(path);

        if (mUIHandler == null) {
            mUIHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    //获取图片，为ImageView回调设置图片

                    ImageBean imageBean = (ImageBean) msg.obj;
                    Bitmap bitmap = imageBean.bitmap;
                    ImageView imageView = imageBean.imageView;
                    String path = imageBean.path;

                    //将path与getTag存储路径进行比较
                    if (imageView.getTag().toString().equals(path)) {
                        imageView.setImageBitmap(bitmap);
                    }
                }
            };
        }

        Bitmap bitmap = getBitmapFromLruCache(path);
        if (bitmap != null) {
            Message message = Message.obtain();
            ImageBean imageBean = new ImageBean();
            imageBean.bitmap = bitmap;
            imageBean.imageView = imageView;
            imageBean.path = path;
            message.obj = imageBean;
            mUIHandler.sendMessage(message);
        } else {
            addTask(new Runnable(){
                @Override
                public void run() {
                    //加载图片
                    //图片的压缩
                    //1. 获取图片需要显示的大小
                    ImageSize imageSize = getImageViewSize(imageView);
                    //2. 压缩图片
                    Bitmap bitmap = decodeSampleBitmapFromPath(path, imageSize.width, imageSize.height);
                    //3. 把图片加入到缓存
                    putBitmapToLruCache(path, bitmap);

                    Message message = Message.obtain();
                    ImageBean imageBean = new ImageBean();
                    imageBean.bitmap = bitmap;
                    imageBean.imageView = imageView;
                    imageBean.path = path;
                    message.obj = imageBean;
                    mUIHandler.sendMessage(message);

                    mThreadPoolSemaphore.release();
                }
            });
        }
    }

    /**
     * 根据图片需要显示的宽和高对图片进行压缩
     * @param path    路径
     * @param width   需要压缩的宽
     * @param height  需要压缩的高
     * @return
     */
    private Bitmap decodeSampleBitmapFromPath(String path, int width, int height) {
        //inJustDecodeBounds：获得图片的宽和高，但是并不把图片加载到内存中
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);  //options中已经有图片的实际的宽和高

        options.inSampleSize = calculateInSampleSize(options, width, height);
        //根据获取到的inSampleSize再次解析图片
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return bitmap;
    }

    /**
     * 根据需求的宽和高和图片实际的宽和高计算inSampleSize
     * @param options    options中已经有图片的宽和高
     * @param reqWidth   需要压缩的宽
     * @param reqHeight  需要压缩的高
     * @return
     */
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int width = options.outWidth;
        int height = options.outHeight;

        int inSampleSize = 1;
        if (width > reqWidth || height > reqHeight) {
            int widthRadio = Math.round(width  * 1.0f / reqWidth);
            int heightRadio = Math.round(height  * 1.0f / reqHeight);

            inSampleSize = Math.max(widthRadio, heightRadio);
        }

        return inSampleSize;
    }

    /**
     * 根据ImageView获得适当压缩的宽和高
     * @param imageView
     * @return
     */
    private ImageSize getImageViewSize(ImageView imageView) {

        //获取屏幕的宽度
        DisplayMetrics displayMetrics = imageView.getContext().getResources().getDisplayMetrics();

        ImageSize imageSize = new ImageSize();
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        int width = imageView.getWidth();  //获取ImageView的实际宽度
        if (width == 0) {
            width = params.width;  //获取ImageView在layout中声明的宽度
        }
        if (width <= 0) {
            //如果ImageView是wrap_content(-2)或者match_parent(-1)
//            width = imageView.getMaxWidth();  //检查最大值
            width = getObjectFiledValue(imageView, "mMaxWidth");
        }
        if (width <= 0) {width = displayMetrics.widthPixels;   //屏幕宽度
        }

        int height = imageView.getHeight();  //获取ImageView的实际高度
        if (height <= 0) {
            height = params.height;  //获取ImageView在layout中声明的高度
        }
        if (height <= 0) {
            //如果ImageView是wrap_content(-2)或者match_parent(-1)
//            height = imageView.getMaxHeight();  //检查最大值
            width = getObjectFiledValue(imageView, "mMaxHeight");
        }
        if (height <= 0) {
            height = displayMetrics.heightPixels;   //屏幕高度
        }

        imageSize.width = width;
        imageSize.height = height;
        return imageSize;
    }

    /**
     * 通过反射获取object的某个属性值
     * @param object
     * @param fieldName
     * @return
     */
    private int getObjectFiledValue(Object object, String fieldName) {
        int value = 0;
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = (int) field.get(object);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
                value = fieldValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }


    /**
     * 往队列中添加任务
     * @param runnable
     */
    private synchronized void addTask(Runnable runnable) {
        mTaskQueue.add(runnable);
        //通知后台轮循线程
        if (mLooperHandler == null) {
            try {
                mLooperHandlerSemaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mLooperHandler.sendEmptyMessage(0x110);
    }

    /**
     * 从队列中取出任务
     * @return
     */
    private Runnable getTask() {
        if (mType == Type.FIFO) {
            return mTaskQueue.removeFirst();
        } else if (mType == Type.LIFO) {
            return mTaskQueue.removeLast();
        }

        return null;
    }

    /**
     * 从LruCache取图片
     * @param  path
     * @return
     */
    private Bitmap getBitmapFromLruCache(String path) {
        return mLruCache.get(path);
    }

    /**
     * 把图片加入到LruCache
     * @param path
     * @param bitmap
     */
    private void putBitmapToLruCache(String path, Bitmap bitmap) {
        if (getBitmapFromLruCache(path) == null) {
            if (bitmap != null) {
                mLruCache.put(path, bitmap);
            }
        }
    }

    private class ImageBean {
        Bitmap bitmap;
        ImageView imageView;
        String path;
    }

    private class ImageSize {
        int width;
        int height;
    }
}

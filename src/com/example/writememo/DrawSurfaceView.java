package com.example.writememo;


import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View.OnTouchListener;


public class DrawSurfaceView extends SurfaceView
implements SurfaceHolder.Callback
{
	//ArrayList<Float> drawListX = new ArrayList<Float>();
	//ArrayList<Float> drawListY = new ArrayList<Float>();
	//private Path path = null;

	private SurfaceHolder mHolder;
	private Bitmap bitmap;

	private Canvas bmpCanvas;

	private Paint paint;

	private float px = -1;
	private float py = -1;

	private int WIDTH = 300;
	private int HEIGHT = 300;

	// ソースから生成されるときに呼び出される
	public DrawSurfaceView(Context context)
	{
		super(context);
		//path = new Path();
		getHolder().addCallback(this);

		// 線と色の設定
		paint = new Paint();
		paint.setColor(Color.RED);
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(6);
		paint.setStrokeCap(Paint.Cap.SQUARE);
		paint.setStrokeJoin(Paint.Join.ROUND);
	}

	// XMLからの生成時のコンストラクタ
	public DrawSurfaceView(Context context, AttributeSet attr)
	{
		super(context, attr);
		//path = new Path();
		getHolder().addCallback(this);

		// 線と色の設定
		paint = new Paint();
		paint.setColor(Color.RED);
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(6);
		paint.setStrokeCap(Paint.Cap.SQUARE);
		paint.setStrokeJoin(Paint.Join.ROUND);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
		WIDTH = width;
		HEIGHT = height;
		mHolder = holder;
		paint.setColor(Color.RED);
		clearCanvas();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
	}

	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
		switch(e.getAction())
		{
		case MotionEvent.ACTION_DOWN:
		{
			float x = e.getX();
			float y = e.getY();
			//drawListX.add();
			px = x;
			py = y;
		}
		case MotionEvent.ACTION_MOVE:
		{
			float x = e.getX();
			float y = e.getY();
			drawLine(x, y);
			px = x;
			py = y;
		}
		break;
		}
		return true;
	}

	public void drawLine(float nx, float ny)
	{
		Canvas canvas = mHolder.lockCanvas();
		if(bmpCanvas != null)
		{
			// current
			//bmpCanvas.drawPath(path, paint);
			bmpCanvas.drawLine(px, py, nx, ny, paint);

			canvas.drawBitmap(bitmap, 0, 0, null);
			mHolder.unlockCanvasAndPost(canvas);
			invalidate();
		}
	}

//	public void drawAllLine()
//	{
//		Canvas canvas = mHolder.lockCanvas();
//		if(bmpCanvas != null)
//		{
//			for(int i = 1; i < drawList.size(); ++i)
//			{
//				Point pp = drawList.get(i - 1);
//				Point np = drawList.get(i);
//				bmpCanvas.drawLine(pp.x, pp.y, np.x, np.y, paint);
//			}
//			canvas.drawBitmap(bitmap, 0, 0, null);
//			mHolder.unlockCanvasAndPost(canvas);
//			invalidate();
//		}
//	}

	public void clearCanvas()
	{
		bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
		// 裏でBitmapを操作するキャンバス
		bmpCanvas = new Canvas(bitmap);
		// 表示用のキャンバス
		Canvas canvas = mHolder.lockCanvas();
		// Bitmapを白で初期化する
		bmpCanvas.drawColor(Color.WHITE);
		// 裏書きを表示のキャンバスへコピーする
		canvas.drawBitmap(bitmap, 0, 0, null);
		mHolder.unlockCanvasAndPost(canvas);
		// 線を描画
		// drawLine();
		// キャンバスを更新する
		invalidate();
	}

	// 渡された画像をキャンバスにセットする関数
	public void setCanvas(Bitmap bitmap)
	{
		SurfaceHolder holder = getHolder();
		Canvas canvas = holder.lockCanvas();
		bmpCanvas = new Canvas(bitmap);
		canvas.drawBitmap(bitmap, 0, 0, null);
		holder.unlockCanvasAndPost(canvas);
	}

	// SurfaceView上で描画されているビットマップのインスタンスを返す
	public Bitmap getBitmap()
	{
		Bitmap copyBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
		return copyBitmap;
	}

	// 色を設定する
	public void setColor(int color)
	{
		paint.setColor(color);
		Log.i("MemoWrite", Integer.toString(color));
	}
}

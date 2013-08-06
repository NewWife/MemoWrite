package com.example.writememo;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;

import com.example.writememo.R;
import com.example.writememo.R.menu;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WriteMemo extends Activity
{
	private final String EXTERNAL_STORAGE_DIR = "WriteMemo";

	private DrawSurfaceView drawSurfaceView;
	private TextView colorConditionTextView;

	private boolean isExternal;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_write_memo);
		drawSurfaceView = (DrawSurfaceView)findViewById(R.id.draw_surface_view);
		colorConditionTextView = (TextView)findViewById(R.id.color_condition_text_view);

		// インテントの値を取得
		//Intent intent = getIntent();
		//
		//isExternal = intent.getExtras().getBoolean("IsExternal");

		// [DEBUG]
		isExternal = true;

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.write_memo, menu);
		return true;
	}

	private void outputBitmap(String fileName, Bitmap bitmap)
	{
		try
		{
			if(isExternal)
			{
				File dir = Environment.getExternalStorageDirectory();
				File appDir = new File(dir, "WriteMemo");
				if(!appDir.exists())
				{
					appDir.mkdir();
				}
				String path = new File(appDir, fileName).getAbsolutePath();
				FileOutputStream fos = new FileOutputStream(path);
				bitmap.compress(CompressFormat.JPEG, 80, fos);
				fos.close();
			}
			else
			{
				if(!getFilesDir().exists()) getFilesDir().mkdir();
				FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);
				bitmap.compress(CompressFormat.JPEG, 80, fos);
				fos.flush();
				fos.close();
			}
		}
		catch (Exception e)
		{
			Log.i("WriteMemo", e.getMessage());
			System.exit(-1);
		}
		Log.i("WriteMemo", "Complete Bitmap File Output");
	}

//	private Bitmap debugLoad(String path)
//	{
//		Bitmap bitmap = null;
//		InputStream input = null;
//		try
//		{
//			input = openFileInput(path);
//			bitmap = BitmapFactory.decodeStream(input);
//			input.close();
//		}
//		catch (Exception e)
//		{
//			Log.i("WriteMemo", e.getMessage());
//			System.exit(-1);
//		}
//		return bitmap;
//	}
//
//	private void debugConfirmImage(String fileName)
//	{
//		Intent intent = new Intent(getApplicationContext(), TestImageActivity.class);
//		intent.putExtra("FileName", fileName);
//		intent.putExtra("IsExternal", isExternal);
//		startActivity(intent);
//	}

	// 描画の終了とアクティビティの終了し、結果を返す
	public void onClickSaveAndExitButton(View v)
	{
		Intent intent = new Intent();
		Date mDate = new Date();
		SimpleDateFormat fileNameDate = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String fileName = fileNameDate.format(mDate) + ".jpg";
		Bitmap bitmap = drawSurfaceView.getBitmap();
		outputBitmap(fileName, bitmap);

		// 保存したパスをアクティビティに返す
		String resultPath = (isExternal) ? EXTERNAL_STORAGE_DIR + "/" + fileName : fileName;
		intent.putExtra("ImagePath", resultPath);
		intent.putExtra("IsExternal", isExternal);
		//setResult(RESULT_OK, intent);
		finish();
	}

	// キャンバスをクリアする
	public void onClickClearCanvas(View v)
	{
		drawSurfaceView.clearCanvas();
	}

	public void onClickRed(View v)
	{
		final int RED = Color.rgb(255, 0, 0);
		drawSurfaceView.setColor(RED);
		colorConditionTextView.setBackgroundColor(RED);
		colorConditionTextView.setText("R");
	}

	public void onClickGreen(View v)
	{
		final int GREEN = Color.rgb(0, 255, 0);
		drawSurfaceView.setColor(GREEN);
		colorConditionTextView.setBackgroundColor(GREEN);
		colorConditionTextView.setText("G");
	}

	public void onClickBlue(View v)
	{
		final int BLUE = Color.rgb(0, 0, 255);
		drawSurfaceView.setColor(BLUE);
		colorConditionTextView.setBackgroundColor(BLUE);
		colorConditionTextView.setText("B");
	}
}

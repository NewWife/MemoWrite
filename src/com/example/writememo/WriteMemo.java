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
import android.content.res.Resources;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WriteMemo extends Activity
{
	private int selectColor;

	private ColorPickerDialog mColorPickerDialog;
	private FrameLayout colorPickerFrame;

	private Button blackButton;
	private Button redButton;
	private Button greenButton;
	private Button blueButton;

	private final String EXTERNAL_STORAGE_DIR = "WriteMemo";
	private final int STROKE_WIDTH = 6;
	private final int ERASE_WIDTH = 100;

	private DrawSurfaceView drawSurfaceView;


	private boolean isExternal;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_write_memo);
		drawSurfaceView = (DrawSurfaceView)findViewById(R.id.draw_surface_view);
		colorPickerFrame = (FrameLayout)findViewById(R.id.color_picker_frame);
		blackButton = (Button)findViewById(R.id.black_color_button);
		redButton = (Button)findViewById(R.id.red_color_button);
		greenButton = (Button)findViewById(R.id.green_color_button);
		blueButton = (Button)findViewById(R.id.blue_color_button);

		mColorPickerDialog = new ColorPickerDialog(this,
				new ColorPickerDialog.OnColorChangedListener() {
			@Override
			public void colorChanged(int color)
			{
				selectColor = color;
				allButtonImageUnselected();
				drawSurfaceView.setColor(selectColor, STROKE_WIDTH);
				colorPickerFrame.setBackgroundColor(selectColor);
			}
		},
		Color.BLACK);

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

	private void allButtonImageUnselected()
	{
		blackButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.black_unselected_alpha));
		redButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_unselected_alpha));
		greenButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_unselected_alpha));
		blueButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_unselected_alpha));
	}

	// 黒をクリックしたら
	public void onClickBlack(View v)
	{
		allButtonImageUnselected();
		blackButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.black_selected_alpha));
		drawSurfaceView.setColor(Color.BLACK, STROKE_WIDTH);
		colorPickerFrame.setBackgroundColor(Color.BLACK);
	}

	// 黒をクリックしたら
	public void onClickRed(View v)
	{
		allButtonImageUnselected();
		redButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_selected_alpha));
		drawSurfaceView.setColor(0xffe88c82, STROKE_WIDTH);
		colorPickerFrame.setBackgroundColor(0xffe88c82);
	}

	// 黒をクリックしたら
	public void onClickGreen(View v)
	{
		allButtonImageUnselected();
		greenButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_selected_alpha));
		drawSurfaceView.setColor(0xff78bf96, STROKE_WIDTH);
		colorPickerFrame.setBackgroundColor(0xff78bf96);
	}

	// 黒をクリックしたら
	public void onClickBlue(View v)
	{
		allButtonImageUnselected();
		blueButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_selected_alpha));
		drawSurfaceView.setColor(0xff76b3db, STROKE_WIDTH);
		colorPickerFrame.setBackgroundColor(0xff76b3db);
	}

	// キャンバスをクリアする
	public void onClickClearCanvas(View v)
	{
		drawSurfaceView.clearCanvas();
	}

	/**
	 * 消しゴムボタンが押された時
	 */
	public void onClickEraser(View v)
	{
		drawSurfaceView.setColor(Color.WHITE, ERASE_WIDTH);
	}

	/**
	 * カラーピッカーボタンがクリックされた時
	 */
	public void onClickColorPicker(View v)
	{
		mColorPickerDialog.show();
	}
}

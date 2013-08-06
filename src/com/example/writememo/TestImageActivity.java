package com.example.writememo;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class TestImageActivity extends Activity
{
	Bitmap bitmap;

	//private static final int WIDTH = 300;
	//private static final int HEIGHT = 300;

	public TestImageActivity()
	{

	}

	private Bitmap loadBitmap(String fileName, boolean isExternal)
	{
		try
		{
			if(isExternal)
			{
				File dir = Environment.getExternalStorageDirectory();
				File appDir = new File(dir, "WriteMemo");
				if(!appDir.exists()) appDir.mkdir();
				File file = new File(appDir, fileName);
				String path = file.getAbsolutePath();
				if(file.exists())
				{
					bitmap = BitmapFactory.decodeFile(file.getPath());
				}
				else
				{
					// エラー処理
				}
			}
			else
			{
				FileInputStream in = openFileInput(fileName);
				BufferedInputStream binput = new BufferedInputStream(in);
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] w = new byte[1024];
				while(binput.read(w) >= 0)
				{
					out.write(w, 0, 1024);
				}
				byte[] byteData = out.toByteArray();
				bitmap = BitmapFactory.decodeByteArray(byteData, 0, byteData.length);
				in.close();
				binput.close();
				out.close();
			}
		}
		catch (Exception e)
		{
			Log.i("WriteMemo", e.getMessage());
			System.exit(-1);
		}
		Log.i("TestImage", "Complete Bitmap File Input");
		//Bitmap resizeBitmap = Bitmap.createScaledBitmap(bitmap, WIDTH, HEIGHT, false);
		//return resizeBitmap;
		return bitmap;
	}

	@Override
	public void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		Intent intent = getIntent();
		String fileName = intent.getStringExtra("FileName");
		boolean isExternal = intent.getExtras().getBoolean("IsExternal");
		bitmap = loadBitmap(fileName, isExternal);

		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		setContentView(layout);

		ImageView imageView = new ImageView(this);
		imageView.setImageBitmap(bitmap);
		imageView.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		imageView.setBackgroundColor(Color.rgb(0, 0, 0));
		layout.addView(imageView);
	}


}

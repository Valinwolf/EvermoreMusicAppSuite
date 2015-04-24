package com.twp.project.em;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.DigitalClock;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.twp.project.eml.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class L extends Activity
{
	// Views
	private static ProgressBar batteryBar;
	private static TextView batteryPercent;
	private TextView clockTextView;
	private TextView dayTextView;
	private TextView countDownView;
	private ImageButton settingsBtnView;
	private FrameLayout baseLayout;
	private LinearLayout timeLayout;

	// Multi-line variables
	CountDownTimer countDown = new CountDownTimer(5000, 1000)
	{
		@Override public void onTick(long millisUntilFinished)
		{
			long count = millisUntilFinished / 1000;
			countDownView.setText("Returning in " + count + "...");
			try
			{
				int percent = battery();
				batteryBar.setProgress(percent);
				batteryPercent.setTextColor(battery_color(percent));
				batteryPercent.setText(percent + "%");
			}
			catch(IOException ex)
			{
				batteryPercent.setText("ERR 1");
				Log.e(V.EM_TAG_BATT, "FAILED TO READ BATTERY FILE!", ex);
			}
			catch(IndexOutOfBoundsException ex)
			{
				batteryPercent.setText("ERR 2");
				Log.e(V.EM_TAG_BATT, "BATTERY FILE HAS AN INVALID VALUE!", ex);
			}
			catch(Exception ex)
			{
				batteryPercent.setText("ERR 3");
				Log.e(V.EM_TAG_BATT, "UNKNOWN ERROR!", ex);
			}
		}

		@Override public void onFinish()
		{
			countDownView.setText("Returning...");
			apollo();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if(SystemClock.elapsedRealtime() <= V.EM_BOOT_TIME)
		{
			apollo();
		}
		else
		{
			setContentView(R.layout.launcher_layout);
			Resources res = getResources();

			// Set view variables
			clockTextView = (DigitalClock) findViewById(R.id.timeDisplay);
			dayTextView = (TextView) findViewById(R.id.dayText);
			countDownView = (TextView) findViewById(R.id.countDown);
			settingsBtnView = (ImageButton) findViewById(R.id.settingsBtn);
			baseLayout = (FrameLayout) findViewById(R.id.baseLayout);
			timeLayout = (LinearLayout) findViewById(R.id.timeLayout);
			batteryBar = (ProgressBar) findViewById(R.id.batteryBar);
			batteryPercent = (TextView) findViewById(R.id.batteryPercent);

			// Set Settings button image and on-click listener
			settingsBtnView.setImageDrawable(res.getDrawable(R.mipmap.settings_icon));
			settingsBtnView.setOnClickListener(new View.OnClickListener()
			{
				@Override public void onClick(View v)
				{
					settings();
					countDown.cancel();
				}
			});

			// Get current date and set dayTextView text
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("EEEEE, d MMMMM yyyy", Locale.US);
			String date = df.format(cal.getTime());
			dayTextView.setText(date);

			// Return to Apollo click listeners
			View.OnClickListener clickReturn = new View.OnClickListener()
			{
				@Override public void onClick(View v)
				{
					apollo();
					countDown.cancel();
				}
			};
			clockTextView.setOnClickListener(clickReturn);
			dayTextView.setOnClickListener(clickReturn);
			baseLayout.setOnClickListener(clickReturn);
			timeLayout.setOnClickListener(clickReturn);
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if(SystemClock.elapsedRealtime() > V.EM_BOOT_TIME)
		{
			countDown.cancel();
			countDown.start();
			// Force reload the time
			clockTextView.invalidate();
		}
	}

	public void apollo()
	{
		Intent apolloLauncher = new Intent();
		apolloLauncher.addCategory(Intent.CATEGORY_LAUNCHER);
		apolloLauncher.setPackage(V.EM_PACKAGE_APOLLO);
		apolloLauncher.setAction(Intent.ACTION_MAIN);
		startActivity(apolloLauncher);
	}

	public void settings()
	{
		Intent settingsLauncher = new Intent();
		settingsLauncher.addCategory(Intent.CATEGORY_LAUNCHER);
		settingsLauncher.setPackage(V.EM_PACKAGE_SETTINGS);
		settingsLauncher.setAction(Intent.ACTION_MAIN);
		startActivity(settingsLauncher);
	}

	public int battery() throws IOException
	{
		File battery = new File("/sys/class/power_supply/battery/capacity");
		BufferedReader br = new BufferedReader(new FileReader(battery));
		return Integer.parseInt(br.readLine());
	}

	public ColorStateList battery_color(int percent) throws IndexOutOfBoundsException
	{
		if(percent <= 100)
		{
			return V.EM_COLORLIST_GREEN;
		}
		else if(percent <= 50)
		{
			return V.EM_COLORLIST_ORANGE;
		}
		else if(percent <= 25)
		{
			return V.EM_COLORLIST_RED;
		}
		else
		{
			throw new IndexOutOfBoundsException(percent+" IS NOT A VALID PERCENTAGE!");
		}
	}
}

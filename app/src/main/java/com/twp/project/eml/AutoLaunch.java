package com.twp.project.eml;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.View;
import android.widget.DigitalClock;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AutoLaunch extends Activity
{
	public static ProgressBar batteryBar;
	public static TextView batteryPercent;
	ColorStateList hi = new ColorStateList(new int[][]{new int[]{android.R.attr.state_pressed}, new int[]{android.R.attr.state_focused}, new int[]{}}, new int[]{Color.rgb(0, 190, 0), Color.rgb(255, 190, 0), Color.rgb(0, 150, 0)});
	ColorStateList mi = new ColorStateList(new int[][]{new int[]{android.R.attr.state_pressed}, new int[]{android.R.attr.state_focused}, new int[]{}}, new int[]{Color.rgb(255, 190, 0), Color.rgb(255, 190, 0), Color.rgb(255, 150, 0)});
	ColorStateList lo = new ColorStateList(new int[][]{new int[]{android.R.attr.state_pressed}, new int[]{android.R.attr.state_focused}, new int[]{}}, new int[]{Color.rgb(250, 0, 0), Color.rgb(250, 190, 0), Color.rgb(180, 0, 0)});
	CountDownTimer countDown = new CountDownTimer(5000, 1000)
	{
		@Override public void onTick(long millisUntilFinished)
		{
			long count = millisUntilFinished / 1000;
			countDownView.setText("Returning in " + count + "...");
			try
			{
				File battery = new File("/sys/class/power_supply/battery/capacity");
				BufferedReader br = new BufferedReader(new FileReader(battery));
				int percent = Integer.parseInt(br.readLine());
				batteryBar.setProgress(percent);
				batteryPercent.setText(percent + "%");
				if(percent <= 100)
				{
					batteryPercent.setTextColor(hi);
				}
				else if(percent <= 50)
				{
					batteryPercent.setTextColor(mi);
				}
				else if(percent <= 25)
				{
					batteryPercent.setTextColor(lo);
				}
				else
				{
					batteryPercent.setTextColor(lo);
					batteryPercent.setText("ERR 2");
				}
			}
			catch(Exception ex)
			{
				batteryPercent.setTextColor(lo);
				batteryPercent.setText("ERR 1");
			}
		}

		@Override public void onFinish()
		{
			countDownView.setText("Returning...");
			apollo();
		}
	};
	private TextView clockTextView;
	private TextView dayTextView;
	private TextView countDownView;
	private ImageButton settingsBtnView;
	private FrameLayout baseLayout;
	private LinearLayout timeLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if(SystemClock.elapsedRealtime() <= 45000)
		{
			apollo();
		}
		else
		{
			setContentView(R.layout.activity_auto_launch);
			Resources res = getResources();

			clockTextView = (DigitalClock) findViewById(R.id.timeDisplay);
			dayTextView = (TextView) findViewById(R.id.dayText);
			countDownView = (TextView) findViewById(R.id.countDown);
			settingsBtnView = (ImageButton) findViewById(R.id.settingsBtn);
			baseLayout = (FrameLayout) findViewById(R.id.baseLayout);
			timeLayout = (LinearLayout) findViewById(R.id.timeLayout);
			batteryBar = (ProgressBar) findViewById(R.id.batteryBar);
			batteryPercent = (TextView) findViewById(R.id.batteryPercent);

			settingsBtnView.setImageDrawable(res.getDrawable(R.mipmap.settings_icon));
			settingsBtnView.setOnClickListener(new View.OnClickListener()
			{
				@Override public void onClick(View v)
				{
					Intent settingsLauncher = new Intent(android.provider.Settings.ACTION_SETTINGS);
					startActivity(settingsLauncher);
					countDown.cancel();
				}
			});

			Calendar cal = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("EEEEE, d MMMMM yyyy");
			String date = df.format(cal.getTime());
			dayTextView.setText(date);

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
		if(SystemClock.elapsedRealtime() > 45000)
		{
			countDown.cancel();
			countDown.start();
		}
	}

	public void apollo()
	{
		Intent apolloLauncher = new Intent();
		apolloLauncher.addCategory(Intent.CATEGORY_LAUNCHER);
		apolloLauncher.setPackage("com.andrew.apollo");
		apolloLauncher.setAction(Intent.ACTION_MAIN);
		startActivity(apolloLauncher);
	}
}

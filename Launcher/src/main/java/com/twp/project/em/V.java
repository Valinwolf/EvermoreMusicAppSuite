package com.twp.project.em;

import android.content.res.ColorStateList;
import android.graphics.Color;

public class V
{
	// INT
	public static final int EM_BOOT_TIME = 20000;
	// STRING
	public static final String EM_PACKAGE_SETTINGS = "com.twp.project.ems";
	public static final String EM_PACKAGE_APOLLO = "com.andrew.apollo";
	public static final String EM_TAG_BATT = "BATTERY MONITOR";
	// COLORSTATELIST
	public static final ColorStateList EM_COLORLIST_GREEN = new ColorStateList(new int[][]{new int[]{android.R.attr.state_pressed}, new int[]{android.R.attr.state_focused}, new int[]{}}, new int[]{Color.rgb(0, 190, 0), Color.rgb(255, 190, 0), Color.rgb(0, 150, 0)});
	public static final ColorStateList EM_COLORLIST_ORANGE = new ColorStateList(new int[][]{new int[]{android.R.attr.state_pressed}, new int[]{android.R.attr.state_focused}, new int[]{}}, new int[]{Color.rgb(255, 190, 0), Color.rgb(255, 190, 0), Color.rgb(255, 150, 0)});
	public static final ColorStateList EM_COLORLIST_RED = new ColorStateList(new int[][]{new int[]{android.R.attr.state_pressed}, new int[]{android.R.attr.state_focused}, new int[]{}}, new int[]{Color.rgb(250, 0, 0), Color.rgb(250, 190, 0), Color.rgb(180, 0, 0)});
}

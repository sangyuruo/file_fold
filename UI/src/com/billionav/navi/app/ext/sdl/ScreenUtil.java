package com.billionav.navi.app.ext.sdl;

import java.math.BigDecimal;

import com.billionav.navi.app.ext.log.NaviLogUtil;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * 将车机的屏幕坐标转换为手机的屏幕坐标(按分辨率)
 * @author sangjun
 *
 */
public class ScreenUtil {
	int scale = 4;
	int roundingMode = 4;// 表示四舍五入，可以选择其他舍值方式，例如去尾，等等

	int screenWidth;
	int screenHeight;

	static int HU_WIDTH = 800;
	static int HU_HEIGHT = 480;

	double widthRate;
	double heightRate;

	String INFO_1 = "screenWidth is %d";
	String INFO_2 = "screenHeight is %d";
	String INFO_3 = "widthRate is %f";
	String INFO_4 = "heightRate is %f";

	public void init(Context cx) {

		DisplayMetrics dm = new DisplayMetrics();
		dm = cx.getApplicationContext().getResources().getDisplayMetrics();
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;

		NaviLogUtil.debugTouch(String.format(INFO_1, screenWidth));
		NaviLogUtil.debugTouch(String.format(INFO_2, screenHeight));

		if (screenWidth > screenHeight) {
			widthRate = (double) screenWidth / HU_WIDTH;
			heightRate = (double) screenHeight / HU_HEIGHT;
		} else {
			widthRate = (double) screenHeight / HU_WIDTH;
			heightRate = (double) screenWidth / HU_HEIGHT;
		}

		widthRate = new BigDecimal(widthRate).setScale(scale, roundingMode)
				.doubleValue();
		heightRate = new BigDecimal(heightRate).setScale(scale, roundingMode)
				.doubleValue();
		NaviLogUtil.debugTouch(String.format(INFO_3, widthRate));
		NaviLogUtil.debugTouch(String.format(INFO_4, heightRate));
	}

	public int getRealMoveWidth(int moveWidth) {
		return (int) Math.rint(moveWidth * widthRate);
	}

	public int getRealMoveHeight(int moveHeight) {
		return (int) Math.rint(moveHeight * heightRate);
	}

	public double getWidthRate() {
		return widthRate;
	}

	public double getHeightRate() {
		return heightRate;
	}

	static {
		instance = new ScreenUtil();
	}

	public static ScreenUtil getInstance() {
		return instance;
	}

	private ScreenUtil() {
	}

	static ScreenUtil instance;
}

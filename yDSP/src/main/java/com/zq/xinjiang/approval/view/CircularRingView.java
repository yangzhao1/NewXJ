package com.zq.xinjiang.approval.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.zq.xinjiang.R;

/** 
* @ClassName: CircularRingView 
* @Description:(自定义圆环) 
* @author drayge
* @date 2015年3月5日 上午9:57:35 
*  
*/
public class CircularRingView extends View {
	
	private int tangle = 0;
	private int delayMillis = 10;
	public CircularRingView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CircularRingView(Context context) {
		this(context, null);
	}

	/**
	 * 圈的宽度
	 */
	private int mCircleWidth = 0;
	/**
	 * 画笔
	 */
	private Paint mPaint;
	/**
	 * 颜色
	 */
	private int mColor = Color.GREEN;

	private int[] aryColor = null;
	private double[] aryColorValue = null;

	public CircularRingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircularRingView,
				defStyle, 0);
		int color = context.getResources().getColor(R.color.capital_color_3);
		mColor = color;
		// 获取XML配置的值
		for (int i = 0; i < typedArray.getIndexCount(); i++) {
			int attr = typedArray.getIndex(i);
			switch (attr) {
			case R.styleable.CircularRingView_circlewidth:
				mCircleWidth = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_PX, 89, getResources().getDisplayMetrics()));
				break;
			case R.styleable.CircularRingView_defaultColor:
				mColor = typedArray.getColor(attr, color);
				break;
			default:
				break;
			}
		}
		typedArray.recycle();
		mPaint = new Paint();
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int centre = getWidth() / 2; // 获取圆心的x坐标
		int radius = (int) (centre - mCircleWidth / 2) - 1;// 半径
		int ttangle = -90;
		mPaint.setStrokeWidth(mCircleWidth);
		mPaint.setAntiAlias(true); // 消除锯齿
		mPaint.setStyle(Paint.Style.STROKE); // 设置空心
		mPaint.setColor(mColor); // 设置圆环的颜色
		RectF oval = new RectF(centre - radius, centre - radius, centre + radius, centre + radius); // 用于定义的圆弧的形状和大小的界限

		if (aryColor != null & aryColorValue != null && aryColor.length == aryColorValue.length) {
			double total = 0;
			for (int i = 0; i < aryColorValue.length; i++) {
				total += aryColorValue[i];
			}
			if (total > 0) {
				for (int i = 0; i < aryColorValue.length; i++) {
					if (aryColorValue[i] > 0) {
						mPaint.setStrokeWidth(mCircleWidth);
						mPaint.setColor(aryColor[i]); // 设置圆环的颜色
						int d = (int) Math.round(aryColorValue[i] / total * 360);
						
						if(d >= tangle) {
							d = tangle;
						}
						
						canvas.drawArc(oval, ttangle, d, false, mPaint); // 根据进度画圆弧
						ttangle += d;
						
					}
				}
				if(ttangle < 270) {
					run(); 
				}
				
				
			} else {
				canvas.drawArc(oval, -90, 360, false, mPaint); // 根据进度画圆弧
			}
		} else {
			canvas.drawArc(oval, -90, 360, false, mPaint); // 根据进度画圆弧
		}
	}

	public void setmCircleWidth(int mCircleWidth) {
		this.mCircleWidth = mCircleWidth;
	}

	public void setmColor(int mColor) {
		this.mColor = mColor;
	}

	public void setAryColor(int[] aryColor) {
		this.aryColor = aryColor;
	}

	public void setAryColorValue(double[] aryColorValue) {
		this.aryColorValue = aryColorValue;
	}
	
	private void run() {
		if(tangle <= 360) {
			if(tangle < 120) {
				tangle += 8;
			} else if(tangle < 240) {
				tangle += 7;
			} else {
				tangle += 5;
			}
			
			postInvalidateDelayed(delayMillis);
		}
	}
}

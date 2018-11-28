package com.zq.xinjiang.approval.view;

import com.zq.xinjiang.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

public class DashedLineView extends View {

	public DashedLineView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(R.color.xx);
		Path path = new Path();
		path.moveTo(0, 10);
		path.lineTo(480, 10);
		PathEffect effects = new DashPathEffect(new float[] { 10, 10, 10, 10 },
				10);
		paint.setPathEffect(effects);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(4);
		canvas.drawPath(path, paint);
	}
}

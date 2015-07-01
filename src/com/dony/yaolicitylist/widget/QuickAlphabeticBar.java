package com.dony.yaolicitylist.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class QuickAlphabeticBar extends View {
    private static final String TAG = "QuickAlphabeticBar";
	OnTouchingAlphaChangedListener onTouchingAlphaChangedListener;
	String[] alphabetic;
	int choose = -1;
	Paint paint = new Paint();
	boolean showBkg = false;

	public QuickAlphabeticBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public QuickAlphabeticBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public QuickAlphabeticBar(Context context) {
		super(context);
	}

	public void setAlphas(String[] alphabetic){
	    this.alphabetic = alphabetic;
	  }
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (showBkg) {
			canvas.drawColor(Color.parseColor("#40000000"));
		}

		int height = getHeight();
		int width = getWidth() ;
		int singleHeight = height / alphabetic.length ;
		for (int i = 0; i < alphabetic.length; i++) {
			paint.setColor(Color.WHITE);
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAntiAlias(true);
			if (i == choose) {
				paint.setColor(Color.parseColor("#3399ff"));
				paint.setFakeBoldText(true);
			}
			float xPos = width / 2 - paint.measureText(alphabetic[i]) / 2;
			float yPos = singleHeight * i + singleHeight;
			canvas.drawText(alphabetic[i], xPos, yPos, paint);
			paint.reset();
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();
		final int oldChoose = choose;
		final OnTouchingAlphaChangedListener listener = onTouchingAlphaChangedListener;
		final int c = (int) (y / getHeight() * alphabetic.length);

		switch (action) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			if (oldChoose != c && listener != null) {
				if (c > 0 && c < alphabetic.length) {
					listener.onTouchingAlphaChanged(alphabetic[c]);
					choose = c;
					invalidate();
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			showBkg = false;
			choose = -1;
			invalidate();
			break;
		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	public void setOnTouchingAlphaChangedListener(
			OnTouchingAlphaChangedListener onTouchingAlphaChangedListener) {
		this.onTouchingAlphaChangedListener = onTouchingAlphaChangedListener;
	}

	public interface OnTouchingAlphaChangedListener {
		public void onTouchingAlphaChanged(String s);
	}
}

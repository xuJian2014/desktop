package com.example.utilTool;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
 
public class AppGridView extends GridView 
{
	public AppGridView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public AppGridView(Context context) 
	{
		super(context);
	}

	public AppGridView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) 
	{
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 3,MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}

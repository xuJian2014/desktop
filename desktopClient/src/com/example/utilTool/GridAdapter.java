package com.example.utilTool;

import com.example.desktop.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter 
{  
	private Context mContext;
	private String[] appList;
	private Bitmap[] bitmapArrs;
	public GridAdapter(Context mContext,String[] appList,Bitmap[] bitmapArrs)
	{
		this.mContext = mContext;
		this.appList=appList;
		this.bitmapArrs=bitmapArrs;
	}
	@Override
	public int getCount()
	{
		return appList.length;
	}
	@Override
	public Object getItem(int position) 
	{
		return appList[position];
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if (convertView == null)
		{
			convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item_app, parent, false);
		}
		ImageView iv = BaseViewHolder.get(convertView, R.id.itemImage);
		TextView tv = BaseViewHolder.get(convertView, R.id.itemText);
		
		Bitmap icon=getResizedBitmap(bitmapArrs[position],80,80);
     	  // icon.compress(Bitmap.CompressFormat.JPEG, 30);
     	if(icon!=null)
     		iv.setImageBitmap(icon); 
		tv.setText(appList[position]);
		return convertView;
	}
	
	 public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight)
     {
  	   Bitmap resizedBitmap=bm;
  	   try
  	   {
			int width = bm.getWidth();
			int height = bm.getHeight();
			float scaleWidth = ((float) newWidth) / width;
			float scaleHeight = ((float) newHeight) / height;
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,matrix, false);
  	    }
  	   catch(Exception e )
  	   {
  		 //
  	   }        	   
  	    return resizedBitmap;
  	}
}  

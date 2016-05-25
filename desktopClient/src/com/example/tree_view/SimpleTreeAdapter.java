package com.example.tree_view;

import java.util.List;

import com.example.desktop.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
public class SimpleTreeAdapter<T> extends TreeListViewAdapter<T>
{
	public SimpleTreeAdapter(ListView mTree, Context context, List<T> datas,int defaultExpandLevel) throws IllegalArgumentException,IllegalAccessException
	{
		super(mTree, context, datas, defaultExpandLevel);
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getConvertView(Node node , int position, View convertView, ViewGroup parent)
	{
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.tree_view_list_item, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.icon = (ImageView) convertView.findViewById(R.id.id_treenode_icon);
			viewHolder.label = (TextView) convertView.findViewById(R.id.id_treenode_label);
			viewHolder.file_icon=(ImageView) convertView.findViewById(R.id.file_icon);
			convertView.setTag(viewHolder);
		} 
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (node.getIcon() == -1)
		{
			viewHolder.icon.setVisibility(View.INVISIBLE);
		}
		else
		{
			viewHolder.icon.setVisibility(View.VISIBLE);
			viewHolder.icon.setImageResource(node.getIcon());
		}
		
		if(node.getFile_icon()==-1)
		{
			String suffixName=getExtensionName(node.getName());
			if("docx".equals(suffixName))
			{
				viewHolder.file_icon.setImageResource(R.drawable.media_word);
			} 
			else if(suffixName.equals("avi")||suffixName.equals("mp4"))
			{
				viewHolder.file_icon.setImageResource(R.drawable.media_avi);
			}
			else if(suffixName.equals("mp3"))
			{
				viewHolder.file_icon.setImageResource(R.drawable.media_music);
			}
			else if(suffixName.equals("xlsx"))
			{
				viewHolder.file_icon.setImageResource(R.drawable.media_excel);
			}
			else if(suffixName.equals("ppt")||suffixName.equals("pptx"))
			{
				viewHolder.file_icon.setImageResource(R.drawable.media_ppt);
			}
			else if(suffixName.equals("jpg")||suffixName.equals("png"))
			{
				viewHolder.file_icon.setImageResource(R.drawable.media_photo);
			}
			else if(suffixName.equals("dll")||suffixName.equals("Dll"))
			{
				viewHolder.file_icon.setImageResource(R.drawable.media_dll);
			}
			else if(suffixName.equals("txt")||suffixName.equals("xml"))
			{
				viewHolder.file_icon.setImageResource(R.drawable.media_txt);
			}
			else if(suffixName.equals("config")||suffixName.equals("Config"))
			{
				viewHolder.file_icon.setImageResource(R.drawable.media_config);
			}
			else if(suffixName.equals("html"))
			{
				viewHolder.file_icon.setImageResource(R.drawable.media_ie);
			}
			else if(suffixName.equals("rar")||suffixName.equals("zip"))
			{
				viewHolder.file_icon.setImageResource(R.drawable.media_rar);
			}
			else if(suffixName.equals("exe"))
			{
				viewHolder.file_icon.setImageResource(R.drawable.media_exe);
			}
			else if(node.getName().contains("迅雷"))
			{
				viewHolder.file_icon.setImageResource(R.drawable.media_xunlei);
			}
			
			else 
			{
				viewHolder.file_icon.setImageResource(R.drawable.media_item);
			}
			if(node.getpId()==0)
			{
				viewHolder.file_icon.setImageResource(R.drawable.file_icon);
			}
		}
		else
		{
			viewHolder.file_icon.setImageResource(node.getFile_icon());
		}
		viewHolder.label.setText(node.getName());
		return convertView;
	}

	public static String getExtensionName(String filename)
	{

		if ((filename != null) && (filename.length() > 0))
		{
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1)))
			{
				return filename.substring(dot + 1);
			}
		}
		return filename;
	}

	private final class ViewHolder
	{
		ImageView icon;
		ImageView file_icon;
		TextView label;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		return super.getView(position, convertView, parent);
	}
	
	public void addExtraNode(int position,String string,int flag)
	{
		Node node=mNodes.get(position);
		int indexOf=mAllNodes.indexOf(node);
		Node extraNode =new Node(-1,node.getId(),string);
		extraNode.setFlag(flag);
		extraNode.setParent(node);
		node.getChildren().add(extraNode);
		mAllNodes.add(indexOf+1, extraNode);
		mNodes=TreeHelper.filterVisibleNode(mAllNodes);
		notifyDataSetChanged();
	}
	
	
}

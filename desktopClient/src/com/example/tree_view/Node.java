package com.example.tree_view;

import java.util.ArrayList;
import java.util.List;

public class Node
{
	private int id;
	
	private int pId = 0;

	private String name;

	
	private int level;

	private boolean isExpand = false;

	private int icon;

	private int file_icon;
	
	private int flag;
	public int getFlag()
	{
		return flag;
	}

	public void setFlag(int flag)
	{
		this.flag = flag;
	}

	public int getFile_icon()
	{
		return file_icon;
	}
	public void setFile_icon(int file_icon)
	{
		this.file_icon = file_icon;
	}
	
	private List<Node> children = new ArrayList<Node>();

	
	private Node parent;

	public Node()
	{
	}

	public Node(int id, int pId, String name)
	{
		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
	}
	
	public Node(int id, int pId, String name,int flag)
	{
		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.flag=flag;
	}
	public int getIcon()
	{
		return icon;
	}

	public void setIcon(int icon)
	{
		this.icon = icon;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getpId()
	{
		return pId;
	}

	public void setpId(int pId)
	{
		this.pId = pId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}

	public boolean isExpand()
	{
		return isExpand;
	}

	public List<Node> getChildren()
	{
		return children;
	}

	public void setChildren(List<Node> children)
	{
		this.children = children;
	}

	public Node getParent()
	{
		return parent;
	}

	public void setParent(Node parent)
	{
		this.parent = parent;
	}

	
	public boolean isRoot()
	{
		return parent == null;
	}

	
	public boolean isParentExpand()
	{
		if (parent == null)
			return false;
		return parent.isExpand();
	}

	
	public boolean isLeaf()
	{
		return children.size() == 0;
	}

	public int getLevel()
	{
		return parent == null ? 0 : parent.getLevel() + 1;
	}

	public void setExpand(boolean isExpand)
	{
		this.isExpand = isExpand;
		if (!isExpand)
		{

			for (Node node : children)
			{
				node.setExpand(isExpand);
			}
		}
	}
}

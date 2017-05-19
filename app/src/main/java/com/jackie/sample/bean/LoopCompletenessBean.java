package com.jackie.sample.bean;

public class LoopCompletenessBean {
	private int resId; 
	private int color ;
	
	public LoopCompletenessBean() {
	}
	
	public LoopCompletenessBean(int resId, int color) {
		super();
		this.resId = resId;
		this.color = color;
	}

	public int getResId()
	{
		return resId;
	}
	public void setResId(int resId)
	{
		this.resId = resId;
	}
	public int getColor()
	{
		return color;
	}
	public void setColor(int color)
	{
		this.color = color;
	} 
}

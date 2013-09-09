package zack.bean;

import java.io.Serializable;

/**
 * @author 周艺津
 * @version 创建时间：2012-5-6 下午01:27:30
 * 
 */
public class Locate implements Serializable
{
	private String id;
	private String latitude;
	private String longtitude;
	private String name;
	private String icon;
	private String userId;
	private String createTime;
	private String provider;
	private String leaveMessage;
	
	public void setLeaveMessage(String leaveMessage)
	{
		this.leaveMessage=leaveMessage;
	}
	
	public String getLeaveMessage()
	{
		return this.leaveMessage;
	}
	
	public void setCreateTime(String createTime)
	{
		this.createTime=createTime;
	}
	
	public String getCreateTime()
	{
		return this.createTime;
	}
	
	public void setUserId(String userid)
	{
		this.userId=userid;
	}
	
	public String getUserId()
	{
		return this.userId;
	}
	public void setIcon(String icon)
	{
		this.icon=icon;
	}
	
	public String getIcon()
	{
		return this.icon;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setId(String id)
	{
		this.id=id;
	}
	
	public String getId()
	{
		return this.id;
	}
	
	public void setLongtitude(String longtitude)
	{
		this.longtitude=longtitude;
	}
	
	public String getLongtitude()
	{
		return this.longtitude;
	}
	public void setLatitude(String latitude)
	{
		this.latitude=latitude;
	}
	
	public String getLatitude()
	{
		return this.latitude;
	}
	
	public void setProvider(String provider)
	{
		this.provider=provider;
	}
	
	public String getProvider()
	{
		return this.provider;
	}
	

}

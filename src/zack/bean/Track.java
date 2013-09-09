package zack.bean;

import java.io.Serializable;

/**
 * @author 周艺津
 * @version 创建时间：2012-5-6 上午10:44:59
 * 
 */
public class Track implements Serializable
{
	private String observId;
	
	private String userId;
	
	private String state;
	
	private String message;
	
	private String name;
	
	private String icon;
	
	private String id;
	
	private String eventId;
	
	private String lastTime;
	
	private String isAccept;
	public void setId(String id)
	{
		this.id=id;
	}
	
	public String getId()
	{
		return this.id;
	}
	
	public void setObservId(String observid)
	{
		this.observId=observid;
	}
	
	public String getObservId()
	{
		return this.observId;
	}
	public void setUserId(String userid)
	{
		this.userId=userid;
	}
	
	public String getUserId()
	{
		return this.userId;
	}
	public void setState(String state)
	{
		this.state=state;
	}
	
	public String getState()
	{
		return this.state;
	}
	public void setMessage(String message)
	{
		this.message=message;
	}
	
	public String getMessage()
	{
		return this.message;
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
	
	public void setEventId(String eventId)
	{
		this.eventId=eventId;
	}
	
	public String getEventId()
	{
		return this.eventId;
	}
	public void setLastTime(String lasttime)
	{
		this.lastTime=lasttime;
	}
	
	public String getLastTime()
	{
		return this.lastTime;
	}
	
	public void setIsAccept(String isAccept)
	{
		this.isAccept=isAccept;
	}
	
	public String getIsAccept()
	{
		return this.isAccept;
	}
	
	public Track()
	{
		
	}

}

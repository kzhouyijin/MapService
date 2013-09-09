package zack.map.config;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * @author 周艺津
 * @version 创建时间：2012-5-5 下午02:55:29
 * 
 */
public class ToolHelp 
{
	public static String GetCurrentTime()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("",Locale.SIMPLIFIED_CHINESE);
		sdf.applyPattern("yyyyMMddHHmmss");
		String timeStr = sdf.format(new Date());
		return timeStr;
	}
	
	public static String GetTwoHoursAge()
	{
		Date date=new Date();
		long ago=date.getTime()-1000*60*60*4;
		date.setTime(ago);
		SimpleDateFormat sdf = new SimpleDateFormat("",Locale.SIMPLIFIED_CHINESE);
		sdf.applyPattern("yyyyMMddHHmmss");
		String timeStr = sdf.format(date);
		return timeStr;
		
	}
	
	public static String GetOneDayAgo()
	{
		Date date=new Date();
		long ago=date.getTime()-1000*60*60*24;
		date.setTime(ago);
		SimpleDateFormat sdf = new SimpleDateFormat("",Locale.SIMPLIFIED_CHINESE);
		sdf.applyPattern("yyyyMMddHHmmss");
		String timeStr = sdf.format(date);
		return timeStr;
		
	}

}

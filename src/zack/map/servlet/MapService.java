package zack.map.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;


import zack.bean.MapException;
import zack.map.config.EventCodType;
import zack.map.config.RequestType;
import zack.map.config.ToolHelp;
import zack.map.logic.AddFriend;
import zack.map.logic.AddFriend2;
import zack.map.logic.BussinessBase;
import zack.map.logic.ConfirmFriend;
import zack.map.logic.DeleteFriend;
import zack.map.logic.LeaveMessage;
import zack.map.logic.LoginOut;
import zack.map.logic.ModifiedConfig;
import zack.map.logic.ModifiedFriend;
import zack.map.logic.QueryEventInfo;
import zack.map.logic.QueryFriend;
import zack.map.logic.QueryGpsData;
import zack.map.logic.RegisterUser;
import zack.map.logic.SaveGpsData;

/**
 * @author 周艺津
 * @version 创建时间：2012-5-5 下午02:40:36
 * 
 */
public class MapService  extends HttpServlet  
{
	
	
	private static Logger logger = Logger.getLogger(MapService.class);
	public static String Tag="MapService";
	private Context context;
	private DataSource dataSource;
	//系统最后的参数时间
	public static long  ConfigTime;
	private static long StartTime;
	private static int count=0;
	
	//缓存已查询过的用户事件，减少数据库连接
	public static Map<String, Integer> Map=new HashMap<String, Integer>();
	@Override
	public void init() throws ServletException 
	{
		try
		{
			
			super.init();
			logger.info("Map服务已启动");
			StartTime= Calendar.getInstance().getTimeInMillis();	
			context=new InitialContext();
			dataSource=(DataSource)context.lookup("java:comp/env/jdbc/map");
			Connection connection=dataSource.getConnection();
			Statement statement=connection.createStatement();

			ResultSet resultSet=statement.executeQuery("select max(createtime) from mapconfig");
			if (resultSet != null&&resultSet.next()) 
			{
				ConfigTime=Long.parseLong(resultSet.getString(1));
			}
			resultSet.close();
			statement.close();
			connection.close();
		
		}
		catch (Exception e)
		{
			logger.error("Map服务初始化失败");
			logger.error(e.getMessage());
			// TODO: handle exception
		}
		
	}
	
	public void doGet ( HttpServletRequest request , HttpServletResponse response )  throws ServletException , IOException  
	{  
		doPost(request, response);
	}  
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String result="";

		count++;
		Connection connection = null;   
		request.setCharacterEncoding("UTF-8");
		int action = 0;
		response.setContentType("text/json;charset=utf-8");
		try 
		{
			
			String actionStr =request.getParameter("action");  
			if(actionStr!=null&&!actionStr.trim().equals(""))
			{
				action=Integer.parseInt(actionStr);
				
				BussinessBase service;
				
				String myId=request.getParameter("myid");
				logger.info("服务器收到请求,myid=="+myId+" action=="+action);
				
				if((myId!=null&&!myId.trim().equals(""))||action==1)
				{
					connection = dataSource.getConnection();  
					boolean isopen=connection.isClosed();
					//根据传入的事件代码调用相应的类
					switch (action) {
					case RequestType.RegisterUser:
						service=new RegisterUser(myId);
						break;
					case RequestType.AddFriend:
//						throw new MapException("连接服务器失败，请退出应用后重试，如果依然异常，请使用1.31版本");
						service=new AddFriend(myId);
						break;
					case RequestType.AddFriend2:
						service=new AddFriend2(myId);
						break;
					case RequestType.ConfirmFirend:
						service=new ConfirmFriend(myId);
						break;
					case RequestType.DeleteFriend:
						service=new DeleteFriend(myId);						
						break;
					case RequestType.ModifiedFriend:
						service=new ModifiedFriend(myId);
						break;
					case RequestType.QueryFriend:
						service=new QueryFriend(myId);
						break;
				
					case RequestType.UserSendGPS:
						service=new SaveGpsData(myId);
						break;
					case RequestType.UserRecvGPS:
						service=new QueryGpsData(myId);
						break;
					case RequestType.ModifiedConfig:
						service=new ModifiedConfig(myId);
						break;
					case RequestType.QueryEventInfo:
						service=new QueryEventInfo(myId);
						break;
					case RequestType.LoginOut:
						service=new LoginOut(myId);
						break;
					case RequestType.LeaveMessage:
						service=new LeaveMessage(myId);
						break;
		
					default:
						throw new Exception("错误的通信代码:"+action);
					}
					result=service.execute(request, connection);
					PrintWriter out = response.getWriter();  
					logger.info("通信成功，返回结果为:"+result);
					out.println(result);  
				}
				else
				{
					PrintWriter out = response.getWriter();  
					result=BussinessBase.ErrorJsonObject(0);
					logger.info("用户验证错误，不存在用户ID:"+result);
					out.println(result);  				
				}
			
			}
			else
			{
				PrintWriter out = response.getWriter();  
				result=BussinessBase.ErrorJsonObject(0);
				logger.info("参数验证错误，返回错误信息为:"+result);
				out.println(result);  			
			}
		}	
		catch (MapException e)
		{
			PrintWriter out = response.getWriter();  	
			result=BussinessBase.ErrorJsonObject(0,e.getMessage());
			logger.info("服务器发生异常，返回错误信息为:"+result);
			logger.error(e.getMessage());	
			out.println(result);  
		}
		catch (Exception e) 
		{
			PrintWriter out = response.getWriter();  	
			result=BussinessBase.ErrorJsonObject(0);
			logger.info("服务器发生异常，返回错误信息为:"+result);
			logger.error(e.getMessage());	
			out.println(result);  
		}
		finally
		{
			try
			{
				if(connection!=null)
				{
					if(!connection.getAutoCommit())
						connection.rollback();
					if(!connection.isClosed())
						connection.close();
				}
			}
			catch (Exception e2)
			{
				// TODO: handle exception
			}
			
			
		}
		  	 
	
	}
	
	
	public static void addEventCode(String userid,int eventcode)
	{
		//如果缓存中有该用户的事件，则修改缓存中的数据
		if(Map.containsKey(userid))
		{
			int currentcode=Map.get(userid);
			
			//如果不存在该事件
			if(!((currentcode&eventcode)==eventcode))
			{
				Map.remove(userid);
				//缓存中加上添加好友事件
				Map.put(userid, currentcode+eventcode);
			}		
		}
		else
		{
			Map.put(userid, EventCodType.RequestFriendEvent);
		}
		logger.debug("EventMap:userid=="+userid+", eventcode==="+MapService.Map.get(userid));
	}
	
	public static void removeEventCode(String userid,int eventcode)
	{
		//如果缓存中有该用户的事件，则修改缓存中的数据
		if(Map.containsKey(userid))
		{
			int currentcode=MapService.Map.get(userid);
			//如果存在该事件
			if((currentcode&eventcode)==eventcode)
			{
				MapService.Map.remove(userid);
				MapService.Map.put(userid, currentcode-eventcode);
			}
			logger.debug("EventMap:userid=="+userid+", eventcode==="+MapService.Map.get(userid));
		}
	
	}
	
	@Override
	public void destroy()
	{

		try
		{
			//获取数据库连接
			Connection connection=dataSource.getConnection();
			Statement statement=connection.createStatement();
			//删除一天前的定位数据
			String time=ToolHelp.GetOneDayAgo();
			String delLocate="delete from locate where createtime<'"+time+"'";
			String delEvent="delete from eventinfo where state=0 and createtime<'"+time+"'";
			String delTrack="delete from track where state=0 and isaccept=0";
			
			statement.execute(delLocate);
			logger.debug("delLocate=="+delLocate);
			logger.debug("delEvent=="+delEvent);
			logger.debug("delTrack=="+delTrack);
			
			statement.close();
			connection.close();
		}
		catch (Exception e)
		{
			logger.error("清除作废数据失败");
			// TODO: handle exception
		}
		long endTime= Calendar.getInstance().getTimeInMillis();
		logger.info("endtime=="+endTime+",starttime="+StartTime);
		long millions=(endTime-StartTime)/1000;
		long minute=0;
		long hour=0;
		if(millions>60)
		{
			minute=endTime%60;
			millions=millions-minute*60;
		}
		if(minute>60)
		{
			hour=minute%60;
			minute=minute-hour*60;
		}


		logger.info("服务器本次启动时间为"+hour+"小时"+minute+"分钟"+millions+"秒,共收到"+count+"次请求");
		// TODO Auto-generated method stub
		super.destroy();
	}

}

package zack.map.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;


import zack.bean.MapException;
import zack.map.config.EventCodType;
import zack.map.config.ToolHelp;
import zack.map.servlet.MapService;

import net.sf.json.JSONObject;

/**
 * @author 周艺津
 * @version 创建时间：2012-5-6 上午08:00:55
 * 
 */
public class AddFriend  extends BussinessBase
{
	public AddFriend(String myId) 
	{
		super(myId);
		logger.info(Tag+"myid="+myId);
	}

	static Logger logger=Logger.getLogger(AddFriend.class);
	private String Tag="请求添加好友:";
	@Override
	public String execute(HttpServletRequest request, Connection connection)throws Exception 
	{
		

			
		connection.setAutoCommit(false);
		String observId=this.myId;
		String userId=request.getParameter("userid");
		String name=request.getParameter("name");
		String message=request.getParameter("message");
		String icon=request.getParameter("icon");
		logger.debug(Tag+"observid="+observId+",userId="+userId+",name="+name+",message="+message+",icon="+icon);
		String timeStr=ToolHelp.GetCurrentTime();
		
		if(userId.equals(observId))
			throw new MapException("不能添加自己为好友");
		
		Statement statement=connection.createStatement();
		//先删除可能存在的作废数据
		StringBuilder sb=new StringBuilder();
		

		
		sb.append("select id from track where (userid=").append(observId).append(" and observid=").append(userId)
		.append(") or (userid=").append(userId).append(" and observid=").append(observId).append(")");
		 ResultSet resultSet= statement.executeQuery(sb.toString());
		 //如果存在记录，则直接返回正确
		 if(resultSet.next())
		 {
			 return getResutleString(initJsonObject(1), null);
		 }
		 else 
		 {
			//在跟踪表中添加好友关系
				sb.delete(0, sb.length());
				sb.append("insert into track(observid,userid,name,state,icon,message,isaccept) values (")
				.append(observId).append(",").append(userId).append(",'").append(name)
				.append("',1,'").append(icon).append("','").append(message).append("',0);");
				logger.debug(Tag+"InsertTrack=="+sb.toString());
				statement.execute(sb.toString());
				sb.delete(0, sb.length());
				
				//在事件表中插入一条添加好友的事件
				sb.append("insert into eventinfo(observid,userid,createtime,eventcode,state) values (")
				.append(observId).append(",").append(userId).append(",'").append(timeStr)
				.append("',").append(EventCodType.RequestFriendEvent).append(",1)");
				logger.debug(Tag+"InsertEvent："+sb.toString());
				statement.execute(sb.toString());
				connection.commit();			
				MapService.addEventCode(userId, EventCodType.RequestFriendEvent);
			
				CloseStatment(statement);
				CloseConnection(connection);	
				return getResutleString(initJsonObject(1), null);				
		}
	}

	@Override
	public String getResutleString(JSONObject jsonObject, Object object) 
	{
		return jsonObject.toString();
	}

	

}

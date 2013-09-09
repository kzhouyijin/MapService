package zack.map.logic;

import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import zack.map.config.EventCodType;
import zack.map.config.ToolHelp;
import zack.map.servlet.MapService;

import net.sf.json.JSONObject;

/**
 * @author 周艺津
 * @version 创建时间：2012-6-19 上午09:46:29
 * 
 */
public class LeaveMessage extends BussinessBase
{
	static Logger logger=Logger.getLogger(DeleteFriend.class);
	private String Tag="用户留言:";
	public LeaveMessage(String myId) 
	{
		super(myId);
		logger.info(Tag+"myid=="+myId);
	}
	
	
	@Override
	public String execute(HttpServletRequest request, Connection connection)throws Exception 
	{
		connection.setAutoCommit(false);
		Statement statement=connection.createStatement();
		String userid=request.getParameter("userid");
		logger.debug(Tag+"userid="+userid);
		String leaveMeaasge=request.getParameter("leavemessage");
		String timeStr=ToolHelp.GetCurrentTime();
		
		StringBuilder sb=new StringBuilder();
		
		logger.debug(Tag+"leavemessage="+leaveMeaasge);
		String sql="update track set leavemessage='"+leaveMeaasge+"' where observid="+userid+" and userid="+myId+" and isaccept=1";
		logger.debug(Tag+"SQL=="+sql);
		statement.execute(sql);
		String sql2="update track set leavemessage=''  where observid="+myId+" and userid="+userid+" and isaccept=1";
		logger.debug(Tag+"SQL2=="+sql2);
		statement.execute(sql2);
		
		//插入留言事件
		sb.append("insert into eventinfo(observid,userid,createtime,eventcode,state) values (")
		.append(myId).append(",").append(userid).append(",'").append(timeStr)
		.append("',").append(EventCodType.LeaveMessage).append(",1)");
		logger.debug(Tag+"InsertEvent："+sb.toString());
		statement.execute(sb.toString());
		
		MapService.addEventCode( userid, EventCodType.LeaveMessage);
		
		
		connection.commit();
		connection.commit();
		CloseStatment(statement);
		CloseConnection(connection);
		// TODO Auto-generated method stub
		return getResutleString(initJsonObject(1), null);
	}

	@Override
	public String getResutleString(JSONObject jsonObject, Object object) {
		// TODO Auto-generated method stub
		return jsonObject.toString();
	}

}

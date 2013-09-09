package zack.map.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import zack.bean.Track;
import zack.map.config.EventCodType;
import zack.map.config.ToolHelp;
import zack.map.servlet.MapService;
import net.sf.json.JSONObject;

/**
 * @author 周艺津
 * @version 创建时间：2012-5-10 下午11:26:57
 * 
 */
//用户退出
public class LoginOut extends BussinessBase
{
	static Logger logger=Logger.getLogger(DeleteFriend.class);
	private String Tag="用户退出系统:";
	public LoginOut(String myId) 
	{
		super(myId);
		logger.info(Tag+"myid=="+myId);
	}

	@Override
	public String execute(HttpServletRequest request, Connection connection)
			throws Exception {
		//如果缓存中有该用户的事件，则把用户的缓存数据删除
		if(MapService.Map.containsKey(myId))
		{
			MapService.Map.remove(myId);
		}
		
		String configTime=request.getParameter("configtime");
		if(configTime==null)
			configTime="20120712000000";
		StringBuilder sb=new StringBuilder();
		String timeStr=ToolHelp.GetCurrentTime();
		

		Statement statement=connection.createStatement();
		//判断是否需要更新系统参数
		if(Long.parseLong(configTime)<MapService.ConfigTime)
		{
			//在事件表中插入一条更新系统参数的事件
			sb.append("insert into eventinfo(observid,userid,createtime,eventcode,state) values (1,")
			.append(myId).append(",'").append(timeStr)
			.append("',").append(EventCodType.ModifiedConfig).append(",1)");
			logger.debug(Tag+"添加更新系统参数事件："+sb.toString());
			statement.execute(sb.toString());
		}		
		//查询出该用户所有的事件
		sb.delete(0, sb.length());
		sb.append("select sum(t.eventcode) from (select EVENTCODE from eventinfo where userid=")
		.append(myId).append(" and state=1 GROUP BY EVENTCODE) t ");
		
		logger.info(Tag+"QueryEventCode=="+sb.toString());
		
		ResultSet resultSet = statement.executeQuery(sb.toString()); 
		int eventcode=0;
		if (resultSet != null&&resultSet.next()) 
		{  
			eventcode=resultSet.getInt(1)  ;	
			MapService.Map.put(myId,eventcode);
		}
		CloseResultSet(resultSet);
		CloseStatment(statement);
		CloseConnection(connection);
		//返回成功
		return getResutleString(initJsonObject(1), null);
	}

	@Override
	public String getResutleString(JSONObject jsonObject, Object object) 
	{
		return jsonObject.toString();
	}

}

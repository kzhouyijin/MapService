package zack.map.logic;

import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

/**
 * @author 周艺津
 * @version 创建时间：2012-5-6 上午08:01:18
 * 
 */
public class ModifiedFriend extends BussinessBase {
	
	static Logger logger=Logger.getLogger(DeleteFriend.class);
	private String Tag="修改好友信息:";
	public ModifiedFriend(String myId) 
	{

		super(myId);
		logger.info(Tag+"+myId="+myId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute(HttpServletRequest request, Connection connection)throws Exception
	{
		String name=request.getParameter("name");
		String icon=request.getParameter("icon");
		String userid=request.getParameter("userid");
		logger.debug(Tag+"name="+name+",icon="+icon+",userid="+userid);
		StringBuilder sb=new StringBuilder();
		sb.append("update track set name='").append(name).append("',icon='").append(icon)
		.append("' where observid=").append(myId).append(" and userid=").append(userid).append(" and isaccept=1");
		
		Statement statement=connection.createStatement();
		logger.debug(Tag+sb.toString());
		statement.execute(sb.toString());
		
	    CloseStatment(statement);
	    CloseConnection(connection);
		
		return getResutleString(initJsonObject(1), null);
	}

	@Override
	public String getResutleString(JSONObject jsonObject, Object object) 
	{
		return jsonObject.toString();
	}

}

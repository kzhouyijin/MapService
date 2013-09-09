package zack.map.logic;

import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

/**
 * @author 周艺津
 * @version 创建时间：2012-5-6 上午08:01:08
 * 
 */
public class DeleteFriend extends BussinessBase
{
	static Logger logger=Logger.getLogger(DeleteFriend.class);
	private String Tag="删除好友:";
	public DeleteFriend(String myId) {
		super(myId);
		logger.info(Tag+"myid=="+myId);
	}

	@Override
	public String execute(HttpServletRequest request, Connection connection)throws Exception 
	{
		connection.setAutoCommit(false);
		String userid=request.getParameter("userid");
		logger.debug(Tag+"userid="+userid);
		Statement statement=connection.createStatement();
		String sql1="delete from track where observid="+myId+" and userid="+userid;
		logger.debug(Tag+"SQL1=="+sql1);
		statement.execute(sql1);
		
		String sql2="delete from track where observid="+userid+" and userid="+myId;
		logger.debug(Tag+"SQL2=="+sql2);
		statement.execute(sql2);
		
		connection.commit();
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

package zack.map.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


import org.apache.log4j.Logger;

import zack.bean.Track;
import zack.map.config.EventCodType;
import zack.map.servlet.MapService;

import net.sf.json.JSONObject;

/**
 * @author 周艺津
 * @version 创建时间：2012-5-6 上午10:22:52
 * 
 */
public class AddFriend2 extends BussinessBase
{
	public AddFriend2(String myId) 
	{
		super(myId);
		logger.debug(Tag+"myid="+myId);
		// TODO Auto-generated constructor stub
	}

	static Logger logger=Logger.getLogger(AddFriend2.class);
	private String Tag="响应添加好友:";
	@Override
	public String execute(HttpServletRequest request, Connection connection) throws Exception 
	{
		
		//对应的事件ID
		int eventId;
		StringBuilder sb=new StringBuilder();
		sb.append("select t.ID,t.OBSERVID,t.MESSAGE,e.ID as eventid,t.isaccept from track t,eventinfo e where t.USERID=")
		.append(myId).append(" and t.STATE=1 and t.USERID=e.USERID and e.STATE=1 and t.isaccept=0")
		.append(" and t.OBSERVID=e.OBSERVID and e.EVENTCODE=1 and e.STATE=1");
		
		Statement statement=connection.createStatement();
		logger.debug(Tag+"Query=="+sb.toString());
		ResultSet resultSet=statement.executeQuery(sb.toString());
	
		List<Track> tracks=new ArrayList<Track>();
		while(resultSet != null&&resultSet.next()) 
		{  
			Track track=new Track();
			track.setId(resultSet.getString(1));
			if(track.getId()==null||track.getId().equals(""))
				continue;
			track.setObservId(resultSet.getString(2));
			track.setMessage(resultSet.getString(3));	
			track.setEventId(resultSet.getString(4));
			track.setIsAccept(resultSet.getString(5));
			tracks.add(track);
		}  
		
		//如果缓存中有该用户的事件，则修改缓存中的数据
		MapService.removeEventCode(myId, EventCodType.RequestFriendEvent);
		CloseResultSet(resultSet);
		CloseStatment(statement);
		CloseConnection(connection);

		return getResutleString(initJsonObject(1), tracks);
	}

	@Override
	public String getResutleString(JSONObject jsonObject, Object object)
	{
		ArrayList<Track> tracks=(ArrayList<Track>)object;
		if(tracks.size()>0)
			jsonObject.element("data", tracks);
		// TODO Auto-generated method stub
		return jsonObject.toString();
	}
	

	
}

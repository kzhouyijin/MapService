package zack.map.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import zack.bean.Locate;
import zack.bean.Track;
import zack.map.config.ToolHelp;

import net.sf.json.JSONObject;

/**
 * @author 周艺津
 * @version 创建时间：2012-5-6 下午03:50:13
 * 
 */
public class QueryFriend extends BussinessBase
{

	static Logger logger=Logger.getLogger(QueryFriend.class);
	private String Tag="查询好友列表:";
	public QueryFriend(String myId) {
		super(myId);
		logger.info(Tag+"myid=="+myId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute(HttpServletRequest request, Connection connection)throws Exception 
	{
		StringBuilder sb=new StringBuilder();
		
		//左外连接

		//获取该用户观察的所有好友信息
//		sb.append("select t.id,max(t.createtime),t.name,t.icon,t.isaccept,t.userid from ")
//		.append("(select t.*,l.createtime from track t left outer join locate l on t.userid=l.userid) t")
//		.append(" where t.observid=").append(myId).append(" and t.isaccept=1 group by t.userid");
		sb.append("select t.id,u.last_time,t.name,t.icon,t.isaccept,t.userid from")
		.append(" track t,user u where t.observid=").append(myId).append(" and t.userid=u.id and t.isaccept=1");
		logger.debug(Tag+sb.toString());
		
		Statement statement=connection.createStatement();
		ResultSet resultSet=statement.executeQuery(sb.toString());
		
		List<Track> list=new ArrayList<Track>();
		

		while(resultSet != null&&resultSet.next()) 
		{  
			Track track=new Track();
			track.setId(resultSet.getString(1));
			if(track.getId()==null||track.getId().equals(""))
				continue;
			track.setLastTime(resultSet.getString(2));
			track.setName(resultSet.getString(3));
			track.setIcon(resultSet.getString(4));
			track.setIsAccept(resultSet.getString(5));
			track.setUserId(resultSet.getString(6));
			list.add(track);
		  //  id=resultSet.getInt(1)  ;
		}  
		CloseResultSet(resultSet);
		CloseStatment(statement);
		CloseConnection(connection);
		return getResutleString(initJsonObject(1), list);
	}

	@Override
	public String getResutleString(JSONObject jsonObject, Object object)
	{
		List<Locate> list=(List<Locate>)object;
		if(list.size()>0)
			jsonObject.element("data", list);
		return jsonObject.toString();
		
	}
	

}

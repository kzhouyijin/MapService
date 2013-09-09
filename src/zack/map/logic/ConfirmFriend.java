package zack.map.logic;

import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import zack.map.config.EventCodType;
import zack.map.servlet.MapService;

import net.sf.json.JSONObject;

/**
 * @author ���ս�
 * @version ����ʱ�䣺2012-5-6 ����10:03:12
 * 
 */
public class ConfirmFriend extends BussinessBase
{

	static Logger logger=Logger.getLogger(ConfirmFriend.class);
	private String Tag="ȷ����Ӻ���:";
	public ConfirmFriend(String myId)
	{
		super(myId);
		logger.info(Tag+"myid=="+myId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute(HttpServletRequest request, Connection connection) throws Exception 
	{
		
		connection.setAutoCommit(false);
		String eventId =request.getParameter("eventid");
		String trackId=request.getParameter("trackid");
		String name=request.getParameter("name");
		String icon=request.getParameter("icon");
		String userId=request.getParameter("userid");
		String accept=request.getParameter("accept");
		logger.debug(Tag+"eventid="+eventId+",trackid="+trackId+",name="+name+",icon="+icon+",userid="+userId+",accept="+accept);
		
		StringBuilder sb=new StringBuilder();
		
		Statement statement=connection.createStatement();
		//���ú��ѹ�ϵΪ��Ч
	
		
		//���ͬ�⣬����ӶԷ�Ϊ����
		if(accept.equals("1"))
		{
			String updateSql1="update track set state=0,isaccept=1 where id="+trackId;
			logger.debug(Tag+"UpdateTrack=="+updateSql1);
			statement.executeUpdate(updateSql1);
			
				
			sb.append("insert into track(observid,userid,name,state,icon,message,isaccept) values (")
			.append(myId).append(",").append(userId).append(",'").append(name)
			.append("',0,'").append(icon).append("','").append("ȷ����Ӻ���").append("',1);");
			logger.debug(Tag+"InsertTrack=="+sb.toString());
			statement.execute(sb.toString());
		}
		//����ܾ�
		else
		{
			//ɾ������׷����Ϣ
			logger.debug(Tag+"DeleteTrack=="+"delete from track where id="+trackId);
			statement.execute("delete from track where id="+trackId);
			
		}
		
		//�����¼���Ч
		String updateSql2="update eventinfo set state=0  where id="+eventId;
		logger.debug(Tag+"UpdateEvent=="+updateSql2);
		statement.executeUpdate(updateSql2);
			
		connection.commit();
		CloseStatment(statement);
		CloseConnection(connection);
		MapService.removeEventCode(myId, EventCodType.RequestFriendEvent);
		
		return getResutleString(initJsonObject(1), null);
	}

	@Override
	public String getResutleString(JSONObject jsonObject, Object object) 
	{
		return jsonObject.toString();

	}
	

}

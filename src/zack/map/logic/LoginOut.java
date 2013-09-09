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
 * @author ���ս�
 * @version ����ʱ�䣺2012-5-10 ����11:26:57
 * 
 */
//�û��˳�
public class LoginOut extends BussinessBase
{
	static Logger logger=Logger.getLogger(DeleteFriend.class);
	private String Tag="�û��˳�ϵͳ:";
	public LoginOut(String myId) 
	{
		super(myId);
		logger.info(Tag+"myid=="+myId);
	}

	@Override
	public String execute(HttpServletRequest request, Connection connection)
			throws Exception {
		//����������и��û����¼�������û��Ļ�������ɾ��
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
		//�ж��Ƿ���Ҫ����ϵͳ����
		if(Long.parseLong(configTime)<MapService.ConfigTime)
		{
			//���¼����в���һ������ϵͳ�������¼�
			sb.append("insert into eventinfo(observid,userid,createtime,eventcode,state) values (1,")
			.append(myId).append(",'").append(timeStr)
			.append("',").append(EventCodType.ModifiedConfig).append(",1)");
			logger.debug(Tag+"��Ӹ���ϵͳ�����¼���"+sb.toString());
			statement.execute(sb.toString());
		}		
		//��ѯ�����û����е��¼�
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
		//���سɹ�
		return getResutleString(initJsonObject(1), null);
	}

	@Override
	public String getResutleString(JSONObject jsonObject, Object object) 
	{
		return jsonObject.toString();
	}

}

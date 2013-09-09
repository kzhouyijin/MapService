package zack.map.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import zack.map.config.EventCodType;
import zack.map.config.ToolHelp;
import zack.map.servlet.MapService;
import net.sf.json.JSONObject;

/**
 * @author ���ս�
 * @version ����ʱ�䣺2012-5-6 ����11:23:06
 * 
 */
public class QueryEventInfo extends BussinessBase {

	static Logger logger=Logger.getLogger(QueryEventInfo.class);
	private String Tag="��ѯ�û��¼�:";
	public QueryEventInfo(String myId) 
	{
		super(myId);
		logger.info(Tag+"myid=="+myId);
		// TODO Auto-generated constructor stub
	}


	@Override
	public String execute(HttpServletRequest request, Connection connection)throws Exception
	{
		
		if(MapService.Map.containsKey(myId))
		{
			return getResutleString(initJsonObject(1), MapService.Map.get(myId));
		}
		else
		{
			
			
			StringBuilder sb=new StringBuilder();
			String timeStr=ToolHelp.GetCurrentTime();
			String configTime=request.getParameter("configtime");

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
				MapService.Map.put(myId, eventcode);			
			}
			CloseResultSet(resultSet);
			CloseStatment(statement);
			CloseConnection(connection);
			//���سɹ�
			return getResutleString(initJsonObject(1), eventcode);
		}

	}

	@Override
	public String getResutleString(JSONObject jsonObject, Object object)
	{
		int eventcode=(Integer)object;
		
		jsonObject.element("eventcode", eventcode);
		// TODO Auto-generated method stub
		return jsonObject.toString();
	}
	


}

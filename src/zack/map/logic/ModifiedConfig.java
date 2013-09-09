package zack.map.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

import zack.map.config.ConfigData;
import zack.map.config.EventCodType;
import zack.map.servlet.MapService;
import net.sf.json.JSONObject;

/**
 * @author ���ս�
 * @version ����ʱ�䣺2012-5-6 ����04:31:17
 * 
 */
public class ModifiedConfig  extends BussinessBase
{

	static Logger logger=Logger.getLogger(ModifiedConfig.class);
	private String Tag="�޸�ϵͳ����:";
	
	public ModifiedConfig(String myId) 
	{
		super(myId);
		logger.info(Tag+"myid"+myId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute(HttpServletRequest request, Connection connection) throws Exception
	{
		connection.setAutoCommit(false);
		String configTime=request.getParameter("configtime");
		//��ѯ���ĵ�ϵͳ����

		Statement statement=connection.createStatement();
		
		
		//���϶�Ӧ���¼�

		StringBuilder sb=new StringBuilder();
		sb.append("update eventinfo set state=0 where observid=1 and eventcode=2 and  userid=").append(myId);
		logger.debug(Tag+"UpdateEvent=="+sb.toString());
		statement.execute(sb.toString());
		
		sb.delete(0, sb.length());
		sb.append("select configkey,configvalue,datatype from mapconfig where createtime>").append(configTime);
		logger.debug(Tag+"QueryConfig=="+sb.toString());	
		
		ResultSet resultSet=statement.executeQuery(sb.toString());
		
		List<ConfigData> datas=new ArrayList<ConfigData>();
		while (resultSet != null&&resultSet.next()) 
		{  
			ConfigData data=new ConfigData();
			data.setKey(resultSet.getString(1));
			data.setValue(resultSet.getString(2));
			data.setType(resultSet.getString(3));
			datas.add(data);
		
		}
		//����������и��޸Ĳ����¼������޸Ļ����е�����
		MapService.removeEventCode(myId, EventCodType.ModifiedConfig);
		
		connection.commit();
		CloseResultSet(resultSet);
		CloseStatment(statement);
		CloseConnection(connection);
		return getResutleString(initJsonObject(1), datas);
	}

	@Override
	public String getResutleString(JSONObject jsonObject, Object object) 
	{
		List<ConfigData> datas=(ArrayList<ConfigData>)object;
		if(datas.size()>0)
		{
			jsonObject.element("configtime", MapService.ConfigTime);
			jsonObject.element("data", datas);
		}
		return jsonObject.toString();
	}
	

}

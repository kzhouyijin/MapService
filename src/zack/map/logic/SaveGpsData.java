package zack.map.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import sun.util.logging.resources.logging;

import zack.map.config.EventCodType;
import zack.map.config.ToolHelp;
import zack.map.servlet.MapService;

import net.sf.json.JSONObject;

/**
 * @author 周艺津
 * @version 创建时间：2012-5-6 下午12:21:06
 * 
 */
public class SaveGpsData extends BussinessBase {

	static Logger logger=Logger.getLogger(SaveGpsData.class);
	private String Tag="保存GPS位置信息:";
	public SaveGpsData(String myId)
	{
		super(myId);
		logger.info(Tag+"myid="+myId);
		
	}

	@Override
	public String execute(HttpServletRequest request, Connection connection) throws Exception 
	{
		connection.setAutoCommit(false);
		String longitude=request.getParameter("longitude");
		String latitude=request.getParameter("latitude");
		String provider=request.getParameter("provider");
		String configtime=request.getParameter("configtime");
		int providerTpye=2;
		if(provider!=null&&!"".equals(provider))
		{
			provider=provider.toLowerCase();
			
			if("gps".equals(provider))
				providerTpye=0;
			else if("network".equals(provider))
				providerTpye=1;
			else if("gsm".equals(provider))
				providerTpye=2;
			else if("baidu".equals(provider))
				providerTpye=3;
		}
		
		
		
		logger.debug(Tag+"longitude="+longitude+",latitude="+latitude);
		String timeStr=ToolHelp.GetCurrentTime();
		StringBuilder sb=new StringBuilder();
		
		sb.append("insert into locate (userid,longitude,latitude,createtime,provider) values( ")
		.append(myId).append(",").append(longitude).append(",").append(latitude)
		.append(",'").append(timeStr).append("',").append(providerTpye).append(")");
		logger.debug(Tag+sb.toString());
		
		Statement statement=connection.createStatement();
		statement.execute(sb.toString());
		//修改用户最后时间
		sb.delete(0, sb.length());
		sb.append("update user set last_time='").append(ToolHelp.GetCurrentTime()).append("' where id=").append(myId);
		statement.execute(sb.toString());
		
		connection.commit();
		
		int eventcode=MapService.Map.get(myId);
		
		CloseStatment(statement);
		CloseConnection(connection);
		return getResutleString(initJsonObject(1), eventcode);
	}

	@Override
	public String getResutleString(JSONObject jsonObject, Object object)
	{
		jsonObject.put("eventcode", (Integer)object);
		return jsonObject.toString();
	}
	
	

}

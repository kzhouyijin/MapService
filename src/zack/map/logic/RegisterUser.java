package zack.map.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;
import net.sf.json.JSONString;
import net.sf.json.util.JSONStringer;

import org.apache.log4j.Logger;

import zack.map.config.ToolHelp;
import zack.map.servlet.TestServlet;

/**
 * @author 周艺津
 * @version 创建时间：2012-5-5 下午02:29:33
 * 
 */
public class RegisterUser  extends BussinessBase
{
	public RegisterUser(String myId) 
	{
		super(myId);
		logger.info(Tag+"myId=="+myId);
	}


	static Logger logger=Logger.getLogger(RegisterUser.class);
	private String Tag="注册用户:";
	
	public  String execute(HttpServletRequest request,Connection connection) throws Exception
	{
		connection.setAutoCommit(true);
		
		String mac=request.getParameter("mac");
		String phone=request.getParameter("phone");
		
		String timeStr=ToolHelp.GetCurrentTime();
		StringBuilder sb=new StringBuilder();
		sb.append("insert into user(mac_address,create_time,last_time,phone_number) values ('");
		sb.append(mac);
		sb.append("','");
		sb.append(timeStr);
		sb.append("','").append(timeStr).append("','").append(phone).append("')");
		logger.debug(Tag+"InsertUser="+sb.toString());
		
		PreparedStatement statement=connection.prepareStatement(sb.toString(),Statement.RETURN_GENERATED_KEYS);
		statement.execute();
		ResultSet resultSet = statement.getGeneratedKeys();  
		int id=0;//保存生成的ID  
		if (resultSet != null&&resultSet.next()) 
		{  
		    id=resultSet.getInt(1)  ;
		}  
		CloseResultSet(resultSet);
		CloseStatment(statement);
		CloseConnection(connection);
	
		String result=getResutleString(initJsonObject(1), id);
		
		return result;
		
	}
	
	
	@Override
	public String getResutleString(JSONObject jsonObject, Object object) 
	{
		int id=(Integer)object;
		jsonObject.element("myid", id);
		jsonObject.element("time", ToolHelp.GetCurrentTime());
		return jsonObject.toString();
	}

}

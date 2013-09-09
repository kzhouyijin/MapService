package zack.map.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

/**
 * @author 周艺津
 * @version 创建时间：2012-5-5 下午02:30:29
 * 
 */
public abstract class BussinessBase 
{
	static Logger logger=Logger.getLogger(BussinessBase.class);
	protected String myId;
	
	public  BussinessBase(String myId)
	{
		this.myId=myId;
	}
	
	public abstract String execute(HttpServletRequest request,Connection connection) throws Exception;
	
	public void CloseConnection(Connection connection) throws SQLException
	{
		if(connection != null)
			connection.close();
	}
	
	public void CloseStatment(PreparedStatement statement) throws SQLException
	{
		if(statement != null)
			statement.close();
	}
	
	public void CloseStatment(Statement statement) throws SQLException
	{
		if(statement != null)
			statement.close();
	}
	
	public void CloseResultSet(ResultSet resultSet) throws SQLException
	{
		if(resultSet != null)
			resultSet.close();
	}

	public  JSONObject initJsonObject(int code)
	{
		JSONObject jsonObject=new JSONObject();
		jsonObject.element("code", code);
		return jsonObject;
		
	}
	
	public abstract String getResutleString(JSONObject jsonObject,Object object);
	
	public static String ErrorJsonObject(int code)
	{
		JSONObject jsonObject=new JSONObject();
		jsonObject.element("code", code);
		return jsonObject.toString();
	}

	public static String ErrorJsonObject(int code,String message)
	{
		JSONObject jsonObject=new JSONObject();
		jsonObject.element("code", code);
		jsonObject.element("error", message);
		return jsonObject.toString();
	}
}

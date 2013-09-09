package zack.map.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

/**
 * @author 周艺津
 * @version 创建时间：2012-5-3 下午08:24:48
 * 
 */
public class TestServlet extends HttpServlet  
{
	private Logger logger = Logger.getLogger(TestServlet.class);
	
	private int a=1;
	public void doGet ( HttpServletRequest request , HttpServletResponse response )  throws ServletException , IOException  
	{  
		doPost(request, response);
	}  
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		try 
		{
			
		
        String mac =request.getParameter("mac");  
		logger.info(mac);
		Context context=new InitialContext();
		logger.info("test1");
		DataSource dataSource=(DataSource)context.lookup("java:comp/env/jdbc/map");
		logger.info("test2");
		Connection conn = dataSource.getConnection();   
		  
		Statement stmt = conn.createStatement();   
		
	
		  
		     //提示：users必须是数据库已有的表，   
		  
		//这里的数据库前文提及的Data Source URL配置里包含的数据库。   
		  
		   String strSql = " select * from user";   
		  
		   ResultSet rs = stmt.executeQuery(strSql);   
		  
		   while(rs.next()){   
		  
		      logger.info(rs.getString(1));                  
		  
		     }   
		  
		
	
		PrintWriter out = response.getWriter();  
		out.println("xxxxxx");  
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			// TODO: handle exception
		}

		
	}
	
	
	

	

}

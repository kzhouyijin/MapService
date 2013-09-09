package zack.map.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import zack.bean.Locate;
import zack.map.config.EventCodType;
import zack.map.config.ToolHelp;
import zack.map.servlet.MapService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author 周艺津
 * @version 创建时间：2012-5-6 下午12:21:16
 * 
 */
public class QueryGpsData extends BussinessBase
{
	static Logger logger=Logger.getLogger(QueryGpsData.class);
	private String Tag="获取GPS位置:";
	public QueryGpsData(String myId) {
		super(myId);
		logger.info(Tag+"myid=="+myId);
	}

	@Override
	public String execute(HttpServletRequest request, Connection connection)throws Exception 
	{
		StringBuilder sb=new StringBuilder();
		String ago=ToolHelp.GetTwoHoursAge();
		
		//select l.*,max(l.CREATETIME),t.`NAME`,t.ICON from locate l,track t
//		 where l.createtime>'20120506133311' and l.userid in(select userid from track where observid=9 and state=1) and l.USERID=t.USERID and t.OBSERVID=9;

		//获取该用户观察的所有好友的位置(默认查询十小时内最新的一条数据)
		sb.append("select * from (").append("select l.*,t.name,t.icon,t.leavemessage from locate l,track t ")
		.append("where l.createtime>'").append(ago).append("' and t.isaccept=1 ")
		.append(" and l.userid=t.userid and t.observid=").append(myId).append(" order by l.createtime desc) p")
		.append(" group by p.userid");
		logger.debug(Tag+sb.toString());
		
		Statement statement=connection.createStatement();
		ResultSet resultSet=statement.executeQuery(sb.toString());
		

		
		List<Locate> list=new ArrayList<Locate>();
		

		while(resultSet != null&&resultSet.next()) 
		{
			Locate locate=new Locate();
			locate.setId(resultSet.getString(1));
			if(locate.getId()==null||locate.getId().equals(""))
				continue;
			locate.setUserId(resultSet.getString(2));
			locate.setLongtitude(resultSet.getString(3));
			locate.setLatitude(resultSet.getString(4));
			locate.setCreateTime(resultSet.getString(5));
			locate.setProvider(resultSet.getString(6));
			locate.setName(resultSet.getString(7));
			locate.setIcon(resultSet.getString(8));
			locate.setLeaveMessage(resultSet.getString(9));
			list.add(locate);
		  //  id=resultSet.getInt(1)  ;
		}  
		
		//删除留言事件
		sb.delete(0, sb.length());
		sb.append("update eventinfo set state='0' where userid=").append(myId)
		.append(" and eventcode=4 and state=1");
		logger.debug(Tag+sb.toString());
		statement.execute(sb.toString());
		MapService.removeEventCode(myId, EventCodType.LeaveMessage);
		
		
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

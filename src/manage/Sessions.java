package manage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.Month;
import java.time.ZoneOffset;

import dbComponent.DBComponent;
import dbComponent.Pool;
import helper.Query;

public class Sessions {
	
	
	public void giveSession(String constraint) throws SQLException
	{
		DBComponent conn = Pool.getDBInstance();
		ResultSet rs; 
		if(constraint.contains("@"))
		{
			rs = conn.exeQueryRS("select.where.email_users", new Object[] {constraint});
		}
		else
		{
			rs =  conn.exeQueryRS("select.where.name_users", new Object[] {constraint});
		}
		int id = 0;
		while(rs.next()) {
			id = rs.getInt("id_users");
		}
		conn.exeSimple(new Query("insert.sessions", new Object[] {id, Instant.now().toString()}));
		Pool.returnDBInstance(conn);
		
	}
	
	public void killSession(String constraint) throws SQLException
	{
		DBComponent conn = Pool.getDBInstance();
		ResultSet rs; 
		if(constraint.contains("@"))
		{
			rs = conn.exeQueryRS("select.where.email_users", new Object[] {constraint});
		}
		else
		{
			rs =  conn.exeQueryRS("select.where.name_users", new Object[] {constraint});
		}
		int id = 0;
		while(rs.next()) {
			id = rs.getInt("id_users");
		}
		conn.exeSimple(new Query("delete.sessions.where.id_users", new Object[] {id}));
		Pool.returnDBInstance(conn);
		
	}
	
	
	public boolean isHaveSession(String constraint) throws SQLException
	{
		DBComponent conn = Pool.getDBInstance();
		ResultSet rs; 
		if(constraint.contains("@"))
		{
			rs = conn.exeQueryRS("select.where.email_users", new Object[] {constraint});
		}
		else
		{
			rs =  conn.exeQueryRS("select.where.name_users", new Object[] {constraint});
		}
		if(rs == null)
			return false;
		String id = null;
		while(rs.next()) {
			id = rs.getString("id_users");
		}
		if (id == null)
			return false;
		rs = conn.exeQueryRS("select.sessions.where.id_users", new Object[] {Integer.parseInt(id)});
		String time = null;
		while(rs.next()) {
			time = rs.getString("date_time");
		}
		Pool.returnDBInstance(conn);
		if(time == null)
		{
			return false;
		}
		else
		{
			Instant date = Instant.parse(time);
			int year_one = date.atOffset(ZoneOffset.UTC).getYear();
			int year_two = Instant.now().atOffset(ZoneOffset.UTC).getYear();
			Month month_one = date.atOffset(ZoneOffset.UTC).getMonth();
			Month month_two = Instant.now().atOffset(ZoneOffset.UTC).getMonth();
			if(year_one == year_two && month_one == month_two)
			{
				int day_one = date.atOffset(ZoneOffset.UTC).getDayOfMonth();
				int day_two = Instant.now().atOffset(ZoneOffset.UTC).getDayOfMonth();
				if(day_two - day_one >= 1)
				{
					killSession(constraint);
					return false;
				}
				else
				{
					return true;
				}
			}
			else
			{
				killSession(constraint);
				return false;
			}
		}
	}
}

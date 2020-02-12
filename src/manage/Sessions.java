package manage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.Month;
import java.time.ZoneOffset;

import resources.Database;
import resources.Pool;

public class Sessions {
	
	
	public void giveSession(String constraint) throws SQLException
	{
		Database ConnectionSession = Pool.getInstance();
		ResultSet rs;
		if(constraint.contains("@"))
		{
			rs = ConnectionSession.SelectWhereConstraintUnique("users", "email_users", constraint);
		}
		else
		{
			rs = ConnectionSession.SelectWhereConstraintUnique("users", "name_users", constraint);
		}
		int id = 0;
		while(rs.next()) {
			id = rs.getInt("id_users");
		}
		String SQL = "INSERT INTO sessions (id_users, date_time) VALUES ";
		SQL += "("+id+",'"+Instant.now().toString()+"');";
		ConnectionSession.Insert(SQL);
		Pool.giveInstance();
		
	}
	
	public void killSession(String constraint) throws SQLException
	{
		Database ConnectionSession = Pool.getInstance();
		ResultSet rs;
		if(constraint.contains("@"))
		{
			rs = ConnectionSession.SelectWhereConstraintUnique("users", "email_users", constraint);
		}
		else
		{
			rs = ConnectionSession.SelectWhereConstraintUnique("users", "name_users", constraint);
		}
		int id = 0;
		while(rs.next()) {
			id = rs.getInt("id_users");
		}
		String SQL = "DELETE FROM sessions WHERE id_users="+id+";";
		ConnectionSession.Delete(SQL);
		Pool.giveInstance();
		
	}
	
	
	public boolean is_have_session(String constraint) throws SQLException
	{
		Database ConnectionSession = Pool.getInstance();
		ResultSet rs;
		if(constraint.contains("@"))
		{
			rs = ConnectionSession.SelectWhereConstraintUnique("users", "email_users", constraint);
		}
		else
		{
			rs = ConnectionSession.SelectWhereConstraintUnique("users", "name_users", constraint);
		}
		if(rs == null)
			return false;
		String id = null;
		while(rs.next()) {
			id = rs.getString("id_users");
		}
		if (id == null)
			return false;
		rs = ConnectionSession.SelectWhereConstraintUnique("sessions", "id_users", id);
		String time = null;
		while(rs.next()) {
			time = rs.getString("date_time");
		}
		Pool.giveInstance();
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

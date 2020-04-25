package manage;

import java.sql.ResultSet;
import java.sql.SQLException;

import dbComponent.DBComponent;
import dbComponent.Pool;

public class FindID {
	
	
	public String returnIDbyConstraint(String constraint) throws SQLException {
		DBComponent conn = Pool.getDBInstance();
		ResultSet rs;
		if(constraint.contains("@"))
		{
			rs = conn.exeQueryRS("select.where.email_users", new Object[]{constraint});
		}
		else
		{
			rs = conn.exeQueryRS("select.where.name_users", new Object[]{constraint});
		}
		Pool.returnDBInstance(conn);
		String id = null;
		while(rs.next()) {
			id = rs.getString("id_users");
		}
		return id;
	}
	
	public String returnConstraintbyID(String id) throws SQLException {
		DBComponent conn = Pool.getDBInstance();
		ResultSet rs;
		rs = conn.exeQueryRS("select.where.id_users", new Object[]{Integer.parseInt(id)});
		Pool.returnDBInstance(conn);
		String constraint = null;
		while(rs.next()) {
			constraint = rs.getString("name_users");
		}
		return constraint;
	}

}

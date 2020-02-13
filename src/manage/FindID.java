package manage;

import java.sql.ResultSet;
import java.sql.SQLException;

import resources.Database;
import resources.Pool;

public class FindID {
	
	
	public String ReturnIDbyConstraint(String constraint) throws SQLException {
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
		Pool.giveInstance();
		String id = null;
		while(rs.next()) {
			id = rs.getString("id_users");
		}
		return id;
	}
	
	public String ReturnConstraintbyID(String id) throws SQLException {
		Database ConnectionSession = Pool.getInstance();
		ResultSet rs;
		rs = ConnectionSession.SelectWhereConstraintUnique("users", "id_users", id);
		Pool.giveInstance();
		String constraint = null;
		while(rs.next()) {
			constraint = rs.getString("name_users");
		}
		return constraint;
	}

}

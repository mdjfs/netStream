package manage;

import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

import aux.HashPassword;
import resources.Database;
import resources.Pool;

public class Auth {
	private HashPassword check_hash = new HashPassword();
	
	
	public boolean[] is_user_validate(String constraint, String password) throws SQLException, NoSuchAlgorithmException, NullPointerException
	{
		Database ConnectionChecker = Pool.getInstance();
		ResultSet rs;
		if(constraint.contains("@"))
		{
			rs = ConnectionChecker.SelectWhereConstraintUnique("users", "email_users", constraint);
		}
		else
		{
			rs = ConnectionChecker.SelectWhereConstraintUnique("users", "name_users", constraint);
		}
		Pool.giveInstance();
		while(rs.next()) {
			
			return new boolean[]{rs.getString("name_users") != null, 
					check_hash.ToHashPassword(password).equals(rs.getString("password_users"))};
		}
		return new boolean[] {false, false};
	}

}

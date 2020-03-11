package manage;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONObject;

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
			rs = ConnectionChecker.selectWhereConstraintUnique("users", "email_users", constraint);
		}
		else
		{
			rs = ConnectionChecker.selectWhereConstraintUnique("users", "name_users", constraint);
		}
		Pool.giveInstance();
		while(rs.next()) {
			
			return new boolean[]{rs.getString("name_users") != null, 
					check_hash.ToHashPassword(password).equals(rs.getString("password_users"))};
		}
		return new boolean[] {false, false};
	}
	
}

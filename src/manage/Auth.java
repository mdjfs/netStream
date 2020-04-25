package manage;

import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

import helper.HashPassword;
import dbComponent.DBComponent;
import dbComponent.Pool;

/*
 * la clase Auth contiene metodos para validar un usuario
 */


public class Auth {
	private HashPassword check_hash = new HashPassword();
	

	/*
	 * Este metodo recibe el username/email y el password que envia el usuario
	 * y verifica si el pass es el mismo
	 * @param constraint es el username/email
	 */
	public boolean[] isUserValidate(String constraint, String password) throws SQLException, NoSuchAlgorithmException, NullPointerException
	{
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
		while(rs.next()) {
			
			return new boolean[]{rs.getString("name_users") != null, 
					check_hash.ToHashPassword(password).equals(rs.getString("password_users"))};
		}
		return new boolean[] {false, false};
	}
	
}

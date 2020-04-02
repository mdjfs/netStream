package manage;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.Instant;

import org.json.simple.JSONObject;

import dbComponent.DBComponent;
import dbComponent.Pool;
import helper.HashPassword;
import helper.JSONManage;
import helper.Query;
import helper.Sanitize;

public class RegisterController {

	JSONObject output = new JSONObject();
	JSONManage messages = new JSONManage();
	HashPassword hashing = new HashPassword();
	Sanitize sanitize = new Sanitize();
	
	public RegisterController() 
	{
		
	}
	
	public JSONObject setRegister(JSONObject json) 
	{
		String name = json.get("name").toString();
		String email = json.get("email").toString();
		if(json.get("password").toString().length() != 0 && name.length() != 0 && email.length() != 0)
		{
			if(email.contains("@") && email.contains(".")) {
				if(!(sanitize.isSanitize(name) && sanitize.isSanitize(email)))
				{
					String message = "Your username or email fails the requeriments";
					return messages.reportErrorMessage(message);
				}
				try 
				{
					String hashed = hashing.ToHashPassword(json.get("password").toString());
					DBComponent conn = Pool.getDBInstance();
					try 
					{
						conn.exeSimple(new Query("insert.users", new Object[] {name, hashed, email, Instant.now().toString()}));
						return messages.reportSuccessMessage("You are Registered");
					} 
					catch (SQLException e) 
					{
						e.printStackTrace();
						return messages.reportErrorMessage(e.getMessage());
					}
					finally
					{
						Pool.returnDBInstance(conn);
					}
				} 
				catch (NoSuchAlgorithmException e1) 
				{
					e1.printStackTrace();
					return messages.reportErrorMessage(e1.getMessage());
				}
			}
			else {
				return messages.reportErrorMessage("Check your email");
			}
		}
		else
		{
			return messages.reportErrorMessage("Please, insert data");
		}
		
	}
}

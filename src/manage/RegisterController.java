package manage;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.Instant;

import org.json.simple.JSONObject;

import aux.HashPassword;
import aux.JSONManage;
import aux.Sanitize;
import resources.Pool;
import resources.Database;

public class RegisterController {

	JSONObject output_json = new JSONObject();
	JSONManage messages_register = new JSONManage();
	HashPassword hashing_register = new HashPassword();
	Sanitize sanitize_register = new Sanitize();
	
	public RegisterController() 
	{
		
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject setRegister(JSONObject input_json) 
	{
		if(!(sanitize_register.is_sanitize(input_json.get("name").toString()) &&
				sanitize_register.is_sanitize(input_json.get("email").toString())))
		{
			String message = "Your username or email fails the requeriments";
			return messages_register.reportErrorMessage(message);
		}
		try 
		{
			String ProccessAfterHashing = hashing_register.ToHashPassword(input_json.get("password").toString());
			Database ConnectionRegister = Pool.getInstance();
			String SQL = "INSERT INTO users (name_users, password_users, email_users, date_creation_users) ";
			SQL += "VALUES ('"+input_json.get("name").toString()+"','"+ProccessAfterHashing+"','"+input_json.get("email").toString();
			SQL += "','"+Instant.now().toString()+"');";
			try 
			{
				ConnectionRegister.insert(SQL);
				Pool.giveInstance();
				return messages_register.reportSuccessMessage("You are Registered");
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
				Pool.giveInstance();
				return messages_register.reportErrorMessage(e.getMessage());
			}
		} 
		catch (NoSuchAlgorithmException e1) 
		{
			e1.printStackTrace();
			return messages_register.reportErrorMessage(e1.getMessage());
		}
		
	}
}

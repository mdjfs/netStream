package manage;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.Instant;

import org.json.simple.JSONObject;

import aux.HashPassword;
import aux.ReportExceptions;
import aux.Sanitize;
import resources.Pool;
import resources.Database;

public class Register {
	
	public Register() 
	{
		
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject SetRegister(JSONObject input_json) 
	{
		JSONObject output_json = new JSONObject();
		ReportExceptions have_exceptions_register = new ReportExceptions();
		HashPassword hashing_register = new HashPassword();
		Sanitize sanitize_register = new Sanitize();
		if(!(sanitize_register.is_sanitize(input_json.get("name").toString()) &&
				sanitize_register.is_sanitize(input_json.get("email").toString())))
		{
			String message = "Your username or email fails the requeriments";
			return have_exceptions_register.ReportErrorMessage(message);
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
				ConnectionRegister.Insert(SQL);
				output_json.put("results", "Success");
				output_json.put("message", "You are Registered");
				output_json.put("status", "200");
				Pool.giveInstance();
				return output_json;
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
				Pool.giveInstance();
				return have_exceptions_register.ReportErrorMessage(e.getMessage());
			}
		} 
		catch (NoSuchAlgorithmException e1) 
		{
			e1.printStackTrace();
			return have_exceptions_register.ReportErrorMessage(e1.getMessage());
		}
		
	}
}

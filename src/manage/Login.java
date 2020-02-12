package manage;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import org.json.simple.JSONObject;

import aux.ReportExceptions;
import resources.Pool;

public class Login {
	private Auth checker_login = new Auth();
	private ReportExceptions exceptions_login = new ReportExceptions();
	private Sessions sessions_login = new Sessions();
	
	
	@SuppressWarnings("unchecked")
	public JSONObject SetLogin(JSONObject input_json) {
		JSONObject output_json = new JSONObject();
		try 
		{
			boolean[] status = checker_login.is_user_validate(input_json.get("constraint").toString(), 
																input_json.get("password").toString());
			if(status[0] && status[1])
			{
				output_json.put("results", "Success");
				output_json.put("message", "You are Log-In");
				output_json.put("status", "200");
				if(!sessions_login.is_have_session(input_json.get("constraint").toString()))
					sessions_login.giveSession(input_json.get("constraint").toString());
				return output_json;
			}
			if(!status[0])
			{
				String message = "The user dont exists, you arent registered ?";
				return exceptions_login.ReportErrorMessage(message);
			}
			if(!status[1])
			{
				String message = "The password is wrong !";
				return exceptions_login.ReportErrorMessage(message);
			}
			String message = "You find a easter egg.";
			return exceptions_login.ReportErrorMessage(message);
			
		} 
		catch (NullPointerException | SQLException | NoSuchAlgorithmException e) 
		{
			Pool.giveInstance();
			e.printStackTrace();
			return exceptions_login.ReportErrorMessage(e.getMessage());
		}
		
		
	}

}

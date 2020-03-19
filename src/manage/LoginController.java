package manage;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import org.json.simple.JSONObject;

import helper.JSONManage;
import resources.Pool;

public class LoginController {
	private Auth checker_login = new Auth();
	private JSONManage messages_login = new JSONManage();
	private Sessions sessions_login = new Sessions();
	
	
	public JSONObject setLogin(JSONObject input_json) {
		try 
		{
			boolean[] status = checker_login.is_user_validate(input_json.get("constraint").toString(), 
																input_json.get("password").toString());
			if(status[0] && status[1])
			{
				if(!sessions_login.is_have_session(input_json.get("constraint").toString()))
					sessions_login.giveSession(input_json.get("constraint").toString());
				return messages_login.reportSuccessMessage("You are Log-In");
			}
			if(!status[0])
			{
				String message = "The user dont exists, you arent registered ?";
				return messages_login.reportErrorMessage(message);
			}
			if(!status[1])
			{
				String message = "The password is wrong !";
				return messages_login.reportErrorMessage(message);
			}
			String message = "You find a easter egg.";
			return messages_login.reportErrorMessage(message);
			
		} 
		catch (NullPointerException | SQLException | NoSuchAlgorithmException e) 
		{
			Pool.giveInstance();
			e.printStackTrace();
			return messages_login.reportErrorMessage(e.getMessage());
		}
		
		
	}

}

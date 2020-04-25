package manage;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import org.json.simple.JSONObject;

import helper.JSONManage;

public class LoginController {
	private Auth check = new Auth();
	private JSONManage messages = new JSONManage();
	private Sessions sessions = new Sessions();
	
	
	public JSONObject setLogin(JSONObject input_json) {
		try 
		{
			boolean[] status = check.isUserValidate(input_json.get("constraint").toString(), 
																input_json.get("password").toString());
			if(status[0] && status[1])
			{
				if(!sessions.isHaveSession(input_json.get("constraint").toString()))
					sessions.giveSession(input_json.get("constraint").toString());
				return messages.reportSuccessMessage("You are Log-In");
			}
			if(!status[0])
			{
				String message = "The user dont exists, you arent registered ?";
				return messages.reportErrorMessage(message);
			}
			if(!status[1])
			{
				String message = "The password is wrong !";
				return messages.reportErrorMessage(message);
			}
			String message = "You find a easter egg.";
			return messages.reportErrorMessage(message);
			
		} 
		catch (NullPointerException | SQLException | NoSuchAlgorithmException e) 
		{
			e.printStackTrace();
			return messages.reportErrorMessage(e.getMessage());
		}
		
		
	}

}

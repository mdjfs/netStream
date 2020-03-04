package manage;

import java.sql.SQLException;

import org.json.simple.JSONObject;

import aux.JSONMessages;
import resources.Pool;

public class LogoutController {
	
	Sessions sessions_logout = new Sessions();
	JSONMessages messages_logout = new JSONMessages();
	
	
	@SuppressWarnings("unchecked")
	public JSONObject setLogout(JSONObject input_json) 
	{
		JSONObject output_json = new JSONObject();
		try 
		{
			if(sessions_logout.is_have_session(input_json.get("constraint").toString()))
			{
				sessions_logout.killSession(input_json.get("constraint").toString());
				return messages_logout.reportSuccessMessage("You are Log-Out");
			}
			else
			{
				return messages_logout.reportErrorMessage("This session dont exist");
			}
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			Pool.giveInstance();
			return messages_logout.reportErrorMessage(e.getMessage());
		}
		
	}

}

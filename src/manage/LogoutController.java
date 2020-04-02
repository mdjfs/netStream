package manage;

import java.sql.SQLException;

import org.json.simple.JSONObject;

import helper.JSONManage;

public class LogoutController {
	
	Sessions sessions = new Sessions();
	JSONManage messages = new JSONManage();
	
	
	public JSONObject setLogout(String id) 
	{
		FindID Logout_ID = new FindID();
		String constraint;
		try {
			constraint = Logout_ID.returnConstraintbyID(id);
		} 
		catch (SQLException e1) {
			e1.printStackTrace();
			return messages.reportErrorMessage(e1.getMessage());
		}
		try 
		{
			if(sessions.isHaveSession(constraint))
			{
				sessions.killSession(constraint);
				return messages.reportSuccessMessage("You are Log-Out");
			}
			else
			{
				return messages.reportErrorMessage("This session dont exist");
			}
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return messages.reportErrorMessage(e.getMessage());
		}
		
	}

}

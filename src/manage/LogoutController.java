package manage;

import java.sql.SQLException;

import org.json.simple.JSONObject;

import aux.JSONMessages;
import resources.Pool;

public class LogoutController {
	
	Sessions sessions_logout = new Sessions();
	JSONMessages messages_logout = new JSONMessages();
	
	
	@SuppressWarnings("unchecked")
	public JSONObject setLogout(String id) 
	{
		FindID Logout_ID = new FindID();
		String constraint;
		try {
			constraint = Logout_ID.returnConstraintbyID(id);
		} 
		catch (SQLException e1) {
			e1.printStackTrace();
			return messages_logout.reportErrorMessage(e1.getMessage());
		}
		try 
		{
			if(sessions_logout.is_have_session(constraint))
			{
				sessions_logout.killSession(constraint);
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

package manage;

import java.sql.SQLException;

import org.json.simple.JSONObject;

import aux.ReportExceptions;

public class Logout {
	
	Sessions sessions_logout = new Sessions();
	ReportExceptions exceptions_logout = new ReportExceptions();
	
	
	@SuppressWarnings("unchecked")
	public JSONObject SetLogout(JSONObject input_json) 
	{
		JSONObject output_json = new JSONObject();
		try 
		{
			sessions_logout.killSession(input_json.get("constraint").toString());
			output_json.put("results", "Success");
			output_json.put("message", "You are Log-Out");
			output_json.put("status", "200");
			return output_json;
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return exceptions_logout.ReportErrorMessage(e.getMessage());
		}
		
	}

}
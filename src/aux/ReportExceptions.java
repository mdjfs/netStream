package aux;

import org.json.simple.JSONObject;

public class ReportExceptions {
	
	@SuppressWarnings("unchecked")
	public JSONObject ReportErrorMessage(String message) {
		JSONObject error_json = new JSONObject();
		error_json.put("results", "error");
		error_json.put("message", message);
		error_json.put("status", "500");
		return error_json;
	}

}

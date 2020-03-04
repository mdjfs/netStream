package aux;

import org.json.simple.JSONObject;

public class JSONMessages {
	
	@SuppressWarnings("unchecked")
	public JSONObject reportErrorMessage(String message) {
		JSONObject error_json = new JSONObject();
		error_json.put("results", "error");
		error_json.put("message", message);
		error_json.put("status", "500");
		return error_json;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject reportSuccessMessage(String message) {
		JSONObject error_json = new JSONObject();
		error_json.put("results", "success");
		error_json.put("message", message);
		error_json.put("status", "200");
		return error_json;
	}

}

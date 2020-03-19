package helper;

import java.io.BufferedReader;
import java.io.IOException;

import org.json.simple.JSONObject;

public class JSONManage {
	
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
	
	public String say_keys(String[] json_keys) {
		/* metodo que devuelve el atributo con arreglo de llaves en string */
		String keys = "";
		for(int i=0; i < json_keys.length ; i++) {
			if(i == json_keys.length - 1)
				keys += json_keys[i];
			else
				keys += json_keys[i]+", ";
		}
		return keys;
	}
	
	public Boolean is_all_keys(JSONObject json_request, String[] json_keys) {
		/* metodo que verifica si todas las llaves del servlet estan en el json */
		for(int i=0; i < json_keys.length ; i++) {
			if( ! json_request.containsKey(json_keys[i]) ) {
				return false;
			}
		}
		return true;
	}

	public String readRaw(BufferedReader buffer) throws IOException {
		/* metodo que lee texto enviado en raw y lo devuelve como string */
		StringBuffer buffertext = new StringBuffer();
		BufferedReader reader = buffer;
		String line = "";
		while ((line = reader.readLine()) != null)
			buffertext.append(line);
		return buffertext.toString();
	}

}

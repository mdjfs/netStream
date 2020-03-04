package manage;

import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONObject;

import aux.HashPassword;
import aux.JSONMessages;
import aux.Sanitize;
import resources.Database;
import resources.Pool;

public class UpdateController {
	
	private Auth checker_islogin = new Auth();
	private FindID find_user = new FindID();
	private HashPassword hash_new_password = new HashPassword();
	private JSONMessages messages_update = new JSONMessages();
	private Sessions sessions_update = new Sessions();
	private Sanitize sanitize_update = new Sanitize();
	
	@SuppressWarnings("unchecked")
	public JSONObject setUpdate(JSONObject input_json) {
		JSONObject output_json = new JSONObject();
		try {
			String constraint = find_user.returnConstraintbyID(input_json.get("id").toString());
			boolean have_session = sessions_update.is_have_session(constraint);
			if(!have_session)
				return messages_update.reportErrorMessage("The user not have session");
			if(constraint==null)
				return messages_update.reportErrorMessage("Dont exists user by this id");
			boolean[] status = checker_islogin.is_user_validate(constraint, 
					input_json.get("password").toString());
			if(status[0] && status[1]) {
				String id = input_json.get("id").toString();
				String param_to_update = input_json.get("parameter").toString();
				String value_to_update = input_json.get("value").toString();
				boolean param_sanitize = sanitize_update.is_sanitize(param_to_update);
				boolean value_sanitize = sanitize_update.is_sanitize(value_to_update);
				boolean id_sanitize = sanitize_update.is_sanitize(id);
				if(param_sanitize && value_sanitize && id_sanitize) 
				{
					updateParam(param_to_update, value_to_update, id);
					return messages_update.reportSuccessMessage(param_to_update + " is update");	
				}
				else
				{
					return messages_update.reportErrorMessage("You inputs not have the requeriments");
				}
			}
			else
			{
				return messages_update.reportErrorMessage("The password is invalid !");
			}
		} catch (NoSuchAlgorithmException | NullPointerException | SQLException e) {
			Pool.giveInstance();
			e.printStackTrace();
			return messages_update.reportErrorMessage(e.getMessage());
		}
	}
	
	private void updateParam(String param, String value, String id) throws SQLException, NoSuchAlgorithmException {
		Database ConnectionUpdate = Pool.getInstance();
		if(param.equals("password_users")) 
		{
			value = hash_new_password.ToHashPassword(value);
		}
		ConnectionUpdate.update("UPDATE users SET "+param+"='"+value+"' WHERE id_users='"+id+"';");
		Pool.giveInstance();
	}
}

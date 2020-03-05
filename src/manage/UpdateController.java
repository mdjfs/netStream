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
	
	private Auth checker_login = new Auth();
	private FindID find_user = new FindID();
	private HashPassword hash_new_password = new HashPassword();
	private JSONMessages messages_update = new JSONMessages();
	private Sessions sessions_update = new Sessions();
	private Sanitize sanitize_update = new Sanitize();
	
	@SuppressWarnings("unchecked")
	public JSONObject setUpdate(String id, JSONObject json_request) {
		JSONObject output_json = new JSONObject();
		try {
			String constraint = find_user.returnConstraintbyID(id);
			boolean[] status = checker_login.is_user_validate(constraint, 
					json_request.get("password").toString());
			if(status[0] && status[1]) 
			{
				boolean have_session = sessions_update.is_have_session(constraint);
				if(!have_session)
					return messages_update.reportErrorMessage("The user not have session");
				if(constraint==null)
					return messages_update.reportErrorMessage("Dont exists user by this id");
				boolean id_sanitize = sanitize_update.is_sanitize(id);
				if(id_sanitize) 
				{
					String param_to_update = json_request.get("parameter").toString();
					String value_to_update = json_request.get("value").toString();
					updateParam(param_to_update, value_to_update, id);
					return messages_update.reportSuccessMessage(param_to_update + " is update");	
				}
				else
				{
					return messages_update.reportErrorMessage("You inputs not have the requeriments");
				}
			}
			if(!status[0])
			{
				String message = "The user dont exists, you arent registered ?";
				return messages_update.reportErrorMessage(message);
			}
			if(!status[1])
			{
				String message = "The password is wrong !";
				return messages_update.reportErrorMessage(message);
			}
			String message = "You find a easter egg.";
			return messages_update.reportErrorMessage(message);
		} 
		catch (NoSuchAlgorithmException | NullPointerException | SQLException e) {
			Pool.giveInstance();
			e.printStackTrace();
			return messages_update.reportErrorMessage(e.getMessage());
		}
	}
	
	private void updateParam(String param, String value, String id) throws SQLException, NoSuchAlgorithmException {
		Database ConnectionUpdate = Pool.getInstance();
		param += "_users";
		if(param.equals("password_users")) 
		{
			value = hash_new_password.ToHashPassword(value);
		}
		ConnectionUpdate.update("UPDATE users SET "+param+"='"+value+"' WHERE id_users='"+id+"';");
		Pool.giveInstance();
	}
}

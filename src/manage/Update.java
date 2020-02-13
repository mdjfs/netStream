package manage;

import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONObject;

import aux.HashPassword;
import aux.ReportExceptions;
import aux.Sanitize;
import resources.Database;
import resources.Pool;

public class Update {
	
	private Auth checker_islogin = new Auth();
	private FindID find_user = new FindID();
	private HashPassword hash_new_password = new HashPassword();
	private ReportExceptions except_update = new ReportExceptions();
	private Sessions sessions_update = new Sessions();
	private Sanitize sanitize_update = new Sanitize();
	
	@SuppressWarnings("unchecked")
	public JSONObject SetUpdate(JSONObject input_json) {
		JSONObject output_json = new JSONObject();
		try {
			String constraint = find_user.ReturnConstraintbyID(input_json.get("id").toString());
			boolean have_session = sessions_update.is_have_session(constraint);
			if(!have_session)
				return except_update.ReportErrorMessage("The user not have session");
			if(constraint==null)
				return except_update.ReportErrorMessage("Dont exists user by this id");
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
					UpdateParam(param_to_update, value_to_update, id);
					output_json.put("results", "Success");
					output_json.put("message", param_to_update + " is update");
					output_json.put("status", "200");
					return output_json;	
				}
				else
				{
					return except_update.ReportErrorMessage("You inputs not have the requeriments");
				}
			}
			else
			{
				return except_update.ReportErrorMessage("The password is invalid !");
			}
		} catch (NoSuchAlgorithmException | NullPointerException | SQLException e) {
			Pool.giveInstance();
			e.printStackTrace();
			return except_update.ReportErrorMessage(e.getMessage());
		}
	}
	
	private void UpdateParam(String param, String value, String id) throws SQLException, NoSuchAlgorithmException {
		Database ConnectionUpdate = Pool.getInstance();
		if(param.equals("password_users")) 
		{
			value = hash_new_password.ToHashPassword(value);
		}
		ConnectionUpdate.Update("UPDATE users SET "+param+"='"+value+"' WHERE id_users='"+id+"';");
		Pool.giveInstance();
	}
}

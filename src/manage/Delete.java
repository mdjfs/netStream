package manage;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import org.json.simple.JSONObject;

import aux.ReportExceptions;
import aux.Sanitize;
import resources.Database;
import resources.Pool;

public class Delete {
	private Auth checker_islogin = new Auth();
	private FindID find_user = new FindID();
	private ReportExceptions except_delete = new ReportExceptions();
	private Sessions sessions_delete = new Sessions();
	private Sanitize sanitize_delete = new Sanitize();
	
	@SuppressWarnings("unchecked")
	public JSONObject SetDelete(JSONObject input_json) {
		JSONObject output_json = new JSONObject();
		try {
			String constraint = find_user.ReturnConstraintbyID(input_json.get("id").toString());
			boolean have_session = sessions_delete.is_have_session(constraint);
			if(!have_session)
				return except_delete.ReportErrorMessage("The user not have session");
			if(constraint==null)
				return except_delete.ReportErrorMessage("Dont exists user by this id");
			boolean[] status = checker_islogin.is_user_validate(constraint, 
					input_json.get("password").toString());
			if(status[0] && status[1]) {
				String id = input_json.get("id").toString();
				boolean id_sanitize = sanitize_delete.is_sanitize(id);
				if(id_sanitize) 
				{
					DeleteUser(id);
					output_json.put("results", "Success");
					output_json.put("message", "user by id "+ id+ " is delete");
					output_json.put("status", "200");
					return output_json;	
				}
				else
				{
					return except_delete.ReportErrorMessage("You inputs not have the requeriments");
				}
			}
			else
			{
				return except_delete.ReportErrorMessage("The password is invalid !");
			}
		} catch (NoSuchAlgorithmException | NullPointerException | SQLException e) {
			Pool.giveInstance();
			e.printStackTrace();
			return except_delete.ReportErrorMessage(e.getMessage());
		}
	}
	
	private void DeleteUser(String id) throws SQLException {
		Database ConnectionUpdate = Pool.getInstance();
		ConnectionUpdate.Delete("DELETE FROM sessions WHERE id_users='"+id+"';");
		ConnectionUpdate.Delete("DELETE FROM users WHERE id_users='"+id+"';");
		Pool.giveInstance();
	}
}

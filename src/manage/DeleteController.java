package manage;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import org.json.simple.JSONObject;

import aux.JSONMessages;
import aux.Sanitize;
import resources.Database;
import resources.Pool;

public class DeleteController {
	private FindID find_user = new FindID();
	private JSONMessages message_delete = new JSONMessages();
	private Sessions sessions_delete = new Sessions();
	private Sanitize sanitize_delete = new Sanitize();
	private Auth checker_login = new Auth();
	
	public JSONObject setDelete(String id, JSONObject json_request) {
		try {
			
			String constraint = find_user.returnConstraintbyID(id);
			boolean[] status = checker_login.is_user_validate(constraint, 
					json_request.get("password").toString());
			if(status[0] && status[1]) 
			{
				boolean have_session = sessions_delete.is_have_session(constraint);
				if(!have_session)
					return message_delete.reportErrorMessage("The user not have session");
				if(constraint==null)
					return message_delete.reportErrorMessage("Dont exists user by this id");
				boolean id_sanitize = sanitize_delete.is_sanitize(id);
				if(id_sanitize) 
				{
					deleteUser(id);
					return message_delete.reportSuccessMessage("user by id "+ id + " is delete");	
				}
				else
				{
					return message_delete.reportErrorMessage("You inputs not have the requeriments");
				}
			}
			if(!status[0])
			{
				String message = "The user dont exists, you arent registered ?";
				return message_delete.reportErrorMessage(message);
			}
			if(!status[1])
			{
				String message = "The password is wrong !";
				return message_delete.reportErrorMessage(message);
			}
			String message = "You find a easter egg.";
			return message_delete.reportErrorMessage(message);
		} catch (NullPointerException | SQLException | NoSuchAlgorithmException e) {
			Pool.giveInstance();
			e.printStackTrace();
			return message_delete.reportErrorMessage(e.getMessage());
		}
	}
	
	private void deleteUser(String id) throws SQLException {
		Database ConnectionUpdate = Pool.getInstance();
		ConnectionUpdate.delete("DELETE FROM sessions WHERE id_users='"+id+"';");
		ConnectionUpdate.delete("DELETE FROM users WHERE id_users='"+id+"';");
		Pool.giveInstance();
	}
}

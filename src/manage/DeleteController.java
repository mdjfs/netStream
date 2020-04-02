package manage;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.simple.JSONObject;

import helper.JSONManage;
import helper.Query;
import helper.Sanitize;
import dbComponent.DBComponent;
import dbComponent.Pool;

public class DeleteController {
	private FindID find = new FindID();
	private JSONManage message = new JSONManage();
	private Sessions sessions = new Sessions();
	private Sanitize sanitize = new Sanitize();
	private Auth check = new Auth();
	
	public JSONObject setDelete(String id, JSONObject json_request) {
		try {
			
			String constraint = find.returnConstraintbyID(id);
			boolean[] status = check.isUserValidate(constraint, 
					json_request.get("password").toString());
			if(status[0] && status[1]) 
			{
				boolean have_session = sessions.isHaveSession(constraint);
				if(!have_session)
					return message.reportErrorMessage("The user not have session");
				if(constraint==null)
					return message.reportErrorMessage("Dont exists user by this id");
				boolean id_sanitize = sanitize.isSanitize(id);
				if(id_sanitize) 
				{
					deleteUser(id);
					return message.reportSuccessMessage("user by id "+ id + " is delete");	
				}
				else
				{
					return message.reportErrorMessage("You inputs not have the requeriments");
				}
			}
			if(!status[0])
			{
				return message.reportErrorMessage("The user dont exists, you arent registered ?");
			}
			if(!status[1])
			{
				return message.reportErrorMessage("The password is wrong !");
			}
			return message.reportErrorMessage("You find a easter egg.");
		} catch (NullPointerException | SQLException | NoSuchAlgorithmException e) {
			e.printStackTrace();
			return message.reportErrorMessage(e.getMessage());
		}
	}
	
	private void deleteUser(String id) throws SQLException {
		DBComponent conn = Pool.getDBInstance();
		ArrayList<Query> array = new ArrayList<Query>();
		array.add(new Query("delete.sessions.where.id_users", new Object[] {id}));
		array.add(new Query("delete.where.id_users", new Object[] {Integer.parseInt(id)}));
		conn.exeBatch(array);
		Pool.returnDBInstance(conn);
	}
}

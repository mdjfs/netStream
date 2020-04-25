package manage;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONObject;

import dbComponent.DBComponent;
import dbComponent.Pool;
import helper.HashPassword;
import helper.JSONManage;
import helper.Query;
import helper.Sanitize;

public class UpdateController {
	
	private Auth check = new Auth();
	private FindID find = new FindID();
	private HashPassword hashing = new HashPassword();
	private JSONManage messages = new JSONManage();
	private Sessions sessions = new Sessions();
	private Sanitize sanitize = new Sanitize();
	
	public JSONObject setUpdate(String id, JSONObject json_request) {
		try {
			String constraint = find.returnConstraintbyID(id);
			boolean[] status = check.isUserValidate(constraint, 
					json_request.get("password").toString());
			if(status[0] && status[1]) 
			{
				boolean have_session = sessions.isHaveSession(constraint);
				if(!have_session)
					return messages.reportErrorMessage("The user not have session");
				if(constraint==null)
					return messages.reportErrorMessage("Dont exists user by this id");
				boolean id_sanitize = sanitize.isSanitize(id);
				if(id_sanitize) 
				{
					String param_to_update = json_request.get("parameter").toString();
					String value_to_update = json_request.get("value").toString();
					updateParam(param_to_update, value_to_update, id);
					return messages.reportSuccessMessage(param_to_update + " is update");	
				}
				else
				{
					return messages.reportErrorMessage("You inputs not have the requeriments");
				}
			}
			if(!status[0])
			{
				String message = "The user dont exists, you arent registered ?";
				return messages.reportErrorMessage(message);
			}
			if(!status[1])
			{
				String message = "The password is wrong !";
				return messages.reportErrorMessage(message);
			}
			String message = "You find a easter egg.";
			return messages.reportErrorMessage(message);
		} 
		catch (NoSuchAlgorithmException | NullPointerException | SQLException | FileNotFoundException e) {
			e.printStackTrace();
			return messages.reportErrorMessage(e.getMessage());
		}
	}
	
	private void updateParam(String param, String value, String id) throws SQLException, NoSuchAlgorithmException, FileNotFoundException {

		param += "_users";
		if(param.equals("password_users")) 
		{
			value = hashing.ToHashPassword(value);
		}
		if(param.equals("name_users")) {
			DBComponent conn = Pool.getDBInstance();
			ResultSet res = conn.exeQueryRS("select.where.id_users", new Object[] {Integer.parseInt(id)});
			System.out.println(id);
			while(res.next()) {
				File file = new File(properties.Properties.URI_SOURCES + res.getString("name_users"));
				if(!file.exists())
					file.mkdirs();
				if(file.exists()) {
					if(file.isDirectory()) {
						conn.exeSimple(new Query("update."+param+".where.id_users", new Object[] {value, Integer.parseInt(id)}));
						File filerename = new File(properties.Properties.URI_SOURCES + value);
						file.renameTo(filerename);
						Pool.returnDBInstance(conn);
						break;
					}
					else {
						Pool.returnDBInstance(conn);
						throw new FileNotFoundException();
					}
				}
				else {
					Pool.returnDBInstance(conn);
					throw new FileNotFoundException();
				}
			}
		}
		else {
			DBComponent conn = Pool.getDBInstance();
			ResultSet res = conn.exeQueryRS("select.where.id_users", new Object[] {Integer.parseInt(id)});
			while(res.next()) {
				conn.exeSimple(new Query("update."+param+".where.id_users", new Object[] {value, Integer.parseInt(id)}));
				break;
			}
			Pool.returnDBInstance(conn);
		}
	}
}

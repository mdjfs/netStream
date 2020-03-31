package manage;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONObject;

import helper.JSONManage;
import resources.Database;
import resources.Pool;

public class InfoController {

	private JSONManage messages_info = new JSONManage();
	
	@SuppressWarnings("unchecked")
	public JSONObject getInfo(String id) {
		Database ConnectionSession = Pool.getInstance();
		try {
			ResultSet rs =ConnectionSession.selectWhereConstraintUnique("users", "id_users", id);
			while(rs.next()) {
				JSONObject json = new JSONObject();
				json.put("username", rs.getString("name_users"));
				json.put("email", rs.getString("email_users"));
				return messages_info.reportSuccessMessage(json.toJSONString());
			}
			return messages_info.reportErrorMessage("dont have info");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return messages_info.reportErrorMessage(e.getMessage());
		}
		finally {
			Pool.giveInstance();
		}
	}
}

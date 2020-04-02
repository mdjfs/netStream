package manage;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONObject;

import dbComponent.DBComponent;
import dbComponent.Pool;
import helper.JSONManage;

public class InfoController {

	private JSONManage messages_info = new JSONManage();
	
	@SuppressWarnings("unchecked")
	public JSONObject getInfo(String id) {
		DBComponent conn = Pool.getDBInstance();
		try {
			ResultSet rs = conn.exeQueryRS("select.where.id_users", new Object[]{Integer.parseInt(id)});
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
			Pool.returnDBInstance(conn);
		}
	}
}

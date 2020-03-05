package manage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.SQLException;

import javax.servlet.http.Part;

import org.json.simple.JSONObject;

public class VideoController {
	private String URI = "";
	private FindID find_user = new FindID();
	
	public JSONObject upload(String id, Part video, Part thumbnail, JSONObject json_request) {
		try 
		{
			File directory = new File(URI + "/" + find_user.returnConstraintbyID(id));
			if(directory.mkdirs())
			{
				try 
				{
					FileOutputStream site_out = new FileOutputStream(directory + json_request.get("name_video").toString());
				} 
				catch (FileNotFoundException e) 
				{
					e.printStackTrace();
				} 
			}
		} 
		catch (SQLException e1) 
		{
			e1.printStackTrace();
		}
	}
}

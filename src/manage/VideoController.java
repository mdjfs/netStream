package manage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import javax.servlet.http.Part;

import org.json.simple.JSONObject;

import aux.JSONManage;

import org.apache.commons.io.IOUtils;


public class VideoController {
	private String URI = "";
	private FindID find_user = new FindID();
	private JSONManage messages_videocontroller = new JSONManage();
	
	private void writeFile(File directory, Part part, String Type, String Name) throws IOException {
		FileOutputStream site_out = new FileOutputStream(directory + Name + "." + Type);
		InputStream part_of_video = part.getInputStream();
		byte[] videoAsByteArray = IOUtils.toByteArray(part_of_video);
		site_out.write(videoAsByteArray);
	}
	
	public JSONObject upload(String id, Part video, Part thumbnail, JSONObject json_request) {
		try 
		{
			File directory = new File(URI + "/" + find_user.returnConstraintbyID(id) + "/");
			if(directory.mkdirs())
			{
				try 
				{
					String name_of_thumbnail = json_request.get("name_thumbnail").toString();
					String name_of_video = json_request.get("name_video").toString();
					String type_video = json_request.get("type_video").toString();
					String type_thumbnail = json_request.get("type_thumbnail").toString();
					writeFile(directory, video,  type_video, name_of_video);
					writeFile(directory, thumbnail, type_thumbnail, name_of_thumbnail);
					return messages_videocontroller.reportSuccessMessage("Movie "+name_of_video+" upload successfully");
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
					return messages_videocontroller.reportErrorMessage(e.getMessage());
				} 
			}
			else
			{
				return messages_videocontroller.reportErrorMessage("Error with directory");
			}
		} 
		catch (SQLException e1) 
		{
			e1.printStackTrace();
			return messages_videocontroller.reportErrorMessage(e1.getMessage());
		}
	}
}

package manage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Base64.Decoder;

import org.json.simple.JSONObject;

import helper.JSONManage;



public class VideoController {
	private String URI = "/home/mdjfs/Documentos/netStreamSources";
	private FindID find_user = new FindID();
	private JSONManage messages_videocontroller = new JSONManage();
	
	private String write(String user, String name, String type, String part, String size) throws IOException {
		File in = new File(URI + "/" + user );
		in.mkdirs();
		FileOutputStream output = new FileOutputStream(in + "/" + name + "." + type, true);
		Decoder decoder = Base64.getDecoder();
		output.write(decoder.decode(part));
		output.close();
		File sizefile = new File(URI + "/" + user + "/" + name + "." + type);
		return (sizefile.length() / Float.parseFloat(size)) * 100 + "%";
	}
	
	public JSONObject upload(String id, JSONObject json_request) {
		if(id != null)
		{
			try 
			{
				String user = find_user.returnConstraintbyID(id);
				String name = json_request.get("name").toString();
				String type_video = json_request.get("type_video").toString();
				String type_thumbnail = json_request.get("type_thumbnail").toString();
				String part_video = json_request.get("part_video").toString();
				String part_thumbnail = json_request.get("part_thumbnail").toString();
				String size_video = json_request.get("size_video").toString();
				String size_thumbnail = json_request.get("size_thumbnail").toString();
				if(part_video.equals(""))
				{
					if(part_thumbnail.equals("")) {
						return messages_videocontroller.reportErrorMessage("not have parts");
					}
					else {
						return messages_videocontroller.reportSuccessMessage("thumbnail percent:" + 
												write(user, name, type_thumbnail, part_thumbnail, size_thumbnail));
					}
				}
				else {
					if(part_thumbnail.equals("")) {
						return messages_videocontroller.reportSuccessMessage("video percent:" +	
												write(user, name, type_video, part_video, size_video));
					}
					else {
						return messages_videocontroller.reportSuccessMessage("video percent:" + 
												write(user, name, type_video, part_video, size_video) +
												"thumbnail percent:" + 
												write(user, name, type_thumbnail, part_thumbnail, size_thumbnail));
					}
				}
				
			} 
			catch (SQLException | IOException e) 
			{
				e.printStackTrace();
				return messages_videocontroller.reportErrorMessage(e.getMessage());
			}
		}
		else 
		{
			return messages_videocontroller.reportErrorMessage("You aren't log-In");
		}
		
	}
}

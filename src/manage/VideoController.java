package manage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Base64;
import java.util.Base64.Decoder;

import org.json.simple.JSONObject;

import helper.JSONManage;
import helper.Sanitize;
import resources.Database;
import resources.Pool;



public class VideoController {
	private String URI = "/home/mdjfs/Documentos/netStreamSources";
	private FindID find_user = new FindID();
	private JSONManage messages_videocontroller = new JSONManage();
	private Sanitize sanitize = new Sanitize();
	
	private void uploadDB(String user, String name, String type, Boolean movie_or_thumbnail) throws SQLException {
		String id_user = find_user.returnIDbyConstraint(user);
		String date = Instant.now().toString();
		String src = user + "/" + name + "." + type;		
		String SQL = "";
		if(movie_or_thumbnail)
			SQL = "INSERT INTO videos (id_users, name, type, src, uploadtime) VALUES ("+id_user
							+",'"+name+"','"+type+"','"+src+"','"+date+"');";
		else
			SQL = "INSERT INTO thumbnails (id_users, name, type, src, uploadtime) VALUES ("+id_user
				+",'"+name+"','"+type+"','"+src+"','"+date+"');";
		Database db = Pool.getInstance();
		db.insert(SQL);
		Pool.giveInstance();
		
	}
	
	private String write(String user, String name, String type, String part) throws IOException {
		File in = new File(URI + "/" + user );
		Decoder decoder = Base64.getDecoder();
		FileOutputStream output = new FileOutputStream(in + "/" + name + "." + type, true);
		String frameparts = part.substring(part.indexOf("(")+1, part.indexOf(")"));
		String[] pckgs = frameparts.split("/");
		float actualpckg = Float.parseFloat(pckgs[0]);
		float totalpckg = Float.parseFloat(pckgs[1]);
		part = part.substring(part.indexOf(")")+1);
		if(! in.exists()) {
			in.mkdirs();
		}
		output.write(decoder.decode(part));
		output.close();
		if(actualpckg == totalpckg) {
			return "finished";
		}
		return actualpckg / totalpckg * 100 + "%";
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
				if(sanitize.is_sanitize(name) && sanitize.is_sanitize(type_video) 
					&& sanitize.is_sanitize(type_thumbnail)) {
					if(part_video.equals(""))
					{
						if(part_thumbnail.equals("")) {
							return messages_videocontroller.reportErrorMessage("not have parts");
						}
						else {
							String message = write(user, name, type_thumbnail, part_thumbnail);
							if(message.equals("finished"))
								uploadDB(user, name, type_thumbnail, false);
							return messages_videocontroller.reportSuccessMessage("thumbnail percent:" + 
															message);
						}
					}
					else {
						if(part_thumbnail.equals("")) {
							String message = write(user, name, type_video, part_video);
							if(message.equals("finished"))
								uploadDB(user, name, type_video, true);
							return messages_videocontroller.reportSuccessMessage("video percent:" +	
													message);
						}
						else {
							String messagethmb = write(user, name, type_thumbnail, part_thumbnail);
							String messagevid = write(user, name, type_video, part_video);
							if(messagethmb.equals("finished"))
								uploadDB(user, name, type_thumbnail, false);
							if(messagevid.equals("finished"))
								uploadDB(user, name, type_video, true);
							return messages_videocontroller.reportSuccessMessage("video percent:" + 
													messagevid +
													"thumbnail percent:" + 
													messagethmb);
						}
					}
				}
				else {
					return messages_videocontroller.reportErrorMessage("Check your params");
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

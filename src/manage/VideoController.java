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
	
	private void deleteDB(String user, String name, String type, Boolean movie_or_thumbnail) throws SQLException {
		String src = user + "/" + name + "." + type;		
		String SQL = "";
		if(movie_or_thumbnail)
			SQL = "DELETE FROM videos WHERE src='"+src+"';";
		else
			SQL = "DELETE FROM thumbnails WHERE src='"+src+"';";
		Database db = Pool.getInstance();
		db.delete(SQL);
		Pool.giveInstance();
	}
	
	private String write(String user, String name, String type, String part) throws IOException {
		File in = null;
		if(name.contains("profile/")) {
			name = name.split("/")[1];
			System.out.println(name);
			in = new File(URI + "/" + user + "/profile");
			if(! in.exists()) {
				in.mkdirs();
			}
		}
		else {
			in = new File(URI + "/" + user );
			if(! in.exists()) {
				in.mkdirs();
			}
		}
		Decoder decoder = Base64.getDecoder();
		FileOutputStream output = new FileOutputStream(in + "/" + name + "." + type, true);
		String frameparts = part.substring(part.indexOf("(")+1, part.indexOf(")"));
		String[] pckgs = frameparts.split("/");
		float actualpckg = Float.parseFloat(pckgs[0]);
		float totalpckg = Float.parseFloat(pckgs[1]);
		part = part.substring(part.indexOf(")")+1);
		output.write(decoder.decode(part));
		output.close();
		if(actualpckg >= totalpckg) {
			return "finished";
		}
		return actualpckg / totalpckg * 100 + "%";
	}
	public JSONObject delete(String id, JSONObject json_request) {
		if(id != null)
		{
			try 
			{
				JSONObject json = new JSONObject();
				String user = find_user.returnConstraintbyID(id);
				String name = json_request.get("name").toString();
				String type = json_request.get("type").toString();
				if(name.contains("profile/")) {
					
					File in = new File(URI + "/" + user + "/profile/profile.jpeg");
					if(in.exists()) {
						in.delete();
						return messages_videocontroller.reportSuccessMessage("File is delete");
					}
					else {
						in = new File(URI + "/" + user + "/profile/profile.png");
						if(in.exists()) {
							in.delete();
							return messages_videocontroller.reportSuccessMessage("File is delete");
						}
						else {
							in = new File(URI + "/" + user + "/profile/profile.jpg");
							if(in.exists()) {
								in.delete();
								return messages_videocontroller.reportSuccessMessage("File is delete");
							}
							else {
								return messages_videocontroller.reportErrorMessage("File dont exist");
							}
						}
					}
				}
				else {
					boolean video = type.equals("mp4") || type.equals("avi") || type.equals("mkv") || type.equals("flv") 
							|| type.equals("mov") || type.equals("wmv");
					boolean thumbnail = type.equals("jpeg") || type.equals("jpg") || type.equals("png") || type.equals("bmp") 
							|| type.equals("svg");
					if(video) {
						File in = new File(URI + "/" + user + "/"+name+"."+type );
						if(in.exists()) {
							deleteDB(user, name, type, true);
							in.delete();
							return messages_videocontroller.reportSuccessMessage("File is delete");
						}
						else {
							return messages_videocontroller.reportErrorMessage("File dont exist");
						}
					}
					else if(thumbnail) {
						File in = new File(URI + "/" + user + "/"+name+"."+type );
						if(in.exists()) {
							deleteDB(user, name, type, false);
							in.delete();
							return messages_videocontroller.reportSuccessMessage("File is delete");
						}
						else {
							return messages_videocontroller.reportErrorMessage("File dont exist");
						}
					}
					else {
						return messages_videocontroller.reportErrorMessage("Error in type");
					}
				}
				
			} 
			catch (SQLException e) 
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
	@SuppressWarnings("unchecked")
	public JSONObject upload(String id, JSONObject json_request) {
		if(id != null)
		{
			try 
			{
				JSONObject json = new JSONObject();
				String user = find_user.returnConstraintbyID(id);
				String name = json_request.get("name").toString();
				String type = json_request.get("type").toString();
				String part = json_request.get("part").toString();
				if(name.contains("profile/")) {
					boolean thumbnail = type.equals("jpeg") || type.equals("jpg") || type.equals("png") || type.equals("bmp") 
							|| type.equals("svg");
					if(thumbnail) {
						String message = write(user, name, type, part);
						if(message.equals("finished")) {
							json.put("ready", "true");
						}
						else {
							json.put("ready", "false");
							json.put("percent", message);
						}
						json.put("type", "photo");
						return messages_videocontroller.reportSuccessMessage(json.toJSONString());
					}
					else {
						return messages_videocontroller.reportErrorMessage("Only pics");
					}
				}
				else {
					type = type.toLowerCase();
					boolean video = type.equals("mp4") || type.equals("avi") || type.equals("mkv") || type.equals("flv") 
							|| type.equals("mov") || type.equals("wmv");
					boolean thumbnail = type.equals("jpeg") || type.equals("jpg") || type.equals("png") || type.equals("bmp") 
							|| type.equals("svg");
					if(sanitize.is_sanitize(name) && sanitize.is_sanitize(type)) {
						if(video)
						{
							String message = write(user, name, type, part);
							if(message.equals("finished")) {
								uploadDB(user, name, type, true);
								json.put("ready", "true");
							}
							else {
								json.put("ready", "false");
								json.put("percent", message);
							}
							json.put("type", "video");
							return messages_videocontroller.reportSuccessMessage(json.toJSONString());
						}
						else {
							if(thumbnail) {
								String message = write(user, name, type, part);
								if(message.equals("finished")) {
									uploadDB(user, name, type, false);
									json.put("ready", "true");
								}
								else {
									json.put("ready", "false");
									json.put("percent", message);
								}
								json.put("type", "thumbnail");
								return messages_videocontroller.reportSuccessMessage(json.toJSONString());
							}
							else {
								return messages_videocontroller.reportErrorMessage("Dont support this type");
							}
						}
					}
					else {
						return messages_videocontroller.reportErrorMessage("Check your params");
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

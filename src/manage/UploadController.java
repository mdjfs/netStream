package manage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Base64;
import java.util.Base64.Decoder;

import org.json.simple.JSONObject;

import dbComponent.DBComponent;
import dbComponent.Pool;
import helper.JSONManage;
import helper.Query;
import helper.Sanitize;



public class UploadController {
	private FindID find = new FindID();
	private JSONManage messages = new JSONManage();
	private Sanitize sanitize = new Sanitize();
	private boolean isVideo = false;
	private boolean isThumbnail = false;
	private String user;
	private String name;
	private String type;
	private String part;
	private String id;
	
	
	public UploadController(String id, JSONObject json) throws SQLException {
		this.id = id;
		this.user = find.returnConstraintbyID(id);
		this.name = json.get("name").toString();
		this.type = json.get("type").toString().toLowerCase();
		if(json.containsKey("part"))
			this.part = json.get("part").toString();
		this.isVideo = type.equals("mp4") || type.equals("avi") || type.equals("mkv") || type.equals("flv") 
				|| type.equals("mov") || type.equals("wmv");
		this.isThumbnail = type.equals("jpeg") || type.equals("jpg") || type.equals("png") || type.equals("bmp") 
				|| type.equals("svg");
	}
	
	private void uploadDB() throws SQLException {
		String id = find.returnIDbyConstraint(user);
		String date = Instant.now().toString();
		String src = user + "/" + name + "." + type;
		DBComponent conn = Pool.getDBInstance();
		if(isVideo) {
			conn.exeSimple(new Query("insert.videos", new Object[] {Integer.parseInt(id), name, type, src, date}));
		}
		if(isThumbnail) {
			conn.exeSimple(new Query("insert.thumbnails", new Object[] {Integer.parseInt(id), name, type, src, date}));
		}
		Pool.returnDBInstance(conn);
		
	}
	
	private void deleteDB() throws SQLException {
		String src = user + "/" + name + "." + type;
		DBComponent conn = Pool.getDBInstance();
		if(isVideo) {
			conn.exeSimple(new Query("delete.videos.where.src", new Object[] {src}));
		}
		if(isThumbnail) {
			conn.exeSimple(new Query("delete.thumbnails.where.src", new Object[] {src}));
		}
		Pool.returnDBInstance(conn);
	}
	
	private String write() throws IOException {
		File in = null;
		if(name.contains("profile/")) {
			name = name.split("/")[1];
			in = new File(properties.Properties.URI_SOURCES + user + "/profile/");
			if(! in.exists()) {
				in.mkdirs();
			}
		}
		else {
			in = new File(properties.Properties.URI_SOURCES + user );
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
	
	public JSONObject delete() {
		if(id != null)
		{
			try 
			{
				if(name.contains("profile/")) {
					
					File in = new File(properties.Properties.URI_SOURCES + user + "/profile/profile.jpeg");
					if(in.exists()) {
						in.delete();
						return messages.reportSuccessMessage("File is delete");
					}
					else {
						in = new File(properties.Properties.URI_SOURCES + user + "/profile/profile.png");
						if(in.exists()) {
							in.delete();
							return messages.reportSuccessMessage("File is delete");
						}
						else {
							in = new File(properties.Properties.URI_SOURCES + user + "/profile/profile.jpg");
							if(in.exists()) {
								in.delete();
								return messages.reportSuccessMessage("File is delete");
							}
							else {
								return messages.reportErrorMessage("File dont exist");
							}
						}
					}
				}
				else {		
					File in = new File(properties.Properties.URI_SOURCES + user + "/"+name+"."+type );
					if(in.exists()) {
						deleteDB();
						in.delete();
						return messages.reportSuccessMessage("File is delete");
					}
					else {
						return messages.reportErrorMessage("File dont exist");
					}
				}
				
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
				return messages.reportErrorMessage(e.getMessage());
			}
		}
		else 
		{
			return messages.reportErrorMessage("You aren't log-In");
		}
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject upload() {
		if(id != null)
		{
			try 
			{
				JSONObject json = new JSONObject();
				if(name.contains("profile/")) {
					if(isThumbnail) {
						String message = write();
						if(message.equals("finished")) {
							json.put("ready", "true");
						}
						else {
							json.put("ready", "false");
							json.put("percent", message);
						}
						return messages.reportSuccessMessage(json.toJSONString());
					}
					else {
						return messages.reportErrorMessage("Only pics");
					}
				}
				else {
					if(sanitize.isSanitize(name) && sanitize.isSanitize(type)) {
						String message = write();
						if(message.equals("finished")) {
							uploadDB();
							json.put("ready", "true");
						}
						else {
							json.put("ready", "false");
							json.put("percent", message);
						}
						return messages.reportSuccessMessage(json.toJSONString());
					}
					else {
						return messages.reportErrorMessage("Check your params");
					}
				}
				
			} 
			catch (SQLException | IOException e) 
			{
				e.printStackTrace();
				return messages.reportErrorMessage(e.getMessage());
			}
		}
		else 
		{
			return messages.reportErrorMessage("You aren't log-In");
		}
		
	}
}

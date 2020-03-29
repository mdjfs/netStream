package com;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import helper.JSONManage;
import manage.VideoController;

@WebServlet("/video")
public class Load extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private JSONParser parser = new JSONParser();
	private JSONManage json_servlet = new JSONManage();
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		    String user = request.getParameter("user");
		    String name = request.getParameter("name");
		    String type = "";
		    if(name.contains(".mp4"))
		    	type = "video/mp4";
		    if(name.contains(".png"))
		    	type = "image/x-png";
		    if(name.contains(".jpg") || name.contains(".jpeg"))
		    	type = "image/jpeg";
		    if(!type.equals(""))
		    {
		    	String dir = "/"+user+"/"+name;
			    String URL = "/home/mdjfs/Documentos/netStreamSources"+dir;
			    InputStream input = new FileInputStream(URL);
			    //response.setContentType("video/quicktime"); //Use this for VLC player
			    response.setContentType(type);
			    response.setHeader("Content-Disposition", "inline; filename=\""+ name + "\"");
			    OutputStream output = response.getOutputStream();
			    if(output != null && input != null) {
			    	byte[] buffer = new byte[2096];
				    int read = 0;
				    while ((read = input.read(buffer)) != -1) {
				      output.write(buffer, 0, read);
				    }
				    input.close();
				    output.flush();
				    output.close();
			    }
		    }



		  }
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] json_keys_video = {"name","type","part"};
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		String json = json_servlet.readRaw(request.getReader());
		if (json == null)
		{
			out.print(json_servlet.reportErrorMessage("Unfound json"));
		}
		else
		{
			JSONObject json_request;
			try {
				json_request = (JSONObject) parser.parse(json);
				if(json_servlet.is_all_keys(json_request, json_keys_video)) {
					VideoController upload_video = new VideoController();
					HttpSession session_video = request.getSession(false);
					if(session_video != null)
						out.print(upload_video.upload(request.getSession(false).getAttribute("ID").toString(), 
								 			json_request));
					else
						out.print(json_servlet.reportErrorMessage("You don't have session"));
				}
				else {
					out.print(json_servlet.reportErrorMessage("Keys json failed, you need this keys: " + json_servlet.say_keys(json_keys_video)));
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				out.print(json_servlet.reportErrorMessage(e.getMessage()));
			}
		}
		
	}

}

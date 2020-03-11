package com;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import aux.JSONManage;
import manage.VideoController;

@WebServlet("/video")
@MultipartConfig
public class Video extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private JSONParser parser = new JSONParser();
	private JSONManage json_servlet = new JSONManage();
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] json_keys_video = {"name_video","name_thumbnail","type_thumbnail","type_video"};
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		String json = json_servlet.readRaw(request.getReader());
		if (json == null)
		{
			out.print(json_servlet.reportErrorMessage("Unfound json"));
		}
		else
		{
			JSONObject json_response;
			JSONObject json_request;
			try {
				json_request = (JSONObject) parser.parse(json);
				if(json_servlet.is_all_keys(json_request, json_keys_video)) {
					VideoController upload_video = new VideoController();
					out.print(upload_video.upload(request.getSession(false).getAttribute("ID").toString(), 
								 		request.getPart("video"), 
								 		request.getPart("thumbnail"), 
								 		json_request));
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

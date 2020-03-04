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

import aux.JSONMessages;
import manage.DeleteController;
import manage.UpdateController;


@WebServlet("/delete")
@MultipartConfig
public class Delete extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private JSONParser parser = new JSONParser();
	private JSONMessages servlet_messages = new JSONMessages();

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		String json = request.getParameter("json");
		if (json==null)
		{
			out.print(servlet_messages.reportErrorMessage("Dont exist parameter json"));
		}
		try 
		{
			JSONObject json_response;
			JSONObject json_request = (JSONObject) parser.parse(json);
			boolean is_id = json_request.containsKey("id");
			boolean is_password = json_request.containsKey("password");
			if(is_id && is_password)
			{
				DeleteController request_delete = new DeleteController();
				json_response =  request_delete.setDelete(json_request);
				out.print(json_response);
			}
			else
			{
				out.print(servlet_messages.reportErrorMessage("data json failed"));
			}
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
			out.print(servlet_messages.reportErrorMessage("invalid json"));
		}
	}

}

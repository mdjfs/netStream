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
import manage.LogoutController;

@WebServlet("/logout")
@MultipartConfig
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private JSONParser parser = new JSONParser();
	private JSONMessages servlet_messages = new JSONMessages();

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin","*");
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
			boolean is_constraint = json_request.containsKey("constraint");
			if(is_constraint)
			{
				LogoutController request_logout = new LogoutController();
				json_response =  request_logout.setLogout(json_request);
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

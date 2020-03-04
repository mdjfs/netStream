package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import aux.JSONMessages;
import manage.RegisterController;

@WebServlet("/register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private JSONParser parser = new JSONParser();
	private JSONMessages servlet_messages = new JSONMessages();

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin","*");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		StringBuffer buffertext = new StringBuffer();
		BufferedReader reader = request.getReader();
		String line = "";
		while ((line = reader.readLine()) != null)
			buffertext.append(line);
		String json = buffertext.toString();
		if (json==null)
		{
			out.print(servlet_messages.reportErrorMessage("Dont exist parameter json"));
		}
		try 
		{
			JSONObject json_response;
			JSONObject json_request = (JSONObject) parser.parse(json);
			boolean is_name = json_request.containsKey("name");
			boolean is_password = json_request.containsKey("password");
			boolean is_email = json_request.containsKey("email");
			if(is_name && is_password && is_email)
			{
				RegisterController request_register = new RegisterController();
				json_response =  request_register.setRegister(json_request);
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

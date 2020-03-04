package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import aux.JSONMessages;
import manage.UpdateController;


@WebServlet("/update")
public class Update extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private JSONParser parser = new JSONParser();
	private JSONMessages servlet_messages = new JSONMessages();

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			HttpSession session_logout = request.getSession(false);
			String id = (String) session_logout.getAttribute("ID");
			if( id != null )
			{
				json_request.put("id", id);
				boolean is_parameter = json_request.containsKey("parameter");
				boolean is_value = json_request.containsKey("value");
				if(is_parameter && is_value)
				{
					UpdateController request_update = new UpdateController();
					json_response =  request_update.setUpdate(json_request);
					out.print(json_response);
				}
				else
				{
					out.print(servlet_messages.reportErrorMessage("data json failed"));
				}
			}
			else
				out.print(servlet_messages.reportErrorMessage("You aren't login"));
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
			out.print(servlet_messages.reportErrorMessage("invalid json"));
		}
	}

}

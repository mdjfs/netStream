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

import aux.ReportExceptions;
import manage.Update;


@WebServlet("/update")
@MultipartConfig
public class Update_com extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private JSONParser parser = new JSONParser();
	private ReportExceptions servlet_exception = new ReportExceptions();

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		String json = request.getParameter("json");
		if (json==null)
		{
			out.print(servlet_exception.ReportErrorMessage("Dont exist parameter json"));
		}
		try 
		{
			JSONObject json_response;
			JSONObject json_request = (JSONObject) parser.parse(json);
			boolean is_id = json_request.containsKey("id");
			boolean is_parameter = json_request.containsKey("parameter");
			boolean is_value = json_request.containsKey("value");
			boolean is_password = json_request.containsKey("password");
			if(is_id && is_parameter && is_value && is_password)
			{
				Update request_update = new Update();
				json_response =  request_update.SetUpdate(json_request);
				out.print(json_response);
			}
			else
			{
				out.print(servlet_exception.ReportErrorMessage("data json failed"));
			}
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
			out.print(servlet_exception.ReportErrorMessage("invalid json"));
		}
	}

}

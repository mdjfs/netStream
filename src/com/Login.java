package com;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import helper.JSONManage;
import manage.FindID;
import manage.LoginController;



@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private JSONParser parser = new JSONParser();
	private JSONManage servlet_messages = new JSONManage();
	
		//Parametros que debe contener el json:
	private String json_keys_login[] = {"constraint", "password"};

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* Endpoint que se encarga del Login y lee un json por raw */
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		String json = servlet_messages.readRaw(request.getReader());
		if (json==null)
		{
			out.print(servlet_messages.reportErrorMessage("Unfound json"));
		}
		else
		{
			try 
			{
				JSONObject json_response;
				JSONObject json_request = (JSONObject) parser.parse(json);
				boolean is_all_keys = servlet_messages.is_all_keys(json_request, json_keys_login);
				if(is_all_keys)
				{
					LoginController request_login = new LoginController();
					json_response =  request_login.setLogin(json_request);
					if(json_response.get("status").equals("200"))
					{
						FindID give_id_session = new FindID();
						String id = null;
						try 
						{
							id = give_id_session.returnIDbyConstraint(json_request.get("constraint").toString());
							if(id != null) {
								HttpSession create_session = request.getSession(true);
								create_session.setAttribute("ID", id);
								out.print(json_response);
							}
							else
							{
								out.print(servlet_messages.reportErrorMessage("error in session"));
							}
						} 
						catch (SQLException e) 
						{
							out.print(servlet_messages.reportErrorMessage(e.getMessage()));
							e.printStackTrace();
						}
					}
					else
					{
						out.print(json_response);
					}
				}
				else
				{
					out.print(servlet_messages.reportErrorMessage("Keys json failed, you need this keys: " + servlet_messages.say_keys(json_keys_login)));
				}
			} 
			catch (ParseException e) 
			{
				e.printStackTrace();
				out.print(servlet_messages.reportErrorMessage("invalid json"));
			}
		}
	}
}

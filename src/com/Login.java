package com;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import aux.JSONMessages;
import manage.FindID;
import manage.LoginController;



@WebServlet("/login")
@MultipartConfig
public class Login extends HttpServlet {
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
			boolean is_constraint = json_request.containsKey("constraint");
			boolean is_password = json_request.containsKey("password");
			if(is_constraint && is_password)
			{
				LoginController request_login = new LoginController();
				json_response =  request_login.setLogin(json_request);
				if(json_response.get("status").equals("200"))
				{
					FindID give_id_cookie = new FindID();
					String id = null;
					try 
					{
						id = give_id_cookie.returnIDbyConstraint(json_request.get("constraint").toString());
					} 
					catch (SQLException e) 
					{
						out.print(servlet_messages.reportErrorMessage(e.getMessage()));
						e.printStackTrace();
					}
					if(id != null) {
						Cookie ID =new Cookie("ID",id);
						System.out.println(request.getCookies());
						response.addCookie(ID);
						out.print(json_response);
					}
					else
					{
						out.print(servlet_messages.reportErrorMessage("error on create session"));
					}
				}
				else
				{
					out.print(json_response);
				}
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

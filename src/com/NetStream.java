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
import manage.Login;
import manage.Logout;
import manage.Register;

@MultipartConfig
@WebServlet("/NetStream")
public class NetStream extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private JSONParser parser = new JSONParser();
	private ReportExceptions servlet_exception = new ReportExceptions();
       
    public NetStream() {
        super();
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		String json = request.getParameter("json");
		try 
		{
			JSONObject json_response;
			JSONObject json_request = (JSONObject) parser.parse(json);
			switch(json_request.get("action").toString().toLowerCase()) 
			{
				case "register":
					Register request_register = new Register();
					json_response = request_register.SetRegister(json_request);
					out.print(json_response);
					break;
				case "login":
					Login request_login = new Login();
					json_response = request_login.SetLogin(json_request);
					out.print(json_response);
					break;
				case "logout":
					Logout request_logout = new Logout();
					json_response = request_logout.SetLogout(json_request);
					out.print(json_response);
					break;
				default:
					String message = "This action is invalide, please try register, login or logout";
					out.print(servlet_exception.ReportErrorMessage(message));
					
			}
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
			out.print(servlet_exception.ReportErrorMessage("json invalido"));
		}
	}

}

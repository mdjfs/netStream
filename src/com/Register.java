package com;

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

import helper.JSONManage;
import manage.RegisterController;

@WebServlet("/register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private JSONParser parser = new JSONParser();
	private JSONManage servlet_messages = new JSONManage();
	
			// Parametros que debe enviar el json:
	private String json_keys_register[] = {"name", "email", "password"};

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* Endpoint dedicado a Registrar Usuarios, lee un json por Raw */
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
				boolean is_all_keys = servlet_messages.is_all_keys(json_request, json_keys_register);
				if(is_all_keys)
				{
					RegisterController request_register = new RegisterController();
					json_response =  request_register.setRegister(json_request);
					out.print(json_response);
				}
				else
				{
					out.print(servlet_messages.reportErrorMessage("Keys json failed, you need this keys: " + servlet_messages.say_keys(json_keys_register)));
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

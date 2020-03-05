package com;

import java.io.BufferedReader;
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

import aux.JSONMessages;
import manage.FindID;
import manage.LoginController;



@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private JSONParser parser = new JSONParser();
	private JSONMessages servlet_messages = new JSONMessages();
	
		//Parametros que debe contener el json:
	private String json_keys_login[] = {"constraint", "password"};

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* Endpoint que se encarga del Login y lee un json por raw */
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		String json = readRaw(request.getReader());
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
				boolean is_all_keys = is_all_keys(json_request);
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
					out.print(servlet_messages.reportErrorMessage("Keys json failed, you need this keys: " + say_keys()));
				}
			} 
			catch (ParseException e) 
			{
				e.printStackTrace();
				out.print(servlet_messages.reportErrorMessage("invalid json"));
			}
		}
	}
	
	private String say_keys() {
		/* metodo que devuelve el atributo con arreglo de llaves en string */
		String keys = "";
		for(int i=0; i < json_keys_login.length ; i++) {
			if(i == json_keys_login.length - 1)
				keys += json_keys_login[i];
			else
				keys += json_keys_login[i]+", ";
		}
		return keys;
	}
	
	private Boolean is_all_keys(JSONObject json_request) {
		/* metodo que verifica si todas las llaves del servlet estan en el json */
		for(int i=0; i < json_keys_login.length ; i++) {
			if( ! json_request.containsKey(json_keys_login[i]) ) {
				return false;
			}
		}
		return true;
	}

	private String readRaw(BufferedReader buffer) throws IOException {
		/* metodo que lee texto enviado en raw y lo devuelve como string */
		StringBuffer buffertext = new StringBuffer();
		BufferedReader reader = buffer;
		String line = "";
		while ((line = reader.readLine()) != null)
			buffertext.append(line);
		return buffertext.toString();
	}
}

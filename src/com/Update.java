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
	private String json_keys_update[] = {"password", "parameter", "value"};

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* endpoint que se encarga de actualizar datos del usuario, lee un json por raw */
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
					HttpSession session_update = request.getSession(false);
					if( session_update != null ) {
						String id = (String) session_update.getAttribute("ID");
						if(id != null)
						{
							UpdateController request_update = new UpdateController();
							json_response =  request_update.setUpdate(id, json_request);
							out.print(json_response);
						}
						else
						{
							out.print(servlet_messages.reportErrorMessage("You aren't log-in !"));
						}
					}
					else
					{
						out.print(servlet_messages.reportErrorMessage("You not have session"));
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
		for(int i=0; i < json_keys_update.length ; i++) {
			if(i == json_keys_update.length - 1)
				keys += json_keys_update[i];
			else
				keys += json_keys_update[i]+", ";
		}
		return keys;
	}
	
	private Boolean is_all_keys(JSONObject json_request) {
		/* metodo que verifica si todas las llaves del servlet estan en el json */
		for(int i=0; i < json_keys_update.length ; i++) {
			if( ! json_request.containsKey(json_keys_update[i]) ) {
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

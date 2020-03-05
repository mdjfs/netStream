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
	
			// Parametros que debe enviar el json:
	private String json_keys_register[] = {"name", "email", "password"};

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* Endpoint dedicado a Registrar Usuarios, lee un json por Raw */
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
					RegisterController request_register = new RegisterController();
					json_response =  request_register.setRegister(json_request);
					out.print(json_response);
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
		for(int i=0; i < json_keys_register.length ; i++) {
			if(i == json_keys_register.length - 1)
				keys += json_keys_register[i];
			else
				keys += json_keys_register[i]+", ";
		}
		return keys;
	}
	
	private Boolean is_all_keys(JSONObject json_request) {
		/* metodo que verifica si todas las llaves del servlet estan en el json */
		for(int i=0; i < json_keys_register.length ; i++) {
			if( ! json_request.containsKey(json_keys_register[i]) ) {
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

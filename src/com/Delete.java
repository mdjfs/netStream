package com;
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

import aux.JSONManage;
import manage.DeleteController;


@WebServlet("/delete")
public class Delete extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private JSONParser parser = new JSONParser();
	private JSONManage servlet_messages = new JSONManage();

		//Parametros que debe contener el json:
	private String json_keys_delete[] = {"password"};
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* Endpoint que se encarga de borrar el usuario, lee un json por raw */
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
				boolean is_all_keys = servlet_messages.is_all_keys(json_request, json_keys_delete);
				if(is_all_keys)
				{
					HttpSession session_delete = request.getSession(false);
					if( session_delete != null ) {
						String id = (String) session_delete.getAttribute("ID");
						if(id != null)
						{
							DeleteController request_delete = new DeleteController();
							json_response =  request_delete.setDelete(id, json_request);
							session_delete.removeAttribute("ID");
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
					out.print(servlet_messages.reportErrorMessage("Keys json failed, you need this keys: " + servlet_messages.say_keys(json_keys_delete)));
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

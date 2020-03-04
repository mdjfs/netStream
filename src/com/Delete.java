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
import manage.DeleteController;
import manage.LogoutController;


@WebServlet("/delete")
public class Delete extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private JSONMessages servlet_messages = new JSONMessages();

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		JSONObject json_response;
		HttpSession session_logout = request.getSession(false);
		String id = (String) session_logout.getAttribute("ID");
		if(id != null)
		{
			DeleteController request_delete = new DeleteController();
			json_response =  request_delete.setDelete(id);
			session_logout.removeAttribute("ID");
			out.print(json_response);
		}
		else
		{
			out.print(servlet_messages.reportErrorMessage("You aren't log-in !"));
		}
	}

}

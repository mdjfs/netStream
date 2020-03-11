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

import aux.JSONManage;
import manage.LogoutController;

@WebServlet("/logout")
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private JSONManage servlet_messages = new JSONManage();

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		HttpSession session_logout = request.getSession(false);
		if( session_logout != null ) {
			String id = (String) session_logout.getAttribute("ID");
			if( id != null )
			{
				LogoutController make_logout = new LogoutController();
				JSONObject json_response = make_logout.setLogout(id);
				session_logout.removeAttribute("ID");
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

}

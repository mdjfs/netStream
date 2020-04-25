package com;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import dbComponent.DBComponent;
import dbComponent.Pool;
import helper.JSONManage;
import manage.FindID;
import manage.UpdateController;

@WebServlet("/news")
public class News extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private FindID find = new FindID();
	private JSONManage messages = new JSONManage();

	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DBComponent conn = Pool.getDBInstance();
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		JSONObject json = new JSONObject();
		try {
			ResultSet rs = conn.exeQueryRS("select.videos", new Object[] {});
			int i = 0;
			while(rs.next() && i < 10) {
				JSONObject jsonVideo = new JSONObject();
				jsonVideo.put("name", rs.getString("name"));
				jsonVideo.put("type", rs.getString("type"));
				jsonVideo.put("username", find.returnConstraintbyID(rs.getString("id_users")));
				ResultSet rsthumb = conn.exeQueryRS("select.thumbnails.where.id_users.name", new Object[] {rs.getInt("id_users"),
																											rs.getString("name")});
				while(rsthumb.next()) {
					JSONObject jsonThumbnail = new JSONObject();
					jsonThumbnail.put("name", rsthumb.getString("name"));
					jsonThumbnail.put("type", rsthumb.getString("type"));
					jsonVideo.put("thumbnail", jsonThumbnail.toJSONString());
					break;
				}
				json.put((i+1)+"",jsonVideo.toJSONString());
				i++;
			}
			out.print(messages.reportSuccessMessage(json.toJSONString()));
		} catch (SQLException e) {
			e.printStackTrace();
			out.print(messages.reportErrorMessage(e.getMessage()));
		}
		finally {
			Pool.returnDBInstance(conn);
		}
		
	}

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		HttpSession sessions = request.getSession(false);
		if( sessions != null ) {
			String id = (String) sessions.getAttribute("ID");
			if(id != null)
			{
				JSONObject json = new JSONObject();
				DBComponent conn = Pool.getDBInstance();
				try {
					ResultSet rs = conn.exeQueryRS("select.videos.where.id_users", new Object[] {Integer.parseInt(id)});
					int i = 0;
					while(rs.next() && i < 10) {
						JSONObject jsonVideo = new JSONObject();
						jsonVideo.put("name", rs.getString("name"));
						jsonVideo.put("type", rs.getString("type"));
						jsonVideo.put("username", find.returnConstraintbyID(rs.getString("id_users")));
						ResultSet rsthumb = conn.exeQueryRS("select.thumbnails.where.id_users.name", new Object[] {rs.getInt("id_users"),
																													rs.getString("name")});
						while(rsthumb.next()) {
							JSONObject jsonThumbnail = new JSONObject();
							jsonThumbnail.put("name", rsthumb.getString("name"));
							jsonThumbnail.put("type", rsthumb.getString("type"));
							jsonVideo.put("thumbnail", jsonThumbnail.toJSONString());
							break;
						}
						json.put((i+1)+"",jsonVideo.toJSONString());
						i++;
					}
					out.print(messages.reportSuccessMessage(json.toJSONString()));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					out.print(messages.reportErrorMessage(e.getMessage()));
				}
				finally
				{
					Pool.returnDBInstance(conn);
				}
				
			}
			else
			{
				out.print(messages.reportErrorMessage("You aren't log-in !"));
			}
		}
		else
		{
			out.print(messages.reportErrorMessage("You not have session"));
		}
	}

}

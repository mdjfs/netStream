package resources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class Database {
	
	private Connection conn;
	private Statement statement;

	public Database() 
	{
		try 
		{
			Class.forName("org.postgresql.Driver");
			this.conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/netStream","postgres","postgres");
			this.statement = conn.createStatement();
		} 
		catch (SQLException | ClassNotFoundException sqle) 
		{
	            System.out.println("Error connecting in SQL: " + sqle);
		}
	}
	
	public void insert(String SQL) throws SQLException {
		statement.executeUpdate(SQL);
	}
	
	public void update(String SQL) throws SQLException {
		statement.executeUpdate(SQL);
	}
	
	public void delete(String SQL) throws SQLException {
		statement.executeUpdate(SQL);
	}
	
	public ResultSet selectWhereConstraintUnique(String table, String constraint, String value) throws SQLException {
		String SQL = "SELECT *FROM "+table+" WHERE "+constraint+"='"+value+"';";
		return statement.executeQuery(SQL);
	}

}

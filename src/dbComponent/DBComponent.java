package dbComponent;

import helper.ConfigComponent;
import helper.Query;
import helper.QueryHandler;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class DBComponent {
	
	private Connection conn = null;
	private ConfigComponent config_db = new ConfigComponent(properties.Properties.URI_DBCONFIG);
	private Properties db_properties = null;
	private QueryHandler querys = new QueryHandler(properties.Properties.URI_QUERYS);
	private boolean is_busy = false;
	
	public DBComponent(){
		try{
			db_properties = config_db.getObjectProperties();
			Class.forName(db_properties.getProperty("db.driver"));
			this.conn = DriverManager.getConnection(db_properties.getProperty("db.url")
													,db_properties.getProperty("db.username")
													,db_properties.getProperty("db.password"));
		} 
		catch (SQLException | ClassNotFoundException sqle){
	            System.out.println("Error connecting in SQL: " + sqle);
		}
	}
	
	private PreparedStatement setParams(String id, Object[] params) throws SQLException {
		String query = querys.getQuery(id);
		PreparedStatement sentence = conn.prepareStatement(query);
		char[] detect_params = query.toCharArray();
		int params_count = 0;
		for(int i=0; i<detect_params.length;i++)
		{
			if(detect_params[i] == '?')
			{
				params_count += 1;
				sentence.setObject(params_count, params[params_count-1]);
			}
		}
		return sentence;
	}
	
	public ArrayList<HashMap<String, Object>> exeQueryAL(String id, Object[] params) throws SQLException {
		ArrayList<HashMap<String , Object>>	result  = new ArrayList<HashMap<String,Object>>();
		PreparedStatement sentence = setParams(id, params);
		ResultSet rs = sentence.executeQuery();
		ResultSetMetaData meta_data = rs.getMetaData();
		while(rs.next()) {
			HashMap<String, Object> querys = new HashMap<String, Object>();
			for(int i=0; i< meta_data.getColumnCount() ; i++) {
				String column_name = meta_data.getColumnName(i);
				Object column_value = rs.getObject(i);
				querys.put(column_name, column_value);
			}
			result.add(querys);
		}
		return result;
	}
	
	public ResultSet exeQueryRS(String id, Object[] params) throws SQLException {
		PreparedStatement sentence = setParams(id, params);
		return sentence.executeQuery();
	}
	
	public ResultSet onlyTests(String SQL) throws SQLException {
		Statement statement = conn.createStatement();
		return statement.executeQuery(SQL);
	}
	
	public void exeBatch(ArrayList<Query> list_query) throws SQLException {
		//El metodo exeBatch recibe una lista de objeto Querys con sus parametros
		try {
			//para usar los metodos rollback y commit el autocommit debe estar desactivado. 
		    conn.setAutoCommit(false);
		    for(Query query : list_query) {
		    	PreparedStatement sentence = setParams(query.getIDQuery(), query.getParamsQuery());
		    	sentence.executeUpdate();
		    }
			conn.commit();
			
		} catch (SQLException e) {
			//Si hay un error de cualquier tipo en el query se ejecuta el metodo rollback que deshace los cambios de la base de datos.
			conn.rollback();
			throw new SQLException(e);
		}
		finally {
			conn.setAutoCommit(true);
		}
	}
	
	public void exeSimple(Query query) throws SQLException{
		try {
		    conn.setAutoCommit(false);
		    PreparedStatement sentence = setParams(query.getIDQuery(), query.getParamsQuery());
		    sentence.executeUpdate();
			conn.commit();
			
		} catch (SQLException e) {
			conn.rollback();
			throw new SQLException(e);
		}
		finally {
			conn.setAutoCommit(true);
		}
	}
	
	public void setBusy(boolean is_busy) {
		this.is_busy = is_busy;
	}
	
	public boolean getBusy() {
		return is_busy;
	}
	
}

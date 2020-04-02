package helper;

public class Query {
	private String id_query = null;
	private Object[] params_query = null;
	
	public Query(String id_query, Object[] params_query) {
		this.id_query = id_query;
		this.params_query = params_query;
	}
	
	public String getIDQuery() {
		return id_query;
	}
	
	public Object[] getParamsQuery() {
		return params_query;
	}
}

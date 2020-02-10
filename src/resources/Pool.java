package resources;

public class Pool {
	private static boolean is_instance_used = false;
	private static Database Connection = new Database();
	
	public static Database getInstance() {
		if(is_instance_used) 
		{
			return null;
		}
		else 
		{
			is_instance_used = true;
			return Connection;
		}
	}
	
	public static void giveInstance() {
		is_instance_used = false;
	}
	

}

package dbComponent;

import java.util.ArrayList;
import java.util.Properties;

import helper.ConfigComponent;

public class Pool {
	
	private static ArrayList<DBComponent> instances = new ArrayList<DBComponent>();
	private static int max_connections = 50; // DEFAULT
	private static int hops = 5;
	private static int requests = 0;
	private ConfigComponent config_pool = new ConfigComponent(properties.Properties.URI_POOLCONFIG);
	
	
	private Pool() {
		Properties pool_properties = config_pool.getObjectProperties();
		if(pool_properties.containsKey("maxconnections") && pool_properties.containsKey("hops"))
		{
			Pool.max_connections = (int) pool_properties.get("maxconnections");
			Pool.hops = (int) pool_properties.get("hops");
		}
	}
	
	public static synchronized DBComponent getDBInstance() {
		requests++;
		if(instances.size() < requests && requests < max_connections)
		{
			for(int i = 0; i < hops; i++)
			{
				instances.add(new DBComponent());
			}
		}
		for(DBComponent viewStatus : instances) {
			if(viewStatus.getBusy() == false) {
				viewStatus.setBusy(true);
				return viewStatus;
			}
		}
		return null;
	}
	
	public static synchronized void returnDBInstance(DBComponent instance) {
		instance.setBusy(false);
		requests--;
	}
}

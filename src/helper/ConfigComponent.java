package helper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigComponent {
	
	Properties prop = new Properties();
	InputStream text_config = null;
	
	public ConfigComponent(String directory_of_config_file) {
		try 
		{
			text_config = new FileInputStream(directory_of_config_file);
			prop.load(text_config);
		} 
		catch(IOException e) {
			System.out.println(e.toString());
		}
	}
	
	public Properties getObjectProperties() {
		return prop;
	}
	
	

}

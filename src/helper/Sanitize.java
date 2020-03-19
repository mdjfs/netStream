package helper;

public class Sanitize {
	
	
	public boolean is_sanitize(String stringtocheck)
	{
		if(stringtocheck.contains("'") || stringtocheck.contains(";")) 
		{
			return false;
		}
		if(stringtocheck.contains("=") || stringtocheck.contains("("))
		{
			return false;
		}
		if(stringtocheck.contains("[") || stringtocheck.contains(")"))
		{
			return false;
		}
		if(stringtocheck.contains("]") || stringtocheck.contains("\\"))
		{
			return false;
		}
		if(stringtocheck.contains("\"") || stringtocheck.contains(","))
		{
			return false;
		}
		return true;
	}
}

package helper;

public class Sanitize {
	
	
	public boolean isSanitize(String stringtocheck)
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

package helper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashPassword {
	
	public String ToHashPassword(String passwordToHash) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(passwordToHash.getBytes());
        byte[] bytes = md.digest();
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
        {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
	}
	
	/*public boolean VerifyHashPassword(String passwordToVerify) {
		String[] passwordhash = ToHashPassword(passwordToVerify);
	}*/
}

package tests;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class Decode {

	public static void main(String[] args) throws IOException {
		
		ArrayList<String> parts = new ArrayList<String>();
		File fichero = new File("/home/mdjfs/Descargas/facebook.png");
		FileInputStream ficheroStream = new FileInputStream(fichero);
		byte data[] = new byte[(int)fichero.length()];
		ficheroStream.read(data);
		int pointer_reader = 0;
		Encoder encoder = Base64.getEncoder();
		Decoder decoder = Base64.getDecoder();

		FileWriter fw = new FileWriter("/home/mdjfs/hola.txt");
		while(pointer_reader < data.length)
		{
			byte[] part = new byte[1024];
			for(int i = 0 ; i < 1024 ; i ++ ) {
				if(pointer_reader != data.length)
				{
					part[i] = data[pointer_reader];
					if(i==1023) {
							fw.write("{\n\"name\":\"pruebajeje22\",\n\"type_thumbnail\":\"png\",\n\"type_video\":\"mp4\",\n\"part_video\":\"\",\n\"part_thumbnail\":\""+encoder.encodeToString(part)+"\",\n\"size_video\":\"\",\n\"size_thumbnail\":\"95224\"\n}\n");
							
							System.out.println("{\n\"name\":\"pruebajeje\",\n\"type_thumbnail\":\"png\",\n\"type_video\":\"mp4\",\n\"part_video\":\"\",\n\"part_thumbnail\":\""+encoder.encodeToString(part)+"\",\n\"size_video\":\"\",\n\"size_thumbnail\":\"95224\"\n}\n");
					}
					pointer_reader++;
				}
			}
		}
		fw.close();
		
		/*for(String part : parts) {
			System.out.println(part);
		}*/
		
		
		
		/*byte part[] = new byte[1024];
		float parts = data.length / 1024;
		int parts_integer = (int) parts + 1;
		
		float percent = parts - parts_integer;
		int last_part = (int) (1024 * percent) + 1;
		for(int i=0 ; i < 1024 ; i ++ )
		{
			part[i] = data[i];
			if(i==1023) {
				String encode = Base64.getEncoder().encodeToString(part);
				System.out.println(encode);
			}
		}*/
		
		/*
		byte[] decoding = Base64.getDecoder().decode(encode);
		FileOutputStream output = null;
		try {
			output = new FileOutputStream("/home/mdjfs/imagen.png");
			output.write(decoding);
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/

	}

}

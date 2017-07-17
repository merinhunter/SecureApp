package assembler;

import java.io.*;
import java.security.*;

public class SHA512CheckSum {
	public final static int SHA512_SIZE = 64;

	public static byte[] checksum(File file) throws FileNotFoundException, Exception {
		//StringBuffer hexString = null;
		MessageDigest digest = MessageDigest.getInstance("SHA-512", "BC");
		InputStream fis = new FileInputStream(file);
		int n = 0;
		byte[] buffer = new byte[1024];
		
		while(n != -1) {
			n = fis.read(buffer);
			if(n > 0)
				digest.update(buffer, 0, n);
		}
		
		fis.close();
		
		return digest.digest();

		/*md = MessageDigest.getInstance("SHA-512", "BC");
		input = new FileInputStream(f);

		byte[] data = new byte[1024];

		int nread = 0;
		while ((nread = input.read(data)) != -1) {
			md.update(data, 0, nread);
		}
		// md.update(file.getBytes(), 0, file.length());
		mdbytes = md.digest();*/

		// Convertimos los bytes en formato hexadecimal
		/*hexString = new StringBuffer();
		for (int i = 0; i < mdbytes.length; i++) {
			hexString.append(Integer.toHexString(0xFF & mdbytes[i]));
		}*/

		//return hexString.toString();
		//return mdbytes;
	}
}
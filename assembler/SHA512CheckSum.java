package assembler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.MessageDigest;

public class SHA512CheckSum {
	public final static int SHA512_SIZE = 64;

	public static byte[] checksum(File file) throws FileNotFoundException, Exception {
		MessageDigest digest = MessageDigest.getInstance("SHA-512", "BC");
		InputStream fis = new FileInputStream(file);
		int n = 0;
		byte[] buffer = new byte[1024];

		while (n != -1) {
			n = fis.read(buffer);
			if (n > 0)
				digest.update(buffer, 0, n);
		}

		fis.close();

		return digest.digest();
	}
}
package cipher;

import java.io.*;

public class Decryptor {
	private String file;
	private AsymmetricCipher cipher;

	public Decryptor(String file, String pass) {
		this.file = file;
		cipher = new AsymmetricCipher(pass);
	}

	public void decrypt() {
		File original = new File(file);
		File crypted = new File(file + "_Original");
		FileInputStream input = null;
		FileOutputStream output = null;

		try {
			input = new FileInputStream(original);
			output = new FileOutputStream(crypted);
		} catch (FileNotFoundException e) {
			System.err.println("Can't find file: " + e);
		}
		DataInputStream datain = new DataInputStream(input);
		DataOutputStream dataout = new DataOutputStream(output);

		try {
			cipher.InitCiphers();
			cipher.CBCDecrypt(datain, dataout);
			System.err.println("DECRYPTION FINISHED");
		} catch (Exception e) {
			System.err.println("Exception when initiating the ciphers:");
			e.printStackTrace();
		} finally {
			try {
				input.close();
				output.close();
			} catch (IOException e) {
				System.err.println("IO Exception:");
				e.printStackTrace();
			}
		}
	}
}

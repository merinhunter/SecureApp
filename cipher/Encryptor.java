package cipher;

import java.io.*;
import assembler.Slice;

public class Encryptor {
	//private String file;
	private AsymmetricCipher cipher;

	public Encryptor(String file, String pass) {
		//this.file = file;
		cipher = new AsymmetricCipher(pass);
	}

	public void encrypt(Slice slice) {
		File original = new File(file);
		File crypted = new File(file + "_AES");
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
			cipher.CBCEncrypt(datain, dataout);
			System.err.println("ENCRYPTION FINISHED");
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
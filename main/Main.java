package main;

//import java.nio.file.Files;
import java.io.*;
import java.util.ArrayList;
//import java.util.Base64;
import java.util.Scanner;

import org.bouncycastle.util.encoders.Hex;

/*import com.besaba.revonline.pastebinapi.Pastebin;
import com.besaba.revonline.pastebinapi.impl.factory.PastebinFactory;
import com.besaba.revonline.pastebinapi.paste.Paste;
import com.besaba.revonline.pastebinapi.paste.PasteBuilder;
import com.besaba.revonline.pastebinapi.paste.PasteExpire;
import com.besaba.revonline.pastebinapi.paste.PasteVisiblity;
import com.besaba.revonline.pastebinapi.response.Response;*/

//import org.bouncycastle.util.encoders.Hex;

//import assembler.SHA512CheckSum;

import cipher.*;
import rsa.RSALibrary;
import rsa.RSA_PSS;
import assembler.*;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		System.out.print("Put file path: ");

		//String path = sc.nextLine();
		
		String path = "/home/sergio/Escritorio/prueba";

		File f = new File(path);
		if(f.exists() && !f.isDirectory()) {
			try {
				Slicer slicer = new Slicer(f, 10);
				ArrayList<Slice> slices = slicer.slice();
				for (Slice slice : slices) {
					System.out.println(slice.toString());
					//System.out.println(Slice.fromBytes(slice.toBytes()).toString());
					//Slice.fromBytes(slice.toBytes());
				}
				
				File f2 = new File("/home/sergio/Escritorio/pepe");
				//Composer.compose(slices, f2);

				Encryptor encryptor = new Encryptor();
				ArrayList<EncFile> files = encryptor.encrypt(slices);
				for (EncFile file : files) {
					System.out.println(file.toBase64());
					System.out.println(file.getData().length);
				}

				Decryptor decryptor = new Decryptor(encryptor.cipher.getKey());
				ArrayList<Slice> decSlices = decryptor.decrypt(files);
				for (Slice slice : decSlices) {
					System.out.println(slice.toString());
					//System.out.println("TRAZA");
				}
				Composer.compose(decSlices, f2);

				/*System.out.println("BYE");
				RSALibrary rsa = new RSALibrary();
				RSA_PSS rsa_pss_priv = new RSA_PSS(rsa.getKey(rsa.PRIVATE_KEY_FILE));
				byte[] S = rsa_pss_priv.sign("hola".getBytes());
				System.out.println(Hex.toHexString(S));
				RSA_PSS rsa_pss_pub = new RSA_PSS(rsa.getKey(rsa.PUBLIC_KEY_FILE));
				System.out.println(rsa_pss_pub.verify("hola".getBytes(), S));*/
			} catch (FileNotFoundException e) {
				System.err.println("FileNotFoundException: " + e.getMessage());
				System.exit(-1);
			} catch (Exception e) {
				System.err.println("Exception: " + e.getMessage());
				System.exit(-1);
			}
		}

		/*File fich = new File(file);
		if (!fich.exists()) {
			sc.close();
			System.err.println("File doesn't exists.");
		}*/

		/*String pass;

		System.out.print("Choose a password(16 characters): ");

		while ((pass = sc.nextLine()).length() != 16)
			System.out.print("Invalid password, choose a new one(16 characters): ");

		Encryptor enc = new Encryptor(file, pass);
		enc.encrypt();
		// Decryptor dec = new Decryptor(file, pass);
		// dec.decrypt();*/

		/*SHA512CheckSum sha = new SHA512CheckSum(file);
		try {
			sha.checksum();
		} catch (Exception e) {
			e.printStackTrace();
		}*/

		/*Slicer slicer = null;
		try {
			slicer = new Slicer(file, 10);
		} catch (FileNotFoundException e) {
			System.err.println("Exception: " + e.getMessage());
			System.exit(-1);
		}

		System.out.println(slicer.getSize());
		
		long blocks = slicer.getBlocks();

		for (int i = 0; i < blocks; i++) {
		Slice slice = slicer.slice();
		System.out.println(slice.toString());
		}*/

		sc.close();
	}
}

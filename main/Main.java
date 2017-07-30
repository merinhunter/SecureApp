package main;

//import java.nio.file.Files;
import java.io.*;
import java.util.ArrayList;
//import java.util.Base64;
import java.util.Scanner;

//import org.bouncycastle.util.encoders.Hex;

//import assembler.SHA512CheckSum;

import cipher.*;
import assembler.*;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		System.out.print("Put file path: ");

		String path = sc.nextLine();
		
		File f = new File(path);
		if(f.exists() && !f.isDirectory()) {
			try {
				Slicer slicer = new Slicer(f, 10);
				ArrayList<Slice> slices = slicer.slice();
				/*for (Slice slice : slices) {
					System.out.println(slice.toString());
					System.out.println(Slice.fromBytes(slice.toBytes()).toString());
					//Slice.fromBytes(slice.toBytes());
				}*/
				Composer.compose(slices);

				Encryptor encryptor = new Encryptor();
				ArrayList<EncFile> files = encryptor.encrypt(slices);
				for (EncFile file : files) {
					System.out.println(file.toBase64());
				}
				
				Decryptor decryptor = new Decryptor(encryptor.cipher.getKey(), encryptor.cipher.getIV());
				ArrayList<Slice> decSlices = decryptor.decrypt(files);
				for (Slice slice : decSlices) {
					System.out.println(slice.toString());
				}
				Composer.compose(decSlices);
				
				System.out.println("ADIOS");
			} catch (FileNotFoundException e) {
				System.err.println("FileNotFoundException: " + e.getMessage());
				System.exit(-1);
			} catch (Exception e) {
				System.err.println("Exception4: " + e.getMessage());
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

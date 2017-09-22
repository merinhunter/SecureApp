package main;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.ArrayList;
//import java.util.Scanner;

import assembler.Slice;
import assembler.Slicer;
import cipher.EncFile;
import cipher.Encryptor;
import cipher.KeyFile;
import common.FileIO;

import rsa.Signer;

public class SliceEncrypt {
	private static String path;
	private static int blockSize;

	private final static String decomposedPath = "/home/sergio/Escritorio/decomposed/";

	public static void main(String[] args) {
		System.out.println("Slice & Encrypt");

		/*
		 * Scanner sc = new Scanner(System.in);
		 * 
		 * System.out.print("Put file path: ");
		 * 
		 * path = sc.nextLine();
		 * 
		 * System.out.println("Set block size: ");
		 * 
		 * blockSize = sc.nextInt();
		 */

		path = "/home/sergio/Escritorio/original";
		blockSize = 10;

		File f = new File(path);
		if (f.exists() && !f.isDirectory()) {
			try {
				Slicer slicer = new Slicer(f, blockSize);
				ArrayList<Slice> slices = slicer.slice();

				Signer signer = new Signer();
				signer.sign(slices);

				Encryptor encryptor = new Encryptor();
				ArrayList<EncFile> files = encryptor.encrypt(slices);

				KeyFile keyFile = new KeyFile(f, encryptor.getKeyEncoded());
				signer.sign(keyFile);

				FileIO.write(keyFile, decomposedPath);

				FileIO.write(files, decomposedPath);
			} catch (FileNotFoundException e) {
				System.err.println("FileNotFoundException: " + e.getMessage());
				System.exit(-1);
			} catch (Exception e) {
				System.err.println("Exception: " + e.getMessage());
				System.exit(-1);
			}
		}

		System.out.println("FINISH!");

		// sc.close();
	}
}

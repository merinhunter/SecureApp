package main;

import java.util.ArrayList;
//import java.util.Scanner;

import assembler.Composer;
import assembler.Slice;
import cipher.Decryptor;
import cipher.EncFile;
import cipher.KeyFile;
import common.FileIO;
import rsa.Signer;

public class DecryptCompose {
	private final static String composedPath = "/home/sergio/Escritorio/composed/";

	public static void main(String[] args) {
		System.out.println("Decrypt & Compose");

		/*
		 * Scanner sc = new Scanner(System.in);
		 * 
		 * System.out.print("Put directory path: ");
		 * 
		 * String decomposedPath = sc.nextLine();
		 */

		String decomposedPath = "/home/sergio/Escritorio/decomposed/";

		try {
			ArrayList<EncFile> files = FileIO.readEncFiles(decomposedPath);

			KeyFile keyFile = FileIO.readKeyFile(decomposedPath);

			Signer signer = new Signer();
			if (!signer.verify(keyFile))
				throw new InternalError("KeyFile invalid signature");

			Decryptor decryptor = new Decryptor(keyFile.getKey());

			ArrayList<Slice> slices = decryptor.decrypt(files);

			if (!signer.verify(slices))
				throw new InternalError("Slices invalid signature");

			Composer.compose(slices, composedPath);
		} catch (Exception e) {
			System.err.println("DecryptCompose Exception: " + e.getMessage());
			System.exit(-1);
		}

		System.out.println("FINISH!");

		// sc.close();
	}

}

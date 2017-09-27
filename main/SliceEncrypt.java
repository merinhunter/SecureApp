package main;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.ArrayList;

import assembler.Slice;
import assembler.Slicer;
import cipher.EncFile;
import cipher.EncKeyFile;
import cipher.Encryptor;
import cipher.KeyFile;
import common.FileIO;
import common.RandomString;
import rsa.Signer;

public class SliceEncrypt {
	private static String path;
	private static int blockSize;

	private final static String decomposedPath = "/home/sergio/Escritorio/decomposed/";
	private final static String usage = "java SliceEncrypt [path] [blockSize]";

	public static void main(String[] args) {
		System.out.println("Slice & Encrypt");

		if (args.length != 2)
			throw new InternalError(usage);

		path = args[0];
		blockSize = Integer.parseInt(args[1]);

		File f = new File(path);
		if (f.exists() && !f.isDirectory()) {

			try {
				RandomString random = new RandomString();
				String sessionID = random.nextString();

				Slicer slicer = new Slicer(f, blockSize, sessionID);
				ArrayList<Slice> slices = slicer.slice();

				Signer signer = new Signer();
				signer.sign(slices);

				Encryptor encryptor = new Encryptor();
				ArrayList<EncFile> files = encryptor.encrypt(slices);

				KeyFile keyFile = new KeyFile(sessionID, encryptor.getKeyEncoded());
				signer.sign(keyFile);

				EncKeyFile encKeyFile = new EncKeyFile(keyFile);

				FileIO.write(encKeyFile, decomposedPath);

				FileIO.write(files, decomposedPath);
			} catch (FileNotFoundException e) {
				System.err.println("FileNotFoundException: " + e.getMessage());
				System.exit(-1);
			} catch (Exception e) {
				System.err.println("Exception: " + e.getMessage());
				System.exit(-1);
			}

		} else {
			throw new InternalError("Specified path is not a file");
		}

		System.out.println("FINISH!");
	}
}

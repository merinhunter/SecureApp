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
import rsa.RSALibrary;
import rsa.Signer;

public class SliceEncrypt {
	private static String filePath;
	private static int blockSize;

	private final static String usage = "Usage: SliceEncrypt [filePath] [blockSize]";

	public static void main(String[] args) {
		System.out.println("Slice & Encrypt");

		if (args.length != 2)
			throw new InternalError(usage);

		filePath = args[0];
		blockSize = Integer.parseInt(args[1]);

		File f = new File(filePath);
		if (f.exists() && !f.isDirectory()) {

			try {
				RandomString random = new RandomString();
				String sessionID = random.nextString();
				String sessionPath = FileIO.sendPath + sessionID + "/";

				FileIO.makeDirectory(sessionPath);

				FileIO.append(FileIO.appPath + FileIO.listFile, sessionID + " " + filePath);

				Slicer slicer = new Slicer(f, blockSize, sessionID);
				ArrayList<Slice> slices = slicer.slice();

				Signer signer = new Signer(RSALibrary.PUBLIC_KEY_FILE);
				for (Slice slice : slices)
					signer.sign(slice);

				Encryptor encryptor = new Encryptor();
				ArrayList<EncFile> files = encryptor.encrypt(slices);
				for (EncFile file: files)
					signer.sign(file);

				KeyFile keyFile = new KeyFile(sessionID, encryptor.getKeyEncoded());
				signer.sign(keyFile);

				EncKeyFile encKeyFile = new EncKeyFile(keyFile, RSALibrary.PUBLIC_KEY_FILE);

				FileIO.write(encKeyFile, sessionPath);

				FileIO.write(files, sessionPath);
			} catch (FileNotFoundException e) {
				System.err.println("FileNotFoundException: " + e.getMessage());
				System.exit(-1);
			} catch (Exception e) {
				System.err.println("Exception: " + e.getMessage());
				System.exit(-1);
			}

		} else {
			System.err.println("Specified path is not a file");
			System.exit(-1);
		}

		System.out.println("FINISH!");
	}
}

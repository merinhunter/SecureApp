package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import assembler.Composer;
import assembler.Slice;
import cipher.Decryptor;
import cipher.EncFile;
import cipher.EncKeyFile;
import cipher.KeyFile;
import common.FileIO;
import rsa.RSALibrary;
import rsa.Signer;

public class DecryptCompose {

	private final static String usage = "Usage: DecryptCompose [sessionID]";

	private static String sessionID;

	public static void main(String[] args) {
		System.out.println("Decrypt & Compose");

		if (args.length != 1)
			throw new InternalError(usage);

		sessionID = args[0];

		String sessionPath = FileIO.appPath + sessionID + '/';
		String badFilePath = sessionPath + FileIO.badFile;
		String errorsFilePath = sessionPath + FileIO.errorsFile;

		String tmpPath = FileIO.decomposedPath + sessionID;

		ArrayList<String> errors = new ArrayList<>();
		ArrayList<String> bad = new ArrayList<>();

		File dir = new File(tmpPath);
		if (dir.exists() && dir.isDirectory()) {

			EncKeyFile encKeyFile = FileIO.readEncKeyFile(tmpPath);
			if (encKeyFile == null) {
				System.err.println("EncKeyFile is missing");
				FileIO.append(errorsFilePath, "EncKeyFile is missing");
				System.exit(-1);
			}

			KeyFile keyFile = null;
			try {
				keyFile = new KeyFile(encKeyFile);
			} catch (Exception e) {
				System.err.println("EncKeyFile is corrupted");
				FileIO.append(badFilePath, "EncKeyFile is corrupted");
				System.exit(-1);
			}

			Signer signer = new Signer(RSALibrary.PUBLIC_KEY_FILE);
			if (!signer.verify(keyFile)) {
				System.err.println("KeyFile has invalid signature");
				bad.add("KeyFile has invalid signature");
			}

			ArrayList<EncFile> files = new ArrayList<>();
			try {
				files = FileIO.readEncFiles(tmpPath);
			} catch (IOException e) {
				System.err.println("Error reading EncFiles");
				errors.add("Error reading EncFiles");
			}

			Decryptor decryptor = new Decryptor(keyFile.getKey());

			ArrayList<Slice> slices = new ArrayList<>();
			for (EncFile file : files) {
				if (!signer.verify(file)) {
					System.err.println("IV of EncFile " + new String(file.getID()) + " is corrupted");
					bad.add("IV of EncFile " + new String(file.getID()) + " is corrupted");
				}

				Slice slice = decryptor.decrypt(file);

				if (slice == null) {
					System.err.println("EncFile " + new String(file.getID()) + " is corrupted");
					bad.add("EncFile " + new String(file.getID()) + " is corrupted");
					continue;
				}

				if (!signer.verify(slice)) {
					System.err.println("Slice " + slice.getHeader().getIndex() + " has invalid signature");
					bad.add("Slice " + slice.getHeader().getIndex() + " has invalid signature");
				}

				slices.add(slice);
			}

			Composer composer = new Composer(sessionID);
			if (!slices.isEmpty())
				composer.compose(slices);

			ArrayList<String> errorsComposer = new ArrayList<>();
			if (composer.hasErrors()) {
				errorsComposer = composer.getErrors();
				for (String error : errorsComposer)
					errors.add(error);
			}

		} else {
			System.err.println("There is not a path for the specified session ID");
			System.exit(-1);
		}

		if (!errors.isEmpty()) {
			System.err.println("Errors have occurred during the process");
			for (String m : errors)
				FileIO.append(errorsFilePath, m);
		}

		if (!bad.isEmpty()) {
			System.err.println("Some files are corrupted or have not passed the necessary security measures");
			for (String m : bad)
				FileIO.append(badFilePath, m);
		}

		System.out.println("FINISH!");
	}

}

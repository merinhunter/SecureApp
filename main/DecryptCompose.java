package main;

import java.util.ArrayList;

import assembler.Composer;
import assembler.Slice;
import cipher.Decryptor;
import cipher.EncFile;
import cipher.EncKeyFile;
import cipher.KeyFile;
import common.FileIO;
import rsa.Signer;

public class DecryptCompose {
	private final static String composedPath = "/home/sergio/Escritorio/composed/";
	private final static String usage = "java DecryptCompose [path]";

	private static String decomposedPath;

	public static void main(String[] args) {
		System.out.println("Decrypt & Compose");

		if (args.length != 1)
			throw new InternalError(usage);

		decomposedPath = args[0];

		try {
			EncKeyFile encKeyFile = FileIO.readEncKeyFile(decomposedPath);

			KeyFile keyFile = new KeyFile(encKeyFile);

			Signer signer = new Signer();
			if (!signer.verify(keyFile))
				throw new InternalError("KeyFile invalid signature");

			ArrayList<EncFile> files = FileIO.readEncFiles(decomposedPath);

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
	}

}

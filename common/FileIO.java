package common;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import cipher.EncFile;
import cipher.KeyFile;

public class FileIO {

	/**
	 * Writes an ArrayList of EncFile in a specific path.
	 */
	public static void write(ArrayList<EncFile> files, String destPath) throws Exception {
		RandomString random = new RandomString(8);

		for (EncFile file : files) {
			FileOutputStream output;

			while (true) {
				String fileName = random.nextString();
				String filePath = destPath + fileName;
				File f = new File(filePath);

				if (!f.exists() && !f.isDirectory()) {
					output = new FileOutputStream(f);
					break;
				}
			}

			output.write(file.toBase64(), 0, file.toBase64().length);

			if (output != null)
				output.close();
		}
	}

	/**
	 * Writes a KeyFile in a specific path.
	 */
	public static void write(KeyFile keyFile, String destPath) throws Exception {
		RandomString random = new RandomString(8);
		FileOutputStream output;

		while (true) {
			String fileName = random.nextString();
			String filePath = destPath + fileName + ".key";
			File f = new File(filePath);

			if (!f.exists() && !f.isDirectory()) {
				output = new FileOutputStream(f);
				break;
			}
		}

		output.write(keyFile.toBase64(), 0, keyFile.toBase64().length);

		if (output != null)
			output.close();
	}

	/**
	 * Read an ArrayList of EncFile from a specific path.
	 */
	public static ArrayList<EncFile> readEncFiles(String originPath) throws Exception {
		ArrayList<EncFile> files = new ArrayList<>();

		File folder = new File(originPath);
		File[] listOfFiles = folder.listFiles();
		Path path = null;

		for (File file : listOfFiles) {
			if (file.isFile() && !file.getName().endsWith(".key")) {
				path = Paths.get(file.getAbsolutePath());

				byte[] data = Files.readAllBytes(path);
				files.add(EncFile.fromBase64(data));
			}
		}

		return files;
	}

	/**
	 * Read a KeyFile from a specific path.
	 */
	public static KeyFile readKeyFile(String originPath) throws Exception {
		File folder = new File(originPath);
		File[] listOfFiles = folder.listFiles();
		Path path = null;

		for (File file : listOfFiles) {
			if (file.isFile() && file.getName().endsWith(".key")) {
				path = Paths.get(file.getAbsolutePath());
				break;
			}
		}

		byte[] data = Files.readAllBytes(path);

		return KeyFile.fromBase64(data);
	}
}

package cipher;

import java.util.ArrayList;

import assembler.Slice;

public class Encryptor {
	public SymmetricCipher cipher;

	public Encryptor() {
		cipher = new SymmetricCipher();
	}

	public ArrayList<EncFile> encrypt(ArrayList<Slice> slices) {
		ArrayList<EncFile> files = new ArrayList<>();

		for (Slice slice : slices) {
			EncFile file = new EncFile(cipher.encrypt(slice.toBytes()));
			file.setIv(cipher.getIV());

			files.add(file);
		}

		return files;
	}
}
package cipher;

import java.util.ArrayList;

import javax.crypto.spec.IvParameterSpec;

import assembler.Slice;

public class Encryptor {
	public SymmetricCipher cipher;
	private AESLibrary aes;

	public Encryptor() {
		aes = new AESLibrary();
		cipher = new SymmetricCipher(aes.generateSymmetricKey());
	}

	public ArrayList<EncFile> encrypt(ArrayList<Slice> slices) {
		ArrayList<EncFile> files = new ArrayList<>();

		for (Slice slice : slices) {
			IvParameterSpec iv = aes.generateIV();
			EncFile file = new EncFile(cipher.encrypt(slice.toBytes(), iv), iv.getIV());

			files.add(file);
		}

		return files;
	}
}
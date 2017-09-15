package cipher;

import java.util.ArrayList;

import javax.crypto.spec.IvParameterSpec;

import assembler.Slice;

public class Decryptor {
	private SymmetricCipher cipher;
	private AESLibrary aes;

	public Decryptor(byte[] keyBytes) {
		aes = new AESLibrary();
		cipher = new SymmetricCipher(aes.generateSymmetricKey(keyBytes));
		//cipher = new SymmetricCipher(keyBytes);
	}

	public ArrayList<Slice> decrypt(ArrayList<EncFile> files) {
		ArrayList<Slice> slices = new ArrayList<>();

		for (EncFile file : files) {
			IvParameterSpec iv = aes.generateIV(file.getIv());
			System.out.println("TRAZA");
			Slice slice = Slice.fromBytes(cipher.decrypt(file.getData(), iv));
			slices.add(slice);
		}

		return slices;
	}
}

package cipher;

import java.util.ArrayList;

import assembler.Slice;

public class Decryptor {
	private SymmetricCipher cipher;

	public Decryptor(byte[] key, byte[] iv) {
		cipher = new SymmetricCipher(key, iv);
	}

	public ArrayList<Slice> decrypt(ArrayList<EncFile> files) {
		ArrayList<Slice> slices = new ArrayList<>();

		for (EncFile file : files) {
			Slice slice = Slice.fromBytes(cipher.decrypt(file.getData()));

			slices.add(slice);
		}

		return slices;
	}
}

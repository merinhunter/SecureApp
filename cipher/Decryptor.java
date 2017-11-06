package cipher;

import javax.crypto.spec.IvParameterSpec;

import assembler.Slice;

public class Decryptor {
	private SymmetricCipher cipher;
	private AESLibrary aes;

	public Decryptor(byte[] keyBytes) {
		aes = new AESLibrary();
		cipher = new SymmetricCipher(aes.generateSymmetricKey(keyBytes));
	}

	public Slice decrypt(EncFile file) {
		IvParameterSpec iv = aes.generateIV(file.getIv());
		byte[] decrypted = cipher.decrypt(file.getData(), iv);

		if (decrypted == null)
			return null;

		return Slice.fromBytes(decrypted);
	}
}

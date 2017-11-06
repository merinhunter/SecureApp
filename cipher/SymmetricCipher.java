package cipher;

import javax.crypto.*;
import javax.crypto.spec.*;

public class SymmetricCipher {
	private final String ALGORITHM = "AES/CBC/PKCS5Padding";
	private final String PROVIDER = "BC";

	private SecretKeySpec key;

	public SymmetricCipher(byte[] keyBytes) {
		key = new SecretKeySpec(keyBytes, ALGORITHM);
	}

	public SymmetricCipher(SecretKeySpec key) {
		this.key = key;
	}

	public byte[] getKey() {
		return key.getEncoded();
	}

	public byte[] encrypt(byte[] plaintext, IvParameterSpec iv) {
		byte[] ciphertext = null;

		try {
			final Cipher cipher = Cipher.getInstance(ALGORITHM, PROVIDER);
			cipher.init(Cipher.ENCRYPT_MODE, key, iv);

			ciphertext = cipher.doFinal(plaintext);
		} catch (Exception e) {
			System.err.println("Encrypt exception: " + e.getMessage());
			return null;
		}

		return ciphertext;
	}

	public byte[] decrypt(byte[] ciphertext, IvParameterSpec iv) {
		byte[] plaintext = null;

		try {
			final Cipher cipher = Cipher.getInstance(ALGORITHM, PROVIDER);
			cipher.init(Cipher.DECRYPT_MODE, key, iv);

			plaintext = cipher.doFinal(ciphertext);
		} catch (Exception e) {
			System.err.println("Decrypt exception: " + e.getMessage());
			return null;
		}

		return plaintext;
	}
}

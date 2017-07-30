package cipher;

import javax.crypto.*;
import javax.crypto.spec.*;

public class SymmetricCipher {
	private final String ALGORITHM = "AES";
	private final String MODE_OF_OPERATION = "AES/CBC/PKCS5Padding";
	private final String PROVIDER = "BC";

	private AESLibrary aes;
	private Cipher aesEnc, aesDec;
	private SecretKeySpec key;
	private IvParameterSpec iv;

	public SymmetricCipher() {
		aes = new AESLibrary();

		key = aes.generateSymmetricKey();
		iv = aes.generateIV();

		// System.out.println(Hex.toHexString(iv.getIV()));
		// System.out.println(new String(iv.toString()));

		initCiphers();
	}

	public SymmetricCipher(byte[] keyBytes, byte[] ivBytes) {
		key = new SecretKeySpec(keyBytes, ALGORITHM);
		iv = new IvParameterSpec(ivBytes);

		initCiphers();
	}

	public byte[] getKey() {
		return key.getEncoded();
	}

	public byte[] getIV() {
		return iv.getIV();
	}

	private void initCiphers() {
		try {
			aesEnc = Cipher.getInstance(MODE_OF_OPERATION, PROVIDER);
			aesEnc.init(Cipher.ENCRYPT_MODE, key, iv);

			aesDec = Cipher.getInstance(MODE_OF_OPERATION, PROVIDER);
			aesDec.init(Cipher.DECRYPT_MODE, key, iv);
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
			System.exit(-1);
		}
	}

	public byte[] encrypt(byte[] data) {
		try {
			return aesEnc.doFinal(data);
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
			System.exit(-1);
		}

		return null;
	}

	public byte[] decrypt(byte[] data) {
		try {
			return aesDec.doFinal(data);
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
			System.exit(-1);
		}

		return null;
	}
}

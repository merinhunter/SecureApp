package cipher;

import javax.crypto.*;
import javax.crypto.spec.*;

import org.bouncycastle.util.encoders.Hex;

public class SymmetricCipher {
	//private final String ALGORITHM = "AES";
	private final String ALGORITHM = "AES/CBC/PKCS5Padding";
	private final String PROVIDER = "BC";

	//private AESLibrary aes;
	//private Cipher aesEnc, aesDec;
	private SecretKeySpec key;
	//private IvParameterSpec iv;

	/*public SymmetricCipher() {
		aes = new AESLibrary();

		key = aes.generateSymmetricKey();
		//iv = aes.generateIV();

		// System.out.println(Hex.toHexString(iv.getIV()));
		// System.out.println(new String(iv.toString()));

		//initCiphers();
	}*/

	/*public SymmetricCipher(byte[] keyBytes, byte[] ivBytes) {
		key = new SecretKeySpec(keyBytes, ALGORITHM);
		iv = new IvParameterSpec(ivBytes);

		initCiphers();
	}*/
	
	public SymmetricCipher(byte[] keyBytes) {
		//aes = new AESLibrary();
		key = new SecretKeySpec(keyBytes, ALGORITHM);
		//iv = new IvParameterSpec(ivBytes);

		//initCiphers();
	}
	
	public SymmetricCipher(SecretKeySpec key) {
		this.key = key;
	}

	public byte[] getKey() {
		return key.getEncoded();
	}

	/*public byte[] getIV() {
		return iv.getIV();
	}*/

	/*private void initCiphers() {
		try {
			aesEnc = Cipher.getInstance(MODE_OF_OPERATION, PROVIDER);
			aesEnc.init(Cipher.ENCRYPT_MODE, key, iv);

			aesDec = Cipher.getInstance(MODE_OF_OPERATION, PROVIDER);
			aesDec.init(Cipher.DECRYPT_MODE, key, iv);
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
			System.exit(-1);
		}
	}*/

	/*public byte[] encrypt(byte[] data) {
		try {
			return aesEnc.doFinal(data);
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
			System.exit(-1);
		}

		return null;
	}*/
	
	public byte[] encrypt(byte[] plaintext, IvParameterSpec iv) {
		byte[] ciphertext = null;

		try {
			final Cipher cipher = Cipher.getInstance(ALGORITHM, PROVIDER);
			//final IvParameterSpec iv = aes.generateIV();
			cipher.init(Cipher.ENCRYPT_MODE, key, iv);

			//cipher.update(plaintext);
			ciphertext = cipher.doFinal(plaintext);
			System.out.println("Bloque cifrado en hex:" + Hex.toHexString(ciphertext));
			System.out.println("IV encrypt hex: " + Hex.toHexString(iv.getIV()));
			System.out.println("Key encrypt hex: " + Hex.toHexString(key.getEncoded()));
		} catch (Exception e) {
			System.err.println("Encrypt exception: " + e.getMessage());
			System.exit(-1);
		}

		return ciphertext;
	}

	/*public byte[] decrypt(byte[] data) {
		try {
			return aesDec.doFinal(data);
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
			System.exit(-1);
		}

		return null;
	}*/

	public byte[] decrypt(byte[] ciphertext, IvParameterSpec iv) {
		byte[] plaintext = null;

		try {
			final Cipher cipher = Cipher.getInstance(ALGORITHM, PROVIDER);
			//final IvParameterSpec iv = aes.generateIV(ivBytes);
			cipher.init(Cipher.DECRYPT_MODE, key, iv);

			plaintext = cipher.doFinal(ciphertext);
		} catch (Exception e) {
			System.out.println(ALGORITHM + "|" + PROVIDER);
			System.out.println("IV decrypt hex: " + Hex.toHexString(iv.getIV()));
			System.out.println("Ciphertext hex: " + Hex.toHexString(ciphertext));
			System.out.println("Ciphertext length: " + ciphertext.length);
			System.out.println("Key decrypt hex: " + Hex.toHexString(key.getEncoded()));
			System.err.println("Decrypt exception: " + e.getMessage());
			System.err.println("Decrypt exception:");
			e.printStackTrace();
			System.exit(-1);
		}

		return plaintext;
	}
}

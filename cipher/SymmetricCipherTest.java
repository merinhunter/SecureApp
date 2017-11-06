package cipher;

import static org.junit.Assert.*;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.junit.Test;

public class SymmetricCipherTest {

	@Test
	public void generateRandomAESKey() {
		AESLibrary aes = new AESLibrary();
		SecretKeySpec key = aes.generateSymmetricKey();

		assertEquals("AES/CBC/PKCS5Padding", key.getAlgorithm());
		assertEquals(AESLibrary.KEY_SIZE, key.getEncoded().length);
	}

	@Test
	public void encryptWithAES() throws Exception {
		byte[] message = "Alice knows Bob's secret.".getBytes();
		AESLibrary aes = new AESLibrary();

		SecretKeySpec key = aes.generateSymmetricKey();
		IvParameterSpec iv = aes.generateIV();

		SymmetricCipher cipher = new SymmetricCipher(key.getEncoded());
		byte[] ciphertext = cipher.encrypt(message, iv);

		byte[] plaintext = cipher.decrypt(ciphertext, iv);

		assertEquals(new String(message), new String(plaintext));
	}

}

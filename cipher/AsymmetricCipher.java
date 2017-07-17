package cipher;

import java.io.*;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.*;
import javax.crypto.spec.*;

public class AsymmetricCipher {
	final int AES_BLOCK_SIZE = 16;
	final String ALGORITHM = "AES";
	final String MODE_OF_OPERATION = "AES/CBC/PKCS5Padding";
	final String PROVIDER = "BC";

	Cipher aesEnc, aesDec;
	SecretKeySpec key;
	AlgorithmParameterSpec iv;

	public AsymmetricCipher(byte[] byteKey) throws InvalidKeyException {

		SecureRandom random = new SecureRandom();
		byte byteIV[] = new byte[AES_BLOCK_SIZE];
		random.nextBytes(byteIV);

		try {
			key = new SecretKeySpec(byteKey, ALGORITHM);
			iv = new IvParameterSpec(byteIV);

			aesEnc = Cipher.getInstance(MODE_OF_OPERATION, PROVIDER);
			aesEnc.init(Cipher.ENCRYPT_MODE, key, iv);

			aesDec = Cipher.getInstance(MODE_OF_OPERATION, PROVIDER);
			aesDec.init(Cipher.DECRYPT_MODE, key, iv);

		} catch (NoSuchAlgorithmException e) {
			System.err.println("Exception: " + e.getMessage());
			System.exit(-1);

		} catch (NoSuchProviderException e) {
			System.err.println("Exception: " + e.getMessage());
			System.exit(-1);

		} catch (NoSuchPaddingException e) {
			System.err.println("Exception: " + e.getMessage());
			System.exit(-1);
		
		} catch (InvalidAlgorithmParameterException e){
			System.err.println("Exception: " + e.getMessage());
			System.exit(-1);
		}
	}

	public byte[] encryptCBC(InputStream in, OutputStream out)
			throws IOException, ShortBufferException, IllegalBlockSizeException, BadPaddingException {
		// Put the IV at the beggining of the cipher file
		out.write(IV, 0, IV.length);

		byte[] buffer = new byte[AES_BLOCK_SIZE];
		int nBytes = 0;
		byte[] cipherBlock = new byte[aesEnc.getOutputSize(buffer.length)];
		int cipherBytes;
		while ((nBytes = in.read(buffer)) != -1) {
			cipherBytes = aesEnc.update(buffer, 0, nBytes, cipherBlock);
			out.write(cipherBlock, 0, cipherBytes);
		}
		// Always call doFinal
		cipherBytes = aesEnc.doFinal(cipherBlock, 0);
		out.write(cipherBlock, 0, cipherBytes);

		out.close();
		in.close();
	}

	public void CBCDecrypt(InputStream in, OutputStream out) throws IOException, ShortBufferException,
			IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException {
		// Get the IV from the file
		in.read(IV, 0, IV.length);
		this.InitCiphers();

		byte[] buffer = new byte[AES_BLOCK_SIZE];
		int nBytes = 0;
		byte[] cipherBlock = new byte[aesDec.getOutputSize(buffer.length)];
		int cipherBytes;
		while ((nBytes = in.read(buffer)) != -1) {
			cipherBytes = aesDec.update(buffer, 0, nBytes, cipherBlock);
			out.write(cipherBlock, 0, cipherBytes);
		}
		// Always call doFinal
		cipherBytes = aesDec.doFinal(cipherBlock, 0);
		out.write(cipherBlock, 0, cipherBytes);

		out.close();
		in.close();
	}
}

package cipher;

import java.security.PublicKey;

import javax.crypto.spec.IvParameterSpec;

import common.Bytes;
import rsa.RSALibrary;

public class EncKeyFile {
	private byte[] iv, header, encKey;

	public final static int IV_SIZE = AESLibrary.KEY_SIZE;

	public EncKeyFile(KeyFile keyFile) throws Exception {
		AESLibrary aes = new AESLibrary();
		SymmetricCipher cipher = new SymmetricCipher(keyFile.getKey());

		IvParameterSpec iv = aes.generateIV();
		this.iv = iv.getIV();

		this.header = cipher.encrypt(Bytes.concat(keyFile.getSessionID(), keyFile.getSignature().getSignature()), iv);

		PublicKey pubKey = (PublicKey) RSALibrary.getKey(RSALibrary.PUBLIC_KEY_FILE);
		this.encKey = RSALibrary.encrypt(keyFile.getKey(), pubKey);
	}

	public EncKeyFile(byte[] iv, byte[] header, byte[] encKey) {
		this.iv = iv;
		this.header = header;
		this.encKey = encKey;
	}

	public byte[] getIv() {
		return iv;
	}

	public byte[] getHeader() {
		return header;
	}

	public byte[] getEncKey() {
		return encKey;
	}

	public byte[] toBytes() {
		return Bytes.concat(iv, header, encKey);
	}

	public static EncKeyFile fromBytes(byte[] src) {
		byte[] iv = new byte[IV_SIZE];
		byte[] header = new byte[src.length - IV_SIZE - RSALibrary.KEY_SIZE / 8];
		byte[] encKey = new byte[RSALibrary.KEY_SIZE / 8];

		System.arraycopy(src, 0, iv, 0, IV_SIZE);
		System.arraycopy(src, IV_SIZE, header, 0, header.length);
		System.arraycopy(src, IV_SIZE + header.length, encKey, 0, RSALibrary.KEY_SIZE / 8);

		return new EncKeyFile(iv, header, encKey);
	}
}

package cipher;

import java.security.PrivateKey;
import java.util.Base64;

import common.Bytes;
import common.RandomString;
import rsa.RSALibrary;
import rsa.Signature;

public class KeyFile {
	private byte[] key;
	private byte[] sessionID;
	private Signature signature;
	public final static int KEY_FILE_SIZE = AESLibrary.KEY_SIZE + RandomString.DEFAULT_SIZE + Signature.BYTES;

	public KeyFile(byte[] sessionID, byte[] key, Signature signature) {
		this.sessionID = sessionID;
		this.key = key;
		this.signature = signature;
	}

	public KeyFile(String sessionID, byte[] key) {
		this.sessionID = sessionID.getBytes();
		this.key = key;
	}

	public KeyFile(EncKeyFile encKeyFile) throws Exception {
		PrivateKey privKey = null;
		try {
			privKey = (PrivateKey) RSALibrary.getKey(RSALibrary.PRIVATE_KEY_FILE);
		} catch (Exception e) {
			System.err.println("Error getting the private key: " + e.getMessage());
			System.exit(-1);
		}

		this.key = RSALibrary.decrypt(encKeyFile.getEncKey(), privKey);
		if(key == null) {
			throw new Exception("EncKeyFile is corrupted");
		}

		AESLibrary aes = new AESLibrary();
		SymmetricCipher cipher = new SymmetricCipher(aes.generateSymmetricKey(this.key));
		byte[] header = cipher.decrypt(encKeyFile.getHeader(), aes.generateIV(encKeyFile.getIv()));

		byte[] sessionID = new byte[RandomString.DEFAULT_SIZE];
		System.arraycopy(header, 0, sessionID, 0, RandomString.DEFAULT_SIZE);
		this.sessionID = sessionID;

		byte[] signature = new byte[Signature.BYTES];
		System.arraycopy(header, RandomString.DEFAULT_SIZE, signature, 0, Signature.BYTES);
		this.signature = new Signature(signature);
	}

	public byte[] getSessionID() {
		return sessionID;
	}

	public byte[] getKey() {
		return key;
	}

	public Signature getSignature() {
		return signature;
	}

	public void setSignature(Signature signature) {
		this.signature = signature;
	}

	public byte[] toBytes() {
		return Bytes.concat(key, sessionID, signature.getSignature());
	}

	public static KeyFile fromBytes(byte[] src) {
		byte[] key = new byte[AESLibrary.KEY_SIZE];
		byte[] sessionID = new byte[RandomString.DEFAULT_SIZE];
		byte[] byteSignature = new byte[Signature.BYTES];

		System.arraycopy(src, 0, key, 0, AESLibrary.KEY_SIZE);
		System.arraycopy(src, AESLibrary.KEY_SIZE, sessionID, 0, RandomString.DEFAULT_SIZE);
		System.arraycopy(src, AESLibrary.KEY_SIZE + RandomString.DEFAULT_SIZE, byteSignature, 0, Signature.BYTES);

		Signature signature = new Signature(byteSignature);

		return new KeyFile(sessionID, key, signature);
	}

	public byte[] toBase64() {
		return Base64.getEncoder().encode(this.toBytes());
	}

	public static KeyFile fromBase64(byte[] base64) {
		return fromBytes(Base64.getDecoder().decode(base64));
	}
}

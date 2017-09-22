package cipher;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import assembler.SHA512CheckSum;
import common.Bytes;
import rsa.RSALibrary;
import rsa.Signature;

public class KeyFile {
	private byte[] id;
	private byte[] encKey;
	private Signature signature;
	public final static int KEY_FILE_SIZE = SHA512CheckSum.SHA512_SIZE + RSALibrary.KEY_SIZE / 8 + Signature.BYTES;

	public KeyFile(byte[] id, byte[] encKey, Signature signature) {
		this.id = id;
		this.encKey = encKey;
		this.signature = signature;
	}

	public KeyFile(File file, byte[] key) throws FileNotFoundException, Exception {
		this.id = SHA512CheckSum.checksum(file);

		PublicKey pubKey = (PublicKey) RSALibrary.getKey(RSALibrary.PUBLIC_KEY_FILE);
		this.encKey = RSALibrary.encrypt(key, pubKey);
	}

	public byte[] getId() {
		return id;
	}

	public byte[] getKey() throws Exception {
		PrivateKey privKey = (PrivateKey) RSALibrary.getKey(RSALibrary.PRIVATE_KEY_FILE);
		return RSALibrary.decrypt(encKey, privKey);
	}

	public byte[] getEncKey() {
		return encKey;
	}

	public Signature getSignature() {
		return signature;
	}

	public void setSignature(Signature signature) {
		this.signature = signature;
	}

	public byte[] toBytes() {
		return Bytes.concat(id, encKey, signature.getSignature());
	}

	public static KeyFile fromBytes(byte[] src) {
		byte[] id = new byte[SHA512CheckSum.SHA512_SIZE];
		byte[] encKey = new byte[RSALibrary.KEY_SIZE / 8];
		byte[] byteSignature = new byte[Signature.BYTES];

		System.arraycopy(src, 0, id, 0, SHA512CheckSum.SHA512_SIZE);
		System.arraycopy(src, SHA512CheckSum.SHA512_SIZE, encKey, 0, RSALibrary.KEY_SIZE / 8);
		System.arraycopy(src, SHA512CheckSum.SHA512_SIZE + RSALibrary.KEY_SIZE / 8, byteSignature, 0, Signature.BYTES);

		Signature signature = new Signature(byteSignature);

		return new KeyFile(id, encKey, signature);
	}

	public byte[] toBase64() {
		return Base64.getEncoder().encode(this.toBytes());
	}

	public static KeyFile fromBase64(byte[] base64) {
		return fromBytes(Base64.getDecoder().decode(base64));
	}
}

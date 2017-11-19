package cipher;

import java.nio.ByteBuffer;
import java.security.PublicKey;

//import javax.crypto.spec.IvParameterSpec;

import common.Bytes;
import rsa.RSALibrary;
import rsa.SecureSignature;

public class EncKeyFile {
	private EncKeyFileHeader header;
	//private byte[] iv, header, encKey;
	private byte[] encKey;

	//public final static int IV_SIZE = AESLibrary.KEY_SIZE;

	public EncKeyFile(KeyFile keyFile, String pubKeyPath) throws Exception {
		//AESLibrary aes = new AESLibrary();
		//SymmetricCipher cipher = new SymmetricCipher(keyFile.getKey());

		//IvParameterSpec iv = aes.generateIV();
		//this.iv = iv.getIV();

		//this.header = cipher.encrypt(Bytes.concat(keyFile.getSessionID(), keyFile.getSignature().getSignature()), iv);

		//PublicKey pubKey = (PublicKey) RSALibrary.getKey(pubKeyPath);

		PublicKey pubKey = null;

		try {
			pubKey = (PublicKey) RSALibrary.getKey(pubKeyPath);
		} catch (Exception e) {
			System.err.println("Error getting the public key: " + e.getMessage());
			System.exit(-1);
		}

		this.header = new EncKeyFileHeader();
		this.header.setID(keyFile.getSessionID());
		this.header.setSignature(new SecureSignature(keyFile.getSignature(), pubKey));

		this.encKey = RSALibrary.encrypt(keyFile.getKey(), pubKey);
	}

	/*public EncKeyFile(byte[] iv, byte[] header, byte[] encKey) {
		this.iv = iv;
		this.header = header;
		this.encKey = encKey;
	}*/

	public EncKeyFile(EncKeyFileHeader header, byte[] encKey) {
		this.header = header;
		this.encKey = encKey;
	}

	/*public byte[] getIv() {
		return iv;
	}*/

	/*public byte[] getHeader() {
		return header;
	}*/

	public byte[] getID() {
		return this.header.getID();
	}

	public void setID(byte[] id) {
		this.header.setID(id);
	}

	public SecureSignature getSignature() {
		return this.header.getSignature();
	}

	public void setSignature(SecureSignature signature) {
		this.header.setSignature(signature);
	}

	public byte[] getEncKey() {
		return encKey;
	}

	public byte[] toBytes() {
		//return Bytes.concat(iv, header, encKey);
		return Bytes.concat(this.header.toBytes(), this.encKey);
	}

	public static EncKeyFile fromBytes(byte[] src) {
		/*byte[] iv = new byte[IV_SIZE];
		byte[] header = new byte[src.length - IV_SIZE - RSALibrary.KEY_SIZE / 8];
		byte[] encKey = new byte[RSALibrary.KEY_SIZE / 8];

		System.arraycopy(src, 0, iv, 0, IV_SIZE);
		System.arraycopy(src, IV_SIZE, header, 0, header.length);
		System.arraycopy(src, IV_SIZE + header.length, encKey, 0, RSALibrary.KEY_SIZE / 8);

		return new EncKeyFile(iv, header, encKey);*/

		ByteBuffer buffer = ByteBuffer.wrap(src);
		byte[] encKey = new byte[src.length - EncKeyFileHeader.BYTES];

		EncKeyFileHeader header = EncKeyFileHeader.fromBytes(buffer);
		buffer.get(encKey);

		return new EncKeyFile(header, encKey);
	}
}

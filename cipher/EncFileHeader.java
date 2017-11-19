package cipher;

import java.nio.ByteBuffer;

import common.Bytes;
import common.RandomString;
import rsa.SecureSignature;

public class EncFileHeader {
	private byte[] ID;
	private byte[] IV;
	private SecureSignature IV_signature;

	public final static int BYTES = RandomString.DEFAULT_SIZE + AESLibrary.KEY_SIZE + SecureSignature.BYTES;

	public EncFileHeader(byte[] iv) {
		this.IV = iv;
	}

	public SecureSignature getIV_signature() {
		return IV_signature;
	}

	public void setIV_signature(SecureSignature iV_signature) {
		IV_signature = iV_signature;
	}

	public byte[] getID() {
		return ID;
	}

	public void setID(byte[] id) {
		ID = id;
	}

	public byte[] getIV() {
		return IV;
	}

	public void setIV(byte[] iv) {
		IV = iv;
	}

	public byte[] toBytes() {
		return Bytes.concat(this.ID, this.IV, this.IV_signature.getSignature());
	}

	public static EncFileHeader fromBytes(ByteBuffer buffer) {
		byte[] ID = new byte[RandomString.DEFAULT_SIZE];
		byte[] IV = new byte[AESLibrary.KEY_SIZE];
		byte[] signature = new byte[SecureSignature.BYTES];

		buffer.get(ID);
		buffer.get(IV);
		buffer.get(signature);

		EncFileHeader header = new EncFileHeader(IV);
		header.setID(ID);
		header.setIV_signature(new SecureSignature(signature));

		return header;
	}

	@Override
	public String toString() {
		return new String(this.ID) + "|" + new String(this.IV) + "|" + this.IV_signature.toString();
	}
}

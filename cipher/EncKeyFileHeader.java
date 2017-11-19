package cipher;

import java.nio.ByteBuffer;

import common.Bytes;
import common.RandomString;
import rsa.SecureSignature;

public class EncKeyFileHeader {
	private byte[] ID;
	private SecureSignature signature;

	public final static int BYTES = RandomString.DEFAULT_SIZE + SecureSignature.BYTES;

	public EncKeyFileHeader() {
	}

	public byte[] getID() {
		return ID;
	}

	public void setID(byte[] id) {
		ID = id;
	}

	public SecureSignature getSignature() {
		return signature;
	}

	public void setSignature(SecureSignature signature) {
		this.signature = signature;
	}

	public byte[] toBytes() {
		return Bytes.concat(this.ID, this.signature.getSignature());
	}

	public static EncKeyFileHeader fromBytes(ByteBuffer buffer) {
		byte[] ID = new byte[RandomString.DEFAULT_SIZE];
		byte[] signature = new byte[SecureSignature.BYTES];

		buffer.get(ID);
		buffer.get(signature);

		EncKeyFileHeader header = new EncKeyFileHeader();
		header.setID(ID);
		header.setSignature(new SecureSignature(signature));

		return header;
	}

	@Override
	public String toString() {
		return new String(this.ID) + "|" + this.signature.toString();
	}
}

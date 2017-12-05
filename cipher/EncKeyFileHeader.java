package cipher;

import java.nio.ByteBuffer;

import rsa.SecureSignature;

public class EncKeyFileHeader {
	private SecureSignature signature;

	public final static int BYTES = SecureSignature.BYTES;

	public EncKeyFileHeader() {
	}

	public SecureSignature getSignature() {
		return signature;
	}

	public void setSignature(SecureSignature signature) {
		this.signature = signature;
	}

	public byte[] toBytes() {
		return this.signature.getSignature();
	}

	public static EncKeyFileHeader fromBytes(ByteBuffer buffer) {
		byte[] signature = new byte[SecureSignature.BYTES];

		buffer.get(signature);

		EncKeyFileHeader header = new EncKeyFileHeader();
		header.setSignature(new SecureSignature(signature));

		return header;
	}

	@Override
	public String toString() {
		return this.signature.toString();
	}
}

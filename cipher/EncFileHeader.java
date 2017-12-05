package cipher;

import java.nio.ByteBuffer;

import common.Bytes;
import rsa.SecureSignature;

public class EncFileHeader {
	private byte[] IV;
	private SecureSignature IV_signature;

	public final static int BYTES = AESLibrary.KEY_SIZE + SecureSignature.BYTES;

	public EncFileHeader(byte[] iv) {
		this.IV = iv;
	}

	public SecureSignature getIV_signature() {
		return IV_signature;
	}

	public void setIV_signature(SecureSignature iV_signature) {
		IV_signature = iV_signature;
	}

	public byte[] getIV() {
		return IV;
	}

	public void setIV(byte[] iv) {
		IV = iv;
	}

	public byte[] toBytes() {
		return Bytes.concat(this.IV, this.IV_signature.getSignature());
	}

	public static EncFileHeader fromBytes(ByteBuffer buffer) {
		byte[] IV = new byte[AESLibrary.KEY_SIZE];
		byte[] signature = new byte[SecureSignature.BYTES];

		buffer.get(IV);
		buffer.get(signature);

		EncFileHeader header = new EncFileHeader(IV);
		header.setIV_signature(new SecureSignature(signature));

		return header;
	}

	@Override
	public String toString() {
		return new String(this.IV) + "|" + this.IV_signature.toString();
	}
}

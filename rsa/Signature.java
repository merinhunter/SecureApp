package rsa;

public class Signature {
	private byte[] signature;
	public static final int BYTES = 512;

	public Signature(byte[] signature) {
		this.signature = signature;
	}

	public byte[] getSignature() {
		return signature;
	}
}

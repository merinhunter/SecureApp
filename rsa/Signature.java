package rsa;

import java.security.PrivateKey;

import org.bouncycastle.util.encoders.Hex;

public class Signature {
	private byte[] signature;
	public static final int BYTES = 512;

	public Signature(byte[] signature) {
		this.signature = signature;
	}

	public Signature(SecureSignature secsignature, PrivateKey privKey) {
		this.signature = RSALibrary.decrypt(secsignature.getSignature(), privKey);
	}

	public byte[] getSignature() {
		return signature;
	}

	@Override
	public String toString() {
		return Hex.toHexString(this.signature);
	}
}

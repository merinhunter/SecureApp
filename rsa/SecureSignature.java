package rsa;

import java.security.PublicKey;

import org.bouncycastle.util.encoders.Hex;

public class SecureSignature {
	private byte[] secsignature;
	public static final int BYTES = 512;

	public SecureSignature(Signature signature, PublicKey pubKey) {
		this.secsignature = RSALibrary.encrypt(signature.getSignature(), pubKey);
	}

	public SecureSignature(byte[] secsignature) {
		this.secsignature = secsignature;
	}

	public byte[] getSignature() {
		return secsignature;
	}

	@Override
	public String toString() {
		return Hex.toHexString(this.secsignature);
	}
}

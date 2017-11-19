package rsa;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.zip.DataFormatException;

import assembler.Slice;
import cipher.EncFile;
import cipher.KeyFile;

public class Signer {
	private Key publicKey, privateKey;

	public Signer(String pubKeyPath) {
		try {
			publicKey = RSALibrary.getKey(pubKeyPath);
			privateKey = RSALibrary.getKey(RSALibrary.PRIVATE_KEY_FILE);
		} catch (Exception e) {
			System.err.println("Signer exception: " + e.getMessage());
			System.exit(-1);
		}
	}

	public void sign(ArrayList<Slice> slices) {
		RSA_PSS rsapss = new RSA_PSS(privateKey);

		for (Slice slice : slices) {
			try {
				Signature signature = new Signature(rsapss.sign(slice.getData()));
				slice.getHeader().setSignature(signature);
			} catch (DataFormatException e) {
				System.err.println("Sign exception: " + e.getMessage());
				System.exit(-1);
			}
		}
	}

	public void sign(Slice slice) {
		RSA_PSS rsapss = new RSA_PSS(privateKey);

		try {
			Signature signature = new Signature(rsapss.sign(slice.getData()));
			slice.getHeader().setSignature(signature);
		} catch (DataFormatException e) {
			System.err.println("Sign exception: " + e.getMessage());
			System.exit(-1);
		}
	}

	public void sign(KeyFile keyFile) {
		RSA_PSS rsapss = new RSA_PSS(privateKey);

		try {
			Signature signature = new Signature(rsapss.sign(keyFile.getKey()));
			keyFile.setSignature(signature);
		} catch (DataFormatException e) {
			System.err.println("Sign DF Exception: " + e.getMessage());
			System.exit(-1);
		} catch (Exception e) {
			System.err.println("Sign Exception: " + e.getMessage());
			System.exit(-1);
		}
	}

	public void sign(EncFile file) {
		RSA_PSS rsapss = new RSA_PSS(privateKey);

		try {
			Signature signature = new Signature(rsapss.sign(file.getIv()));
			SecureSignature secsignature = new SecureSignature(signature, (PublicKey) publicKey);

			file.setSignature(secsignature);
		} catch (DataFormatException e) {
			System.err.println("Sign DF Exception: " + e.getMessage());
			System.exit(-1);
		} catch (Exception e) {
			System.err.println("Sign Exception: " + e.getMessage());
			System.exit(-1);
		}
	}

	public boolean verify(ArrayList<Slice> slices) {
		RSA_PSS rsapss = new RSA_PSS(publicKey);
		boolean result = true;

		for (Slice slice : slices) {
			try {
				if (!rsapss.verify(slice.getData(), slice.getHeader().getSignature().getSignature()))
					result = false;
			} catch (DataFormatException e) {
				System.err.println("Verify exception: " + e.getMessage());
				System.exit(-1);
			}
		}

		return result;
	}

	public boolean verify(Slice slice) {
		RSA_PSS rsapss = new RSA_PSS(publicKey);

		try {
			if (!rsapss.verify(slice.getData(), slice.getHeader().getSignature().getSignature()))
				return false;
		} catch (DataFormatException e) {
			System.err.println("Verify exception: " + e.getMessage());
			System.exit(-1);
		}

		return true;
	}

	public boolean verify(KeyFile keyFile) {
		RSA_PSS rsapss = new RSA_PSS(publicKey);
		boolean result = false;

		try {
			result = rsapss.verify(keyFile.getKey(), keyFile.getSignature().getSignature());
		} catch (DataFormatException e) {
			System.err.println("Verify DF Exception: " + e.getMessage());
			System.exit(-1);
		} catch (Exception e) {
			System.err.println("Verify Exception: " + e.getMessage());
			System.exit(-1);
		}

		return result;
	}

	public boolean verify(EncFile file) {
		RSA_PSS rsapss = new RSA_PSS(publicKey);
		SecureSignature secsignature = file.getSignature();
		Signature signature = new Signature(secsignature, (PrivateKey) privateKey);

		try {
			if (!rsapss.verify(file.getIv(), signature.getSignature()))
				return false;
		} catch (DataFormatException e) {
			System.err.println("Verify exception: " + e.getMessage());
			System.exit(-1);
		}

		return true;
	}
}

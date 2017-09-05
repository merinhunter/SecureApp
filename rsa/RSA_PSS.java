package rsa;

import java.math.BigInteger;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.zip.DataFormatException;

import common.Bytes;

public class RSA_PSS {
	public final String HASH_ALGORITHM = "SHA-512";
	public final String PROVIDER = "BC";

	/**
	 * Exponent E (priv or pub), modulus N
	 */
	private BigInteger E, N;

	private MessageDigest md;

	private int emBits, emLen, hLen, sLen;

	public RSA_PSS(Key key) {

		if (key instanceof RSAPrivateKey) {
			E = ((RSAPrivateKey) key).getPrivateExponent();
			N = ((RSAPrivateKey) key).getModulus();
		} else {
			E = ((RSAPublicKey) key).getPublicExponent();
			N = ((RSAPublicKey) key).getModulus();
		}

		this.emBits = this.N.bitLength() - 1;
		this.emLen = (int) Math.ceil(this.emBits / 8.0);

		try {
			this.md = MessageDigest.getInstance(HASH_ALGORITHM, PROVIDER);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}

		this.hLen = md.getDigestLength();
		this.sLen = emLen - hLen - 2;
	}

	/**
	 * RSASSA-PSS-SIGN
	 * 
	 * @param M
	 *            Message to be signed
	 * @return S Signature octet string
	 * @throws DataFormatException
	 */
	public byte[] sign(byte[] M) throws DataFormatException {

		byte[] EM = EMSA_PSS_ENCODE(M);

		BigInteger m = new BigInteger(1, EM);

		BigInteger s = RSASP1(m);

		byte[] S = Bytes.I2OSP(s, this.emLen);

		return S;
	}

	/**
	 * RSASSA-PSS-VERIFY
	 * 
	 * @param M
	 *            Message to be verified
	 * @param S
	 *            Signature of message to be verified
	 * @return boolean If signature is valid or not
	 * @throws DataFormatException
	 */
	public boolean verify(byte[] M, byte[] S) throws DataFormatException {

		if (S.length != emLen)
			throw new InternalError("Invalid signature");

		BigInteger s = new BigInteger(1, S);

		BigInteger m = RSAVP1(s);

		byte[] EM = Bytes.I2OSP(m, this.emLen);

		return EMSA_PSS_VERIFY(M, EM);
	}

	/**
	 * EMSA-PSS-ENCODE
	 * 
	 * @param M
	 *            Message octet string to encode
	 * @return EM encoded octet string message
	 * @throws DataFormatException
	 */
	private byte[] EMSA_PSS_ENCODE(byte[] M) throws DataFormatException {

		this.md.update(M);
		byte[] mHash = this.md.digest();

		if (emLen < hLen + sLen + 2) {
			throw new InternalError("Encoding error");
		}

		byte[] salt = new byte[sLen];
		SecureRandom rng = new SecureRandom();
		rng.nextBytes(salt);

		this.md.update(new byte[8]);
		this.md.update(mHash);

		byte[] H = this.md.digest(salt);

		byte[] PS = new byte[emLen - sLen - hLen - 2];

		byte[] DB = Bytes.concat(PS, new byte[] { (byte) 0x01 }, salt);

		MGF1 mgf1 = new MGF1(this.md);
		byte[] dbMask = mgf1.generateMask(H, emLen - hLen - 1);

		byte[] maskedDB = Bytes.xor(DB, dbMask);

		byte[] MASK = { (byte) 0xFF, 0x7F, 0x3F, 0x1F, 0x0F, 0x07, 0x03, 0x01 };
		int maskBits = 8 * emLen - emBits;
		maskedDB[0] &= MASK[maskBits];

		byte[] EM = Bytes.concat(maskedDB, H, new byte[] { (byte) 0xbc });

		return EM;
	}

	/**
	 * EMSA-PSS-VERIFY
	 * 
	 * @param M
	 *            octet string message to verify
	 * @param EM
	 *            encoded octet string
	 * @return boolean If signature matches message
	 */
	private boolean EMSA_PSS_VERIFY(byte[] M, byte[] EM) {

		this.md.update(M);
		byte[] mHash = this.md.digest();

		if (emLen < hLen + sLen + 2)
			return false;

		if (EM[EM.length - 1] != (byte) 0xbc)
			return false;

		byte[] maskedDB = new byte[emLen - hLen - 1];
		System.arraycopy(EM, 0, maskedDB, 0, maskedDB.length);

		byte[] H = new byte[hLen];
		System.arraycopy(EM, maskedDB.length, H, 0, hLen);

		byte[] MASK = { (byte) 0xFF, 0x7F, 0x3F, 0x1F, 0x0F, 0x07, 0x03, 0x01 };
		int maskBits = 8 * emLen - emBits;
		if ((maskedDB[0] & ~MASK[maskBits]) != 0)
			return false;

		MGF1 mgf1 = new MGF1(this.md);
		byte[] dbMask = mgf1.generateMask(H, emLen - hLen - 1);

		byte[] DB = Bytes.xor(maskedDB, dbMask);

		DB[0] &= MASK[maskBits];

		for (int i = 0; i < (emLen - hLen - sLen - 2); i++) {
			if (DB[i] != 0) {
				return false;
			}
		}

		if (DB[emLen - hLen - sLen - 2] != (byte) 0x1) {
			return false;
		}

		byte[] salt = new byte[sLen];
		System.arraycopy(DB, DB.length - sLen, salt, 0, sLen);

		this.md.reset();
		this.md.update(new byte[8]);
		this.md.update(mHash);

		byte[] Hdash = this.md.digest(salt);

		return Arrays.equals(H, Hdash);
	}

	/**
	 * RSASP1
	 * 
	 * Output: s signature representative, an integer between 0 and n - 1
	 * 
	 * Assumption: RSA private key K is valid
	 * 
	 * @param m
	 *            Message representative
	 * @return s Signature representative
	 */
	private BigInteger RSASP1(BigInteger m) {
		BigInteger s = m.modPow(E, N);
		return s;
	}

	/**
	 * RSAVP1
	 * 
	 * Inverse of RSAVP1
	 * 
	 * Output: m message representative
	 * 
	 * Assumption: RSA public key (n, e) is valid
	 * 
	 * @param s
	 *            Signature representative
	 * @return m Message representative
	 */
	private BigInteger RSAVP1(BigInteger s) {
		return this.RSASP1(s);
	}
}

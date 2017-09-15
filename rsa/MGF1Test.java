package rsa;

import static org.junit.Assert.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

public class MGF1Test {
	private MessageDigest md;

	/**
	 * Tests the Mask Generator Function for a SHA1 digest.
	 */
	@Test
	public void testSHA1() {
		String expected = "1ac9075cd4";
		try {
			md = MessageDigest.getInstance("SHA-1", "BC");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}

		MGF1 mgf1 = new MGF1(this.md);
		byte[] result = mgf1.generateMask("foo".getBytes(), 5);

		System.out.println(Hex.toHexString(result));

		assertEquals(Hex.toHexString(result), expected);
	}

	/**
	 * Tests the Mask Generator Function for a SHA256 digest.
	 */
	@Test
	public void testSHA256() {
		String expected = "382576a7841021cc28fc4c0948753fb8312090cea942ea4c4e735d10dc724b155f9f6069f289d61daca0cb814502ef04eae1";
		try {
			md = MessageDigest.getInstance("SHA-256", "BC");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}

		MGF1 mgf1 = new MGF1(this.md);
		byte[] result = mgf1.generateMask("bar".getBytes(), 50);

		assertEquals(Hex.toHexString(result), expected);
	}

}

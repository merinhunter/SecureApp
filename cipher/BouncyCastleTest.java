package cipher;

import static org.junit.Assert.*;

import java.security.Security;

import org.junit.Test;

public class BouncyCastleTest {

	@Test
	public void testBC() {
		assertNotNull(Security.getProvider("BC"));
	}

}

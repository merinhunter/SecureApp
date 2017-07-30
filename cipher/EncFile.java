package cipher;

import java.util.Base64;

public class EncFile {
	private byte[] iv;
	private byte[] data;

	public EncFile(byte[] data) {
		this.data = data;
	}

	public byte[] getIv() {
		return iv;
	}

	public void setIv(byte[] iv) {
		this.iv = iv;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String toBase64() {
		return new String(Base64.getEncoder().encode(data));
	}
}

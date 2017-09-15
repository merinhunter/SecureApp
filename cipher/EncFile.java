package cipher;

import java.util.Base64;

import common.Bytes;

public class EncFile {
	private byte[] iv;
	private byte[] data;

	public EncFile(byte[] data, byte[] iv) {
		this.data = data;
		this.iv = iv;
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

	public byte[] toBytes() {
		return Bytes.concat(iv, data);
	}

	public String toBase64() {
		return new String(Base64.getEncoder().encode(this.toBytes()));
	}
}

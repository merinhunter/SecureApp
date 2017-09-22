package cipher;

import java.util.Base64;

import common.Bytes;

public class EncFile {
	private byte[] iv;
	private byte[] data;

	public final static int IV_SIZE = AESLibrary.KEY_SIZE;

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

	public static EncFile fromBytes(byte[] src) {
		byte[] iv = new byte[IV_SIZE];
		byte[] data = new byte[src.length - IV_SIZE];

		System.arraycopy(src, 0, iv, 0, IV_SIZE);
		System.arraycopy(src, IV_SIZE, data, 0, data.length);

		return new EncFile(data, iv);
	}

	public byte[] toBase64() {
		return Base64.getEncoder().encode(this.toBytes());
	}

	public static EncFile fromBase64(byte[] base64) {
		return fromBytes(Base64.getDecoder().decode(base64));
	}
}

package cipher;

import java.util.Base64;

import common.Bytes;
import common.RandomString;

public class EncFile {
	private byte[] id;
	private byte[] iv;
	private byte[] data;

	public final static int ID_SIZE = RandomString.DEFAULT_SIZE;
	public final static int IV_SIZE = AESLibrary.KEY_SIZE;

	public EncFile(byte[] data, byte[] iv) {
		this.data = data;
		this.iv = iv;
	}

	public EncFile(byte[] id, byte[] data, byte[] iv) {
		this.id = id;
		this.data = data;
		this.iv = iv;
	}

	public byte[] getID() {
		return id;
	}

	public void setID(byte[] id) {
		this.id = id;
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
		return Bytes.concat(id, iv, data);
	}

	public static EncFile fromBytes(byte[] src) {
		byte[] id = new byte[ID_SIZE];
		byte[] iv = new byte[IV_SIZE];
		byte[] data = new byte[src.length - ID_SIZE - IV_SIZE];

		System.arraycopy(src, 0, id, 0, ID_SIZE);
		System.arraycopy(src, ID_SIZE, iv, 0, IV_SIZE);
		System.arraycopy(src, ID_SIZE + IV_SIZE, data, 0, data.length);

		return new EncFile(id, data, iv);
	}

	public byte[] toBase64() {
		return Base64.getEncoder().encode(this.toBytes());
	}

	public static EncFile fromBase64(byte[] base64) {
		return fromBytes(Base64.getDecoder().decode(base64));
	}
}

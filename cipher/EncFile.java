package cipher;

public class EncFile {
	private byte[] data;

	public EncFile(byte[] data) {
		this.data = data;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String toString() {
		return new String(data);
	}
}

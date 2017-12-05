package cipher;

public class FalseHeader {
	private byte[] ID;

	public byte[] getID() {
		return ID;
	}

	public void setID(byte[] id) {
		ID = id;
	}

	@Override
	public String toString() {
		return new String(this.ID);
	}
}

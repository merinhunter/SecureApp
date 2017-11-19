package assembler;

import java.nio.ByteBuffer;

import common.RandomString;
import rsa.Signature;

public class Header {
	private int index;
	private long nBlocks;
	private byte[] sessionID;
	private long fileSize;
	private Signature signature;

	public final static int ID_SIZE = RandomString.DEFAULT_SIZE;
	public final static int HEADER_SIZE = Integer.BYTES + Long.BYTES + RandomString.DEFAULT_SIZE + Long.BYTES
			+ Signature.BYTES;

	public Header(long nBlocks, long fileSize, String sessionID) {
		this.nBlocks = nBlocks;
		this.sessionID = sessionID.getBytes();
		this.fileSize = fileSize;
	}

	public Header(Header header) {
		this.nBlocks = header.nBlocks;
		this.sessionID = header.sessionID;
		this.fileSize = header.fileSize;
	}

	private Header() {
	}

	public int getIndex() {
		return this.index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public long getnBlocks() {
		return nBlocks;
	}

	public byte[] getSessionID() {
		return sessionID;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public Signature getSignature() {
		return signature;
	}

	public void setSignature(Signature signature) {
		this.signature = signature;
	}

	public byte[] toBytes() {
		ByteBuffer buffer = ByteBuffer.allocate(HEADER_SIZE);

		buffer.putInt(index);
		buffer.putLong(nBlocks);
		buffer.put(sessionID, 0, ID_SIZE);
		buffer.putLong(fileSize);
		buffer.put(signature.getSignature(), 0, Signature.BYTES);

		return buffer.array();
	}

	public static Header fromBytes(ByteBuffer buffer) {
		Header header = new Header();
		byte[] sessionID = new byte[ID_SIZE];
		byte[] signature = new byte[Signature.BYTES];

		header.index = buffer.getInt();
		header.nBlocks = buffer.getLong();
		buffer.get(sessionID);
		header.sessionID = sessionID;
		header.fileSize = buffer.getLong();
		buffer.get(signature);
		header.signature = new Signature(signature);

		return header;
	}

	@Override
	public String toString() {
		String s = "";

		return s + index + "|" + nBlocks + "|" + new String(sessionID) + "|" + fileSize + "|"
				+ this.signature.toString();
	}
}

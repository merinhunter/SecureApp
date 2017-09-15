package assembler;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;

import org.bouncycastle.util.encoders.Hex;

public class Header {
	private int index;
	private int originalSize;
	private long nBlocks;
	private byte[] hash;
	private long fileSize;
	public final static int HEADER_SIZE = Integer.BYTES + Integer.BYTES + Long.BYTES + SHA512CheckSum.SHA512_SIZE + Long.BYTES;

	public Header(long nBlocks, File file) throws FileNotFoundException, Exception {
		this.nBlocks = nBlocks;
		this.hash = SHA512CheckSum.checksum(file);
		this.fileSize = file.length();
	}

	public Header(Header header) {
		this.nBlocks = header.nBlocks;
		this.hash = header.hash;
		this.fileSize = header.fileSize;
	}

	public Header() {
	}

	public int getIndex() {
		return this.index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getOriginalSize() {
		return originalSize;
	}

	public void setOriginalSize(int originalSize) {
		this.originalSize = originalSize;
	}

	public long getnBlocks() {
		return nBlocks;
	}

	public byte[] getHash() {
		return hash;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public byte[] toBytes() {
		ByteBuffer buffer = ByteBuffer.allocate(HEADER_SIZE);

		buffer.putInt(index);
		buffer.putInt(originalSize);
		buffer.putLong(nBlocks);
		buffer.put(hash, 0, SHA512CheckSum.SHA512_SIZE);
		buffer.putLong(fileSize);

		return buffer.array();
	}

	public static Header fromBytes(ByteBuffer buffer) {
		Header header = new Header();
		byte[] hash = new byte[SHA512CheckSum.SHA512_SIZE];

		header.index = buffer.getInt();
		header.originalSize = buffer.getInt();
		header.nBlocks = buffer.getLong();
		buffer.get(hash);
		header.hash = hash;
		header.fileSize = buffer.getLong();

		return header;
	}

	@Override
	public String toString() {
		String s = "";

		return s + index + "|" + originalSize + "|" + nBlocks + "|" + Hex.toHexString(hash) + "|" + fileSize;
	}
}

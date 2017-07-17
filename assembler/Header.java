package assembler;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;

import org.bouncycastle.util.encoders.Hex;

public class Header {
	private int count;
	private long blocks;
	private byte[] hash;
	private long size;
	public final static int HEADER_SIZE = Integer.BYTES + Long.BYTES + SHA512CheckSum.SHA512_SIZE + Long.BYTES;

	public Header(long blocks, File file) throws FileNotFoundException, Exception {
		this.blocks = blocks;
		// sha512 = new SHA512CheckSum(path);
		this.hash = SHA512CheckSum.checksum(file);
		this.size = file.length();
	}

	public Header(Header header) {
		this.blocks = header.blocks;
		this.hash = header.hash;
		this.size = header.size;
	}

	/*public Header(int count, long blocks, byte[] hash, long size) {
		this.count = count;
		this.blocks = blocks;
		this.hash = hash;
		this.size = size;
	}*/

	public Header() {
	}
	
	public int getCount() {
		return this.count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public long getSize() {
		return this.size;
	}

	public byte[] toBytes() {
		ByteBuffer buffer = ByteBuffer.allocate(HEADER_SIZE);

		buffer.putInt(count);
		buffer.putLong(blocks);
		buffer.put(hash, 0, SHA512CheckSum.SHA512_SIZE);
		buffer.putLong(size);

		return buffer.array();
	}

	public static Header fromBytes(ByteBuffer buffer) {
		Header header = new Header();
		byte[] hash = new byte[SHA512CheckSum.SHA512_SIZE];

		header.count = buffer.getInt();
		header.blocks = buffer.getLong();
		buffer.get(hash);
		header.hash = hash;
		header.size = buffer.getLong();

		return header;
	}

	public String toString() {
		String s = "";

		return s + count + "|" + blocks + "|" + Hex.toHexString(hash) + "|" + size;
	}
}

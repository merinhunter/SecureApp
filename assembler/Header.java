package assembler;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;

import org.bouncycastle.util.encoders.Hex;

import rsa.Signature;

public class Header {
	private int index;
	private long nBlocks;
	private byte[] hash;
	private long fileSize;
	private Signature signature;
	public final static int HEADER_SIZE = Integer.BYTES + Long.BYTES + SHA512CheckSum.SHA512_SIZE + Long.BYTES
			+ Signature.BYTES;

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
		buffer.put(hash, 0, SHA512CheckSum.SHA512_SIZE);
		buffer.putLong(fileSize);
		buffer.put(signature.getSignature(), 0, Signature.BYTES);

		return buffer.array();
	}

	public static Header fromBytes(ByteBuffer buffer) {
		Header header = new Header();
		byte[] hash = new byte[SHA512CheckSum.SHA512_SIZE];
		byte[] signature = new byte[Signature.BYTES];

		header.index = buffer.getInt();
		header.nBlocks = buffer.getLong();
		buffer.get(hash);
		header.hash = hash;
		header.fileSize = buffer.getLong();
		buffer.get(signature);
		header.signature = new Signature(signature);

		return header;
	}

	@Override
	public String toString() {
		String s = "";

		return s + index + "|" + nBlocks + "|" + Hex.toHexString(hash) + "|" + fileSize
				+ Hex.toHexString(signature.getSignature());
	}
}

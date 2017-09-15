package assembler;

import java.nio.ByteBuffer;

public class Slice {
	private Header header;
	private byte[] data;

	public Slice(int index, Header header) {
		this.header = new Header(header);
		this.header.setIndex(index);
	}

	public Slice(Header header) {
		this.header = header;
	}

	public Header getHeader() {
		return header;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public byte[] toBytes() {
		ByteBuffer buffer = ByteBuffer.allocate(Header.HEADER_SIZE + data.length);

		buffer.put(header.toBytes(), 0, Header.HEADER_SIZE);
		buffer.put(data, 0, data.length);

		return buffer.array();
	}

	public static Slice fromBytes(byte[] barray) {
		ByteBuffer buffer = ByteBuffer.wrap(barray);
		byte[] data = new byte[barray.length - Header.HEADER_SIZE];

		Header header = Header.fromBytes(buffer);
		Slice slice = new Slice(header);
		buffer.get(data);
		slice.setData(data);

		return slice;
	}

	@Override
	public String toString() {
		return header.toString() + "|" + new String(data);
	}
}
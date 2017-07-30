package assembler;

import java.nio.ByteBuffer;

public class Slice {
	private Header header;
	private byte[] data;

	public Slice(int count, Header header) {
		// System.out.println(count);
		this.header = new Header(header);
		this.header.setCount(count);
	}

	public Slice(Header header) {
		this.header = header;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public Header getHeader() {
		return header;
	}

	public byte[] toBytes() {
		ByteBuffer buffer = ByteBuffer.allocate(Header.HEADER_SIZE + data.length);

		buffer.put(header.toBytes(), 0, Header.HEADER_SIZE);
		buffer.put(data, 0, data.length);

		return buffer.array();
	}

	public static Slice fromBytes(byte[] barray) {
		// Slice slice = null;
		// System.out.println(barray.length);
		ByteBuffer buffer = ByteBuffer.wrap(barray);
		// System.out.println(barray.length);
		// System.out.println(new String(barray));
		byte[] data = new byte[barray.length - Header.HEADER_SIZE];

		Header header = Header.fromBytes(buffer);
		Slice slice = new Slice(header);
		// ByteBuffer buffer = ByteBuffer.wrap(barray, Header.HEADER_SIZE, barray.length - Header.HEADER_SIZE);
		buffer.get(data);
		// System.out.println(new String(data));
		slice.setData(data);

		return slice;
	}

	public String toString() {
		return header.toString() + "|" + new String(data);
	}
}
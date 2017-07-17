package assembler;

import java.io.*;
import java.util.ArrayList;

public class Slicer {
	private File file;
	private int size_b;
	private Header header;
	//private int counter = 0;
	//private FileInputStream input;

	public Slicer(File f, int size_b) throws FileNotFoundException, Exception {
		this.file = f;
		this.size_b = size_b;
		this.header = new Header(getBlocks(), file);
		//input = new FileInputStream(file);
	}

	public long getSize() {
		return file.length();
	}

	public long getBlocks() {
		long nblocks = getSize() / size_b;

		if (getSize() % size_b != 0)
			nblocks++;

		return nblocks;
	}

	public ArrayList<Slice> slice() throws FileNotFoundException {
		ArrayList<Slice> slices = new ArrayList<>();
		long nblocks = getBlocks();
		FileInputStream input = new FileInputStream(file);

		try {
			for (int i = 0; i < nblocks; i++) {
				Slice slice = new Slice(i, header);
				byte[] buf = new byte[size_b];

				System.out.println(input.read(buf, 0, size_b));
				//input.skip(10);
				//System.out.println(new String(buf));
				//System.out.println(buf.length);
				slice.setData(buf);
				//System.out.println(slice.toString());
				slices.add(slice);
				//input.skip(0);
			}
		} catch (IOException e) {
			System.err.println("Error reading file: " + e);
			System.exit(-1);
		} finally {
			try {
				System.out.println("Cierro");
				input.close();
			} catch (IOException e) {
				System.err.println("IO Exception closing files:");
				e.printStackTrace();
			}
		}

		/*Slice slice = new Slice(counter, header);
		byte[] buf = new byte[size_b];
		int bytes_readed = 0;

		try {
			System.out.println(bytes_readed = input.read(buf, 0, buf.length));
			counter++;
			slice.setData(buf);
		} catch (IOException e) {
			System.err.println("Error reading file: " + e);
		} finally {
			try {
				if (bytes_readed < buf.length) {
					System.out.println("Cierro");
					input.close();
				}
			} catch (IOException e) {
				System.err.println("IO Exception closing files:");
				e.printStackTrace();
			}
		}*/

		return slices;
	}
}
